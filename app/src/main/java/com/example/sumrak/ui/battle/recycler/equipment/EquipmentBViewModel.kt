package com.example.sumrak.ui.battle.recycler.equipment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sumrak.data.DataBase
import com.example.sumrak.data.inventory.consumables.ConsumablesRepository
import com.example.sumrak.data.inventory.equipment.EquipmentRepository
import com.example.sumrak.ui.battle.recycler.equipment.item.EquipmentBItemManager
import com.example.sumrak.ui.inventory.recycler.consumables.item.ConsumablesItemManager
import com.example.sumrak.ui.inventory.recycler.equipment.item.EquipmentItem
import com.example.sumrak.ui.inventory.recycler.equipment.item.EquipmentItemManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class EquipmentBViewModel(application: Application) : AndroidViewModel(application) {
    private val equipmentManager = EquipmentBItemManager.getInstance()
    private val consumablesManager = ConsumablesItemManager.getInstance()

    private val repository: EquipmentRepository
    private val repositoryCons: ConsumablesRepository

    val equipmentItem = MutableLiveData<EquipmentItem>()
    private fun updatechargeEquipmentDb(id: Int, change: Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateChargeEquipment(id, change)
        }
    }
    fun getEquipmentToIdPlayer(id : Int){
        equipmentManager.clearList()
        viewModelScope.launch(Dispatchers.IO) {
            repository.getEquipmentToIdPlayer(id).forEach {
                equipmentManager.addItem(it.toEquipmentItem())
            }
        }
        viewModelScope.launch(Dispatchers.IO){
            consumablesManager.clearConsumablesList()
            repositoryCons.getConsumablesToPlayerId(id).forEach {
                consumablesManager.loadItemToPlayer(it.toConsumablesItem())
            }
        }
    }




    private fun updateChargeEquipment(id: Int, change: Int){
        val item = equipmentManager.getItemToId(id)
        item.charge += change
        if (item.charge<0) item.charge = 0
        equipmentManager.updateItem(item)
        updatechargeEquipmentDb(id, item.charge)
        equipmentItem.postValue(item)

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

    init {
        val daoEquipmentDb = DataBase.getDb(application).getEquipmentDao()
        repository = EquipmentRepository(daoEquipmentDb)

        val daoConsumablesDb = DataBase.getDb(application).getConsumablesDao()
        repositoryCons = ConsumablesRepository(daoConsumablesDb)
    }
    companion object{
        private var instance : EquipmentBViewModel? = null

        @JvmStatic
        fun getInstance(application: Application): EquipmentBViewModel {
            return instance ?: synchronized(this){
                val newInstance = EquipmentBViewModel(application)
                instance = newInstance
                newInstance
            }
        }
    }
}