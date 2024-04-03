package com.example.sumrak.ui.inventory.recycler.equipment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sumrak.data.DataBase
import com.example.sumrak.data.inventory.equipment.EquipmentDbEntity
import com.example.sumrak.data.inventory.equipment.EquipmentRepository
import com.example.sumrak.Player
import com.example.sumrak.data.inventory.consumables.ConsumablesRepository
import com.example.sumrak.ui.inventory.recycler.consumables.item.ConsumablesItemManager
import com.example.sumrak.ui.inventory.recycler.equipment.item.EquipmentItem
import com.example.sumrak.ui.inventory.recycler.equipment.item.EquipmentItemManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class EquipmentViewModel(application: Application) : AndroidViewModel(application) {

    private val equipmentManager = EquipmentItemManager.getInstance()
    private val consumablesManager = ConsumablesItemManager.getInstance()

    private val repository: EquipmentRepository
    private val repositoryCons: ConsumablesRepository

    val equipmentItem = MutableLiveData<EquipmentItem>()
    var activeIdItemSettings = 0
    var activeIdItemRepair = 0
    var activeIdItemLink = -1
    val liveIdItemLink = MutableLiveData<Int>()
    val modeSettings = MutableLiveData<Int>()

    // TODO runBlocking - опасная команда, лучше искать альтернативы
    private fun addEquipmentDb(equipmentDbEntity: EquipmentDbEntity) : Long {
        return runBlocking {
            val result = CoroutineScope(Dispatchers.IO).async{
                repository.addEquipment(equipmentDbEntity)
            }
            result.await()
        }
    }

    private fun updateEquipmentDb(equipmentDbEntity: EquipmentDbEntity){
       viewModelScope.launch(Dispatchers.IO) {
           repository.updateEquipment(equipmentDbEntity)
       }
    }

    private fun updatechargeEquipmentDb(id: Int, change: Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateChargeEquipment(id, change)
        }
    }

    private fun deleteEquipmentDb(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteEquipmentToId(id)
        }
    }

    fun getEquipmentToIdPlayer(id : Int){
        equipmentManager.clearList()
        viewModelScope.launch(Dispatchers.IO) {
            repository.getEquipmentToIdPlayer(id).forEach {
                equipmentManager.addItem(it.toEquipmentItem())
            }
        }
    }

    fun addEquipmentItem(name: String, change: Int, step: Int, test: Boolean, consumablesLink: Int){
        val id = addEquipmentDb(EquipmentDbEntity(0, Player.getInstance().getIdActivePlayer(), name, change, change, step, test, consumablesLink)).toInt()
        equipmentManager.addItem(EquipmentItem(id, Player.getInstance().getIdActivePlayer(), name, change, change, step, test, consumablesLink))
        consumablesManager.replaceConsumablesView()
    }

    fun updateEquipmentItem(equipmentItem: EquipmentItem){
        updateEquipmentDb(equipmentItem.toEquipmentDbEntity())
        equipmentManager.updateItem(equipmentItem)
        consumablesManager.replaceConsumablesView()
    }

    fun updateChargeEquipment(id: Int, change: Int){
        val item = equipmentManager.getItemToId(id)
        item.charge += change
        if (item.charge<0) item.charge = 0
        equipmentManager.updateItem(item)
        updatechargeEquipmentDb(id, item.charge)
        equipmentItem.postValue(item)

    }
    fun getActiveRepairItem(): EquipmentItem {
        return equipmentManager.getItemToId(activeIdItemRepair)
    }


    fun replaceChangeEquipment(id: Int){
        val item = equipmentManager.getItemToId(id)
        if (item.consumablesLink > 0){
            useConsyumablesToSaveBD(item.consumablesLink)
        }
        item.charge = item.maxCharge
        equipmentManager.updateItem(item)
        updatechargeEquipmentDb(id, item.charge)
        equipmentItem.postValue(item)
    }

    fun useEquipmentChange(id: Int){
        val item = equipmentManager.getItemToId(id)
        updateChargeEquipment(item.id, -item.step)
    }

    fun deleteEquipmentItem(id: Int){
        deleteEquipmentDb(id)
        equipmentManager.deleteItemToId(id)
        setMode(0,0)
        consumablesManager.replaceConsumablesView()
    }

    fun setMode(mode: Int, id: Int){
        if (id == 0){
            modeSettings.postValue(mode)
            activeIdItemSettings = 0
            activeIdItemRepair = 0
            equipmentItem.postValue(EquipmentItem(0,0,"",0,0,0,false, -1))
        }
        else{
            if (mode==3){
                if (id == activeIdItemSettings){
                    activeIdItemSettings = 0
                    modeSettings.postValue(0)
                }
                else{
                    activeIdItemRepair = 0
                    activeIdItemSettings = id
                    equipmentItem.postValue(equipmentManager.getItemToId(id))
                    modeSettings.postValue(mode)
                }
            }
            else if (mode==4){
                if (id == activeIdItemRepair){
                    activeIdItemRepair = 0
                    modeSettings.postValue(0)
                }
                else{
                    activeIdItemSettings = 0
                    activeIdItemRepair = id
                    equipmentItem.postValue(equipmentManager.getItemToId(id))
                    modeSettings.postValue(mode)
                }
            }
            else{
                modeSettings.postValue(mode)
            }

        }
    }

    fun controlConsumablesToId() : Boolean{
        return consumablesManager.controlConsumablesToId(activeIdItemLink)
    }

    fun getValueConsumablesToId(id: Int) : Int{
        return consumablesManager.getValueToConsumablesId(id)
    }

    fun useConsyumablesToSaveBD(id: Int){
        consumablesManager.useConsumablesLinkEquipent(id)
        val value = getValueConsumablesToId(id)

        viewModelScope.launch(Dispatchers.IO) {
            repositoryCons.updateValueConsumablesTuples(id, value)
        }
    }

    fun setModeLink(mode: Boolean){
        val item = equipmentManager.getItemToId(activeIdItemSettings)
        activeIdItemLink = item.consumablesLink

        if (mode){
            if (consumablesManager.getItemCount()>0){
                if (activeIdItemLink < 1){
                    activeIdItemLink = consumablesManager.getLastConsumablesItemId()
                }
            }
            else activeIdItemLink = 0
        }
        else{
            activeIdItemLink = -1
        }
        liveIdItemLink.postValue(activeIdItemLink)

    }

    fun getConsumablesToId(id: Int) : String{
        if (id == 0){
            return "Отсутсвуют Расходники"
        }
        return "${consumablesManager.getItemToId(id).name} Количество: ${consumablesManager.getItemToId(id).value}"
    }

    fun stepConsumablesItemLink(step: Int){
        val item = equipmentManager.getItemToId(activeIdItemSettings)
        val newId = consumablesManager.nextConsumblesItemId(activeIdItemLink, step)
        item.consumablesLink = newId
        activeIdItemLink = newId
        liveIdItemLink.postValue(activeIdItemLink)
    }


    init {
        val daoEquipmentDb = DataBase.getDb(application).getEquipmentDao()
        repository = EquipmentRepository(daoEquipmentDb)

        val daoConsumablesDb = DataBase.getDb(application).getConsumablesDao()
        repositoryCons = ConsumablesRepository(daoConsumablesDb)
    }


    companion object{
        private var instance : EquipmentViewModel? = null

        @JvmStatic
        fun getInstance(application: Application): EquipmentViewModel{
            return instance ?: synchronized(this){
                val newInstance = EquipmentViewModel(application)
                instance = newInstance
                newInstance
            }
        }
    }
}