package com.example.sumrak.data.playerdb

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sumrak.data.DataBase
import com.example.sumrak.data.settings.SettingsDbEntity
import com.example.sumrak.lists.DataPlayer
import com.example.sumrak.lists.PlayerVariable
import com.example.sumrak.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class PlayerViewModel(application: Application): AndroidViewModel(application) {

    val readSettings : Flow<List<SettingsDbEntity>>
    private val readAllPlayer : Flow<List<PlayerIsEffectsDb>>
    private val repository : PlayerRepository
    private val activeId = MutableLiveData<Int?>()
    val playerV = MutableLiveData<PlayerVariable>()
    private var playerVariable: PlayerVariable? = null

    fun loadData(){
        viewModelScope.launch(Dispatchers.IO) {
            readAllPlayer.collect {list->
                Player.getInstance().clearPlayerList()
                list.forEach {
                    val player = it.toDataPlayer()
                    Player.getInstance().addPlayer(player)
                }
            }
        }
    }

    fun loadSettings(){
        viewModelScope.launch(Dispatchers.IO) {
            readSettings.collect {list->
                list.forEach {
                    if (it.name_settings == "active_id"){
                        activeId.postValue(it.value)
                    }
                }
            }
        }
    }

    //Изменить активного персонажа
    fun changeActivePlayer(step :Int) {
        // Вынес в переменные, чтобы при компиляции программмы было меньше созданий объектов класса Player
        val activePlayer = Player.getInstance()
        val currentPlayerCount = activePlayer.getPlayerCount()
        if (step > 0) {
            if (activePlayer.activePosPlayer < currentPlayerCount - 1) {
                activePlayer.activePosPlayer++
            }
            else {
                activePlayer.activePosPlayer = 0
            }
        }
        else{
            if (activePlayer.activePosPlayer > 0) {
                activePlayer.activePosPlayer--
            }
            else {
                activePlayer.activePosPlayer = currentPlayerCount - 1
            }
        }
        updateActivePlayer()
    }


    private fun updateActivePlayer() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateActivePlayer(SettingsDbEntity(1,
                "active_id",
                Player.getInstance().getIdActivePlayer()))
        }
    }

    fun setIdActivePlayer(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateActivePlayer(
                SettingsDbEntity(1,"active_id", id)
            )
        }
    }

    fun getCountSettings() : Int{
        return runBlocking {
            val result = CoroutineScope(Dispatchers.IO).async {
                repository.getCountSettings()
            }
            result.await()
        }
    }

    fun addPlayer(playerDbEntity: PlayerDbEntity) : Long {
        return runBlocking {
            if (getCountSettings()==0){
                repository.addSettings(SettingsDbEntity(1,"active_id", 1))
                }
            val result = CoroutineScope(Dispatchers.IO).async {
                repository.addPlayer(playerDbEntity)
            }
            result.await()
        }
    }

    fun addPlayerVariable(playerVariableEntity: PlayerVariableEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addPlayerVariable(playerVariableEntity)
        }
    }

    fun updatePlayer(playerDbEntity: PlayerDbEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePlayer(playerDbEntity)
        }
    }

    fun getPlayerToId(id: Int) : DataPlayer{
        return runBlocking {
            val item = CoroutineScope(Dispatchers.IO).async {
                repository.getPlayerToId(id).toDataPlayer()
            }
            item.await()
        }
    }

    fun getPlayerVariableToId(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            playerV.postValue(repository.getPlayerVariableToId(id).toPlayerVariable())
            playerVariable = repository.getPlayerVariableToId(id).toPlayerVariable()
            Player.getInstance().playerEntity = playerVariable
        }
    }

    fun updateHpPlayer(id : Int, hp : Int) {
        playerVariable?.let {
            it.hp = it.hp + hp
            viewModelScope.launch(Dispatchers.IO) {
                repository.updateHpPlayer(id, it.hp)
            }
            playerV.postValue(it)
        }
    }

    fun updateFatePlayer(id: Int) {
        playerVariable?.let {
            --it.fate
            viewModelScope.launch(Dispatchers.IO) {
                repository.updateFatePlayer(id, it.fate)
            }
            playerV.postValue(it)
        }
    }

    fun updateLightKarm(id: Int, light : Int) {
        playerVariable?.let {
            it.lightKarm = it.lightKarm + light
            viewModelScope.launch(Dispatchers.IO) {
                repository.updateLightKarm(id, it.lightKarm)
            }
            playerV.postValue(it)
        }
    }

    fun updateDarkKarm(id: Int, dark : Int){
        playerVariable?.let {
            it.darkKarm = it.darkKarm + dark
            viewModelScope.launch(Dispatchers.IO) {
                repository.updateDarkKarm(id, it.darkKarm)
            }
            playerV.postValue(it)
        }
    }

    fun deletePlayer(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deletePlayer(id)
            changeActivePlayer(-1)
        }
    }

    init {
        val daoPlayerDb = DataBase.getDb(application).getPlayerDao()
        repository = PlayerRepository(daoPlayerDb)
        readAllPlayer = repository.readAllPlayer
        readSettings = repository.readSettings
    }

    companion object {
        private var instance: PlayerViewModel? = null

        @JvmStatic
        fun getInstance(application: Application): PlayerViewModel {
            return instance ?: synchronized(this) {
                val newInstance = PlayerViewModel(application)
                instance = newInstance
                newInstance
            }
        }
    }


}