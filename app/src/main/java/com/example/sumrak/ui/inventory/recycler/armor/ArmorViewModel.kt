package com.example.sumrak.ui.inventory.recycler.armor

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sumrak.Data.DataBase
import com.example.sumrak.Data.inventory.armor.ArmorRepository
import com.example.sumrak.Data.playerdb.PlayerReposiroty
import com.example.sumrak.Player
import com.example.sumrak.ui.inventory.recycler.armor.item.ArmorItem
import com.example.sumrak.ui.inventory.recycler.armor.item.ArmorItemManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ArmorViewModel(application: Application) : AndroidViewModel(application) {
    private val repositoryPlayer: PlayerReposiroty
    private val repository: ArmorRepository
    private val armorItemManager = ArmorItemManager.getInstance()

    val selectedNoArmor = MutableLiveData<Boolean>()
    val modeSettings = MutableLiveData<Int>()
    val visibleView = MutableLiveData<Boolean>()
    val repairProgress = MutableLiveData<Int>()
    var activeItemRepair = -1
    var activeItem = -1
    var visible = true
    var armorItem = ArmorItem(0, 0, "", "", 0, 0,0,"")

    fun addArmor(name: String, params: Int, endurance : Int, features : String){
        armorItem.idPlayer = Player.getInstance().getIdActivePlayer()
        armorItem.name = name
        armorItem.params = params
        armorItem.endurance = endurance
        armorItem.enduranceMax = endurance
        armorItem.features = features
        armorItem.id = runBlocking {
            val result = CoroutineScope(Dispatchers.IO).async{
                repository.addArmor(armorItem.toArmorDbEntity())
            }
            result.await()
        }.toInt()
        armorItemManager.addItem(armorItem)
        modeAddArmor(false)
    }

    fun updateArmor(name: String, params: Int, endurance : Int, features : String){
        if (armorItem.endurance > endurance){
            armorItem.endurance = endurance
        }
        else if (armorItem.endurance == armorItem.enduranceMax){
            armorItem.endurance = endurance
        }
        armorItem.name = name
        armorItem.params = params
        armorItem.enduranceMax = endurance
        armorItem.features = features
        updateArmorToBd(armorItem)
        armorItemManager.updateArmor(armorItem, armorItem.id)
        modeAddArmor(false)
    }

    fun updateArmorToBd (armorItemm : ArmorItem){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateArmor(armorItemm.toArmorDbEntity())
        }
    }

    fun updateArmorToRepair(step : Int){
        if (step == 1){
            if (armorItem.endurance < armorItem.enduranceMax){
                armorItem.endurance++
            }
        }
        else{
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
        if (armorItem.id == Player.getInstance().getActivePlayer().active_armor){
            selectedNoArmor.postValue(true)
            val idPlayer = Player.getInstance().getActivePlayer().id
            viewModelScope.launch(Dispatchers.IO) {
                repositoryPlayer.updateArmorIdPlayer(idPlayer!!, 0)
            }
        }
        armorItemManager.deleteItem(armorItem.id)
        modeAddArmor(false)

    }

    fun deleteArmorToBd(id: Int){
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
        repositoryPlayer = PlayerReposiroty(daoPlayerDb)
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