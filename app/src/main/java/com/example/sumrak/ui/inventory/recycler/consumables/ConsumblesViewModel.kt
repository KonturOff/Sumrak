package com.example.sumrak.ui.inventory.recycler.consumables


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sumrak.Data.DataBase
import com.example.sumrak.Data.inventory.consumables.ConsumablesDbEntity
import com.example.sumrak.Data.inventory.consumables.ConsumablesRepository
import com.example.sumrak.ui.inventory.recycler.consumables.item.ConsumablesItemManager
import com.example.sumrak.ui.inventory.recycler.consumables.item.ConsumblesItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ConsumblesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ConsumablesRepository
    private val consumablesItemManager = ConsumablesItemManager.getInstance()


    var viewVisible = true
    var r = MutableLiveData<Boolean>()

    fun addConsumables(consumablesDbEntity: ConsumablesDbEntity) : Long{
        return runBlocking {
            val result = CoroutineScope(Dispatchers.IO).async {
                repository.addConsumables(consumablesDbEntity)
            }
            result.await()
        }
    }

    fun getCosumablesToPlayerId(id : Int){
        viewModelScope.launch(Dispatchers.IO){
            consumablesItemManager.clearConsumablesList()
            repository.getCosumablesToPlayerId(id).forEach {
                consumablesItemManager.loadItemToPlayer(it.toConsumablesItem())
            }
        }
    }

    fun updateConsumbles(id: Int, value: Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateValueConsumablesTaples(id, value)
        }
    }


    fun delConsumablesItem(id: Int){
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
        consumablesItemManager.addItem(ConsumblesItem(id, item.idPlayer, item.name, item.value))

    }

    fun deleteItem(id: Int) {
        consumablesItemManager.deleteItem(id)
        delConsumablesItem(id)
    }

    fun updateItem(id: Int, count : Int){
        val item = consumablesItemManager.getItemToId(id)

        if (count == -1){
            if (item.value!=0){
                val upItem = ConsumblesItem(item.id, item.idPlayer, item.name, item.value + count)
                consumablesItemManager.updateItem(upItem, id)
            }
        }
        else{
            val upItem = ConsumblesItem(item.id, item.idPlayer, item.name, item.value + count)
            consumablesItemManager.updateItem(upItem, id)
        }



    }

    init {
        val daoConsumablesDb = DataBase.getDb(application).getConsumamblesDao()
        repository = ConsumablesRepository(daoConsumablesDb)
    }

    companion object{
        private var instance : ConsumblesViewModel? = null

        @JvmStatic
        fun getInstance(application: Application) : ConsumblesViewModel{
            return instance ?: synchronized(this){
                val newInstance = ConsumblesViewModel(application)
                instance = newInstance
                newInstance
            }
        }
    }


}