package com.example.sumrak.ui.inventory.recycler.equipment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sumrak.Data.DataBase
import com.example.sumrak.Data.inventory.equipment.EquipmentDbEntity
import com.example.sumrak.Data.inventory.equipment.EquipmentRepository
import com.example.sumrak.Player
import com.example.sumrak.ui.inventory.recycler.equipment.item.EquipmentItem
import com.example.sumrak.ui.inventory.recycler.equipment.item.EquipmentItemManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class EquipmentViewModel(application: Application) : AndroidViewModel(application) {
    private val equipmentManager = EquipmentItemManager.getInstance()

    private val repository: EquipmentRepository

    val equipmentItem = MutableLiveData<EquipmentItem>()
    var activeIdItemSettings = 0
    var activeIdItemRepair = 0
    val modeSettings = MutableLiveData<Int>()

    fun addEquipmentDb(equipmentDbEntity: EquipmentDbEntity):Long{
        return runBlocking {
            val result = CoroutineScope(Dispatchers.IO).async{
                repository.addEquipment(equipmentDbEntity)
            }
            result.await()
        }
    }

    fun updateEquipmentDb(equipmentDbEntity: EquipmentDbEntity){
       viewModelScope.launch(Dispatchers.IO) {
           repository.updateEquipment(equipmentDbEntity)
       }
    }

    fun updatechargeEquipmentDb(id: Int, change: Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateChargeEquipment(id, change)
        }
    }

    fun deleteEquipmentDb(id: Int){
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

    fun addEquipmentItem(name : String, change : Int, step : Int, test : Boolean){
        val id = addEquipmentDb(EquipmentDbEntity(0, Player.getInstance().getIdActivePlayer(), name, change, change, step, test)).toInt()
        equipmentManager.addItem(EquipmentItem(id, Player.getInstance().getIdActivePlayer(), name, change, change, step, test))
    }

    fun updateEquipmentItem(equipmentItem: EquipmentItem){
        updateEquipmentDb(equipmentItem.ToEquipmentDbEntity())
        equipmentManager.updateItem(equipmentItem)
    }

    fun updateChargeEquipment(id: Int, change: Int){
        var item = equipmentManager.getItemToId(id)
        item.charge = item.charge + change
        if (item.charge<0) item.charge = 0
        equipmentManager.updateItem(item)
        updatechargeEquipmentDb(id, item.charge)
        equipmentItem.postValue(item)

    }
    fun getActiveRepairItem(): EquipmentItem {
        return equipmentManager.getItemToId(activeIdItemRepair)
    }

    fun replaceChangeEquipment(id: Int){
        var item = equipmentManager.getItemToId(id)
        item.charge = item.maxCharge
        equipmentManager.updateItem(item)
        updatechargeEquipmentDb(id, item.charge)
        equipmentItem.postValue(item)
    }

    fun useEquipmentChange(id: Int){
        var item = equipmentManager.getItemToId(id)
        updateChargeEquipment(item.id, -item.step)
    }

    fun deleteEquipmentItem(id: Int){
        deleteEquipmentDb(id)
        equipmentManager.deleteItemToId(id)
        setMode(0,0)
    }

    fun setMode(mode: Int, id: Int){
        if (id == 0){
            modeSettings.postValue(mode)
            activeIdItemSettings = 0
            activeIdItemRepair = 0
            equipmentItem.postValue(EquipmentItem(0,0,"",0,0,0,false))
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


    init {
        val daoEquipmentDb = DataBase.getDb(application).getEquipmentDao()
        repository = EquipmentRepository(daoEquipmentDb)
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