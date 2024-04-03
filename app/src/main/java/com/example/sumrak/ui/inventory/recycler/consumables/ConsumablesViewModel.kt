package com.example.sumrak.ui.inventory.recycler.consumables


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sumrak.data.DataBase
import com.example.sumrak.data.inventory.consumables.ConsumablesDbEntity
import com.example.sumrak.data.inventory.consumables.ConsumablesRepository
import com.example.sumrak.ui.inventory.recycler.consumables.item.ConsumablesItemManager
import com.example.sumrak.ui.inventory.recycler.consumables.item.ConsumablesItem
import com.example.sumrak.ui.inventory.recycler.equipment.item.EquipmentItemManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ConsumablesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ConsumablesRepository
    private val consumablesItemManager = ConsumablesItemManager.getInstance()
    private val equipmentManager = EquipmentItemManager.getInstance()


    private var viewVisible = true
    var r = MutableLiveData<Boolean>()

    private fun addConsumables(consumablesDbEntity: ConsumablesDbEntity) : Long {
        return runBlocking {
            val result = CoroutineScope(Dispatchers.IO).async {
                repository.addConsumables(consumablesDbEntity)
            }
            result.await()
        }
    }

    fun getConsumablesToPlayerId(id : Int) {
        viewModelScope.launch(Dispatchers.IO){
            consumablesItemManager.clearConsumablesList()
            repository.getConsumablesToPlayerId(id).forEach {
                consumablesItemManager.loadItemToPlayer(it.toConsumablesItem())
            }
        }
    }

    fun updateConsumables(id: Int, value: Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateValueConsumablesTuples(id, value)
        }
    }


    private fun delConsumablesItem(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteConsumables(id)
        }
    }

    fun tvClick(){
        if (viewVisible){
            viewVisible = false
            r.postValue(false)
        }
        else{
            viewVisible = true
            r.postValue(true)
        }
    }

    fun addItem(item: ConsumablesDbEntity) {
        val id = addConsumables(item).toInt()
        consumablesItemManager.addItem(ConsumablesItem(id, item.idPlayer, item.name, item.value, false))

    }

    fun deleteItem(id: Int) {
        consumablesItemManager.deleteItem(id)
        delConsumablesItem(id)
    }

    fun updateItem(id: Int, count : Int){
        val item = consumablesItemManager.getItemToId(id)

        if (count == -1){
            if (item.value!=0){
                val upItem = ConsumablesItem(item.id, item.idPlayer, item.name, item.value + count, false)
                consumablesItemManager.updateItem(upItem, id)
            }
        }
        else{
            val upItem = ConsumablesItem(item.id, item.idPlayer, item.name, item.value + count, false)
            consumablesItemManager.updateItem(upItem, id)
        }
    }

    fun checkLinkToIdConsum(id: Int) : Boolean{
        return equipmentManager.checkLinkToIdConsum(id)
    }

    init {
        val daoConsumablesDb = DataBase.getDb(application).getConsumablesDao()
        repository = ConsumablesRepository(daoConsumablesDb)
    }

    companion object{
        private var instance : ConsumablesViewModel? = null

        @JvmStatic
        fun getInstance(application: Application) : ConsumablesViewModel{
            return instance ?: synchronized(this){
                val newInstance = ConsumablesViewModel(application)
                instance = newInstance
                newInstance
            }
        }
    }


}