package com.example.sumrak.ui.inventory

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sumrak.data.DataBase
import com.example.sumrak.data.inventory.inventoryItem.InventoryItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class InventoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: InventoryItemRepository
    private val inventoryManager = InventoryManager.getInstance()

    val effectsVisibility = MutableLiveData<Boolean>()
    val consumablesVisibility = MutableLiveData<Boolean>()
    val armorVisibility = MutableLiveData<Boolean>()
    val equipmentVisibility = MutableLiveData<Boolean>()
    val arsenalVisibility = MutableLiveData<Boolean>()

    fun addInventoryView(inventoryItem: InventoryItem){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addInventoryItem(inventoryItem.toInventoryItemEntity())
        }
    }

    fun getInventoryItemToPlayer(idPlayer: Int){
        inventoryManager.clearList()
        viewModelScope.launch(Dispatchers.IO) {
            repository.getInventoryItemToPlayer(idPlayer).forEach {
                inventoryManager.addItem(it.toInventoryItem())
            }
        }
    }

    fun updateItemsToInventoryList(){
        inventoryManager.updatePosition()
        viewModelScope.launch(Dispatchers.IO) {
            val items = inventoryManager.getAllItems().toList()
            for (i in items.indices){
                repository.updateInventoryItemPos(items[i].toInventoryItemEntity())
            }
        }
    }

    fun updateVisibleView(name: String){
        inventoryManager.toggleItemState(name)
        val item = inventoryManager.getItemToName(name)
        getVisibleView(name)
        updateVisibleViewToBd(item.id, item.isExpanded)
    }

    private fun updateVisibleViewToBd(id : Int, isExpanded:Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateVisibleToId(id, isExpanded)
        }
    }

    fun getVisibleView(name : String){
        val visible = inventoryManager.getVisibleToName(name)
        when(name){
            "Эффекты" -> effectsVisibility.postValue(visible)
            "Расходники" -> consumablesVisibility.postValue(visible)
            "Броня" -> armorVisibility.postValue(visible)
            "Снаряжение" -> equipmentVisibility.postValue(visible)
            "Арсенал" -> arsenalVisibility.postValue(visible)

        }
    }


    init {
        val daoInventoryDb = DataBase.getDb(application).getInventoryItemDao()
        repository = InventoryItemRepository(daoInventoryDb)
    }

    companion object{
        private var instance : InventoryViewModel? = null

        @JvmStatic
        fun getInstance(application: Application) : InventoryViewModel{
            return instance ?: synchronized(this){
                val newInstance = InventoryViewModel(application)
                instance = newInstance
                newInstance
            }
        }
    }
}