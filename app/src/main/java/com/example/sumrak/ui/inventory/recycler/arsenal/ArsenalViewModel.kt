package com.example.sumrak.ui.inventory.recycler.arsenal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sumrak.Player
import com.example.sumrak.data.DataBase
import com.example.sumrak.data.inventory.arsenal.ArsenalDbEntity
import com.example.sumrak.data.inventory.arsenal.ArsenalRepository
import com.example.sumrak.data.playerdb.PlayerRepository
import com.example.sumrak.ui.inventory.recycler.arsenal.item.ArsenalItem
import com.example.sumrak.ui.inventory.recycler.arsenal.item.ArsenalItemManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ArsenalViewModel(application: Application) : AndroidViewModel(application) {
    private val repositoryPlayer: PlayerRepository
    private val repository : ArsenalRepository
    private val arsenalItemManager = ArsenalItemManager.getInstance()

    val modeSettings = MutableLiveData<Int>()
    val arsenalItem = MutableLiveData<ArsenalItem>()
    var activeClip = MutableLiveData<Int>()
    private var activeIdSettings = 0
    private var activeIdRepair = 0



    private fun addArsenalDb(arsenalDbEntity: ArsenalDbEntity): Int{
        return runBlocking {
            val result = CoroutineScope(Dispatchers.IO).async {
                repository.addArsenalItem(arsenalDbEntity)
            }
            result.await().toInt()
        }
    }

    private fun updateArsenalDb(arsenalDbEntity: ArsenalDbEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateArsenalItem(arsenalDbEntity)
        }
    }

    private fun updateArsenalPatronsDb(id: Int, clip1: Int, clip2: Int, clip3: Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePatronsTuples(id, clip1, clip2, clip3)
        }
    }
    private fun deleteArsenalDb(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteArsenalToId(id)
        }
    }

    fun getArsenalToIdPlayer(id: Int){
        arsenalItemManager.clearList()
        arsenalItemManager.addItem(ArsenalItem(0, id, 1, "Без оружия", 1, 5, 0, 0, 1, 0 ,0, 0, 0, 0, 0, 4,true, false, ""))
        viewModelScope.launch(Dispatchers.IO) {
            repository.getArsenalToIdPlayer(id).forEach {
                arsenalItemManager.addItem(it.toArsenalItem())
            }
        }
    }

    fun addArsenalItem(arsenalItem: ArsenalItem){
        val id = addArsenalDb(arsenalItem.toArsenalDbEntity())
        arsenalItem.id = id
        arsenalItemManager.addItem(arsenalItem)
    }

    fun updateArsenalItem(arsenalItem: ArsenalItem){
        arsenalItemManager.updateItem(arsenalItem, arsenalItem.id)
        updateArsenalDb(arsenalItem.toArsenalDbEntity())
    }

    fun setActiveArsenalToPlayer(position : Int){
        val idArsenal = arsenalItemManager.getItem(position).id
        val idPlayer = Player.getInstance().getIdActivePlayer()
        viewModelScope.launch(Dispatchers.IO) {
            repositoryPlayer.updateArsenalIdPlayer(idPlayer, idArsenal)
        }
    }

    fun deleteArsenalItem(id: Int){
        deleteArsenalDb(id)
        arsenalItemManager.deleteItem(id)
    }
    fun setMode(mode: Int, id : Int){
        when(mode){
            0 -> {
                activeIdRepair = 0
                activeIdSettings = 0
                modeSettings.postValue(mode)
            }
            1 -> {arsenalItem.postValue(ArsenalItem(0,0,0,"",1, 5,0,0,0,0,0,
                0, 0, 0, 0, 4, false, false, ""))
                activeIdSettings = 0
                activeIdRepair = 0
                modeSettings.postValue(mode)
            }
            2 -> {
                activeIdRepair = 0
                if (id == activeIdSettings){
                    modeSettings.postValue(0)
                    activeIdSettings = 0
                }
                else{
                    activeIdSettings = id
                    val item = arsenalItemManager.getItemToId(id)
                    arsenalItem.postValue(item)
                    modeSettings.postValue(mode)
                }
            }
            3 -> {
                activeIdSettings = 0
                if (id == activeIdRepair){
                    modeSettings.postValue(0)
                    activeIdRepair = 0
                }
                else{
                    activeIdRepair = id
                    val item = arsenalItemManager.getItemToId(id)
                    arsenalItem.postValue(item)
                    modeSettings.postValue(mode)
                    activeClip.postValue(1)
                }
            }
        }

    }

    fun ravageClipsToArsenal(id: Int, clip1: Int, clip2: Int, clip3: Int){
        arsenalItemManager.updatePatronsItemToId(id, clip1, clip2, clip3)
        updateArsenalPatronsDb(id, clip1, clip2, clip3)
        arsenalItem.postValue(arsenalItemManager.getItemToId(id))
    }
    fun setActiveClip(clip : Int){
        activeClip.postValue(clip)
    }

    fun getPatronsArsenal(id: Int): String {
        val item = arsenalItemManager.getItemToId(id)
        return "${item.clip1}/${item.maxPatrons} (${item.clip1 + item.clip2 + item.clip3})"
    }




    init {
        val daoArsenalDb = DataBase.getDb(application).getArsenalDao()
        repository = ArsenalRepository(daoArsenalDb)

        val daoPlayerDb = DataBase.getDb(application).getPlayerDao()
        repositoryPlayer = PlayerRepository(daoPlayerDb)
    }
    companion object{
        private var instance : ArsenalViewModel? = null

        @JvmStatic
        fun getInstance(application: Application) : ArsenalViewModel{
            return instance ?: synchronized(this){
                val newInstance = ArsenalViewModel(application)
                instance = newInstance
                newInstance
            }
        }
    }
}