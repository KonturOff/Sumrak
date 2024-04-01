package com.example.sumrak.Data.playerdb

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sumrak.Data.DataBase
import com.example.sumrak.Data.settings.SettingsDbEntity
import com.example.sumrak.Lists.DataPlayer
import com.example.sumrak.Lists.PlayerVariable
import com.example.sumrak.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class PlayerViewModel(application: Application): AndroidViewModel(application) {

    val readSettings : Flow<List<SettingsDbEntity>>
    val readAllPlayer : Flow<List<PlayerIsEffectsDb>>
    private val reposiroty : PlayerReposiroty
    val activeId = MutableLiveData<Int?>()
    val playerV = MutableLiveData<PlayerVariable>()
    lateinit var playerVariable: PlayerVariable




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
                    if (it.name_settings.equals("active_id")){
                        activeId.postValue(it.value)
                    }
                }
            }
        }
    }

    //Изменить активного персонажа
    fun changeActivePlayer(step :Int){
        if (step>0){
            if (Player.getInstance().activPosPlayer < Player.getInstance().getPlayerCount()-1){
                Player.getInstance().activPosPlayer++
            }
            else {
                Player.getInstance().activPosPlayer = 0
            }
        }
        else{
            if (Player.getInstance().activPosPlayer > 0){
                Player.getInstance().activPosPlayer--
            }
            else {
                Player.getInstance().activPosPlayer = Player.getInstance().getPlayerCount()-1
            }
        }
        updateActivePlayer()
    }


    fun updateActivePlayer() {
        viewModelScope.launch(Dispatchers.IO) {
            reposiroty.updateActivePlayer(SettingsDbEntity(1,
                "active_id",
                Player.getInstance().getIdActivePlayer()))
        }
    }

    fun setIdActivePlayer(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            reposiroty.updateActivePlayer(
                SettingsDbEntity(1,"active_id", id)
            )
        }
    }

    fun getCountSettings() : Int{
        return runBlocking {
            val result = CoroutineScope(Dispatchers.IO).async {
                reposiroty.getCountSettings()
            }
            result.await()
        }
    }



    fun addPlayer(playerDbEntity: PlayerDbEntity) : Long {
        return runBlocking {
            if (getCountSettings()==0){
                reposiroty.addSettings(SettingsDbEntity(1,"active_id", 1))
                }
            val result = CoroutineScope(Dispatchers.IO).async {
                reposiroty.addPlayer(playerDbEntity)
            }
            result.await()
        }
    }

    fun addPlayerVariable(playerVariableEntity: PlayerVariableEntity){
        viewModelScope.launch(Dispatchers.IO) {
            reposiroty.addPlayerVariable(playerVariableEntity)
        }
    }

    fun updatePlayer(playerDbEntity: PlayerDbEntity){
        viewModelScope.launch(Dispatchers.IO) {
            reposiroty.updatePlayer(playerDbEntity)
        }
    }

    fun getPlayerToId(id: Int) : DataPlayer{
        return runBlocking {
            val item = CoroutineScope(Dispatchers.IO).async {
                reposiroty.getPlayerToId(id).toDataPlayer()
            }
            item.await()
        }

    }

    fun getPlayerVariableToId(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            playerV.postValue(reposiroty.getPlayerVariableToId(id).toPlayerVariable())
            playerVariable = reposiroty.getPlayerVariableToId(id).toPlayerVariable()
            Player.getInstance().playerEntity = playerVariable
        }
    }

    fun updateHpPlayer(id : Int, hp : Int){
        playerVariable.hp = playerVariable.hp + hp
        viewModelScope.launch(Dispatchers.IO) {
            reposiroty.updateHpPlayer(id, playerVariable.hp)
        }
        playerV.postValue(playerVariable)
    }

    fun updateFatePlayer(id: Int){
        -- playerVariable.fate
        viewModelScope.launch(Dispatchers.IO) {
            reposiroty.updateFatePlayer(id, playerVariable.fate)
        }
        playerV.postValue(playerVariable)
    }

    fun updateLightKarm(id: Int, light : Int){
        playerVariable.light_karm = playerVariable.light_karm + light
        viewModelScope.launch(Dispatchers.IO) {
            reposiroty.updateLightKarm(id, playerVariable.light_karm)
        }
        playerV.postValue(playerVariable)
    }

    fun updateDarkKarm(id: Int, dark : Int){
        playerVariable.dark_karm = playerVariable.dark_karm + dark
        viewModelScope.launch(Dispatchers.IO) {
            reposiroty.updateDarkKarm(id, playerVariable.dark_karm)
        }
        playerV.postValue(playerVariable)
    }

    fun deletePlayer(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            reposiroty.deletePlayer(id)
            changeActivePlayer(-1)
        }
    }

    init {
        val daoPlayerDb = DataBase.getDb(application).getPlayerDao()
        reposiroty = PlayerReposiroty(daoPlayerDb)
        readAllPlayer = reposiroty.readAllPlayer
        readSettings = reposiroty.readSettings
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