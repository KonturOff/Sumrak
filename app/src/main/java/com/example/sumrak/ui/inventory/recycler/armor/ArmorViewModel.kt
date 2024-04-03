package com.example.sumrak.ui.inventory.recycler.armor

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sumrak.data.DataBase
import com.example.sumrak.data.inventory.armor.ArmorRepository
import com.example.sumrak.data.playerdb.PlayerRepository
import com.example.sumrak.Player
import com.example.sumrak.ui.inventory.recycler.armor.item.ArmorItem
import com.example.sumrak.ui.inventory.recycler.armor.item.ArmorItemManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ArmorViewModel(application: Application) : AndroidViewModel(application) {
    private val repositoryPlayer: PlayerRepository
    private val repository: ArmorRepository
    private val armorItemManager = ArmorItemManager.getInstance()

    val selectedNoArmor = MutableLiveData<Boolean>()
    val modeSettings = MutableLiveData<Int>()
    private val visibleView = MutableLiveData<Boolean>()
    val repairProgress = MutableLiveData<Int>()
    private var activeItemRepair = -1
    private var activeItem = -1
    var visible = true
    var armorItem = ArmorItem(0, 0, "", "", 0, 0,0,"")

    fun addArmor(name: String, params: Int, endurance : Int, features : String) {
        armorItem.apply {
            this.idPlayer = Player.getInstance().getIdActivePlayer()
            this.name = name
            this.params = params
            this.endurance = endurance
            this.enduranceMax = endurance
            this.features = features
            this.id = runBlocking {
                val result = CoroutineScope(Dispatchers.IO).async {
                    repository.addArmor(toArmorDbEntity())
                }
                result.await()
            }.toInt()
            armorItemManager.addItem(armorItem)
        }
        modeAddArmor(false)
    }

    fun updateArmor(name: String, params: Int, endurance : Int, features : String) {
        armorItem.apply {
            if (this.endurance > endurance) {
                this.endurance = endurance
            } else if (this.endurance == this.enduranceMax) {
                this.endurance = endurance
            }
            this.name = name
            this.params = params
            this.enduranceMax = endurance
            this.features = features
            updateArmorToBd(armorItem)
            armorItemManager.updateArmor(armorItem, this.id)
        }
        modeAddArmor(false)
    }

    private fun updateArmorToBd (armorItem : ArmorItem){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateArmor(armorItem.toArmorDbEntity())
        }
    }

    fun updateArmorToRepair(step : Int){
        if (step == 1){
            if (armorItem.endurance < armorItem.enduranceMax){
                armorItem.endurance++
            }
        }
        else {
            if (armorItem.endurance > 0){
                armorItem.endurance--
            }
        }
        repairProgress.postValue(armorItem.endurance)
        armorItemManager.updateEnduranceArmor(armorItem.endurance, armorItem.id)
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateEnduranceArmor(armorItem.id, armorItem.endurance)
        }
    }

    fun deleteArmor(){
        deleteArmorToBd(armorItem.id)
        if (armorItem.id == Player.getInstance().getActivePlayer().activeArmor){
            selectedNoArmor.postValue(true)
            val idPlayer = Player.getInstance().getActivePlayer().id
            viewModelScope.launch(Dispatchers.IO) {
                repositoryPlayer.updateArmorIdPlayer(idPlayer!!, 0)
            }
        }
        armorItemManager.deleteItem(armorItem.id)
        modeAddArmor(false)

    }

    private fun deleteArmorToBd(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteArmorToId(id)
        }
    }

    fun getArmorToPlayer(id : Int){
        viewModelScope.launch(Dispatchers.IO) {
            armorItemManager.clearArmorList()
            armorItemManager.loarArmorToPlayer(ArmorItem(0,id,"Без брони", "Без брони", 0, 0, 0, ""))
            repository.getArmorListToPlayer(id).forEach {
                armorItemManager.loarArmorToPlayer(it.toArmorItem())
            }
        }
    }

    fun setActiveArmorToPlayer(position : Int){
        val idArmor = armorItemManager.getItem(position).id
        val idPlayer = Player.getInstance().getActivePlayer().id

        viewModelScope.launch(Dispatchers.IO) {
            repositoryPlayer.updateArmorIdPlayer(idPlayer!!, idArmor)
        }
    }

    fun setClassArmor(classArmor: String){
        armorItem.classArmor = classArmor
    }

    fun tvClick(){
        if (visible){
            visible = false
            visibleView.postValue(false)
        }
        else{
            visible = true
            visibleView.postValue(true)
        }
    }

    fun modeRepairArmor(id: Int){
        if (id==activeItemRepair){
            modeAddArmor(false)
        }
        else{
            modeSettings.postValue(3)
            armorItem = armorItemManager.getItemToId(id)
            activeItemRepair = id
            activeItem = -1
            repairProgress.postValue(armorItem.endurance)
        }
    }

    fun modeSettingsArmor(id : Int){
        if (id==activeItem){
            modeAddArmor(false)
        }
        else{
            modeSettings.postValue(2)
            armorItem = armorItemManager.getItemToId(id)
            activeItem = id
            activeItemRepair = -1
        }
    }

    fun modeAddArmor(mode : Boolean){
        if (mode){
            modeSettings.postValue(1)
        }
        else modeSettings.postValue(0)
        activeItem = -1
        activeItemRepair = -1
        armorItem = ArmorItem(0,0,"","",0,0,0,"")
    }

    init {
        val daoArmorDb = DataBase.getDb(application).getArmorDao()
        repository = ArmorRepository(daoArmorDb)

        val daoPlayerDb = DataBase.getDb(application).getPlayerDao()
        repositoryPlayer = PlayerRepository(daoPlayerDb)
    }

    companion object{
        private var instance : ArmorViewModel? = null

        @JvmStatic
        fun getInstance(application: Application): ArmorViewModel{
            return  instance ?: synchronized(this){
                val newInstance  = ArmorViewModel(application)
                instance = newInstance
                newInstance
            }
        }
    }
}