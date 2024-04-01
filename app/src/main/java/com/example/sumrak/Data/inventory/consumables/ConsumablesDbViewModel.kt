package com.example.sumrak.Data.inventory.consumables

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sumrak.Data.DataBase
import com.example.sumrak.ui.inventory.recycler.consumables.item.ConsumablesItemManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class ConsumablesDbViewModel(application: Application): AndroidViewModel(application) {

    private val repository: ConsumablesRepository
    private val manager = ConsumablesItemManager.getInstance()

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
            manager.clearConsumablesList()
            repository.getCosumablesToPlayerId(id).forEach {
                manager.addItem(it.toConsumablesItem())
            }
        }
    }

    fun delConsumablesItem(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteConsumables(id)
        }
    }





    init {
        val daoConsumablesDb = DataBase.getDb(application).getConsumamblesDao()
        repository = ConsumablesRepository(daoConsumablesDb)
    }

    companion object{
        private var instance : ConsumablesDbViewModel? = null

        @JvmStatic
        fun getInstanse(application: Application) : ConsumablesDbViewModel {
            return instance ?: synchronized(this) {
                val newInstance = ConsumablesDbViewModel(application)
                instance = newInstance
                newInstance
            }
        }

    }
}