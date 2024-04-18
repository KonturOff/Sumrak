package com.example.sumrak.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sumrak.Player
import com.example.sumrak.data.DataBase
import com.example.sumrak.data.playerdb.PlayerRepository
import com.example.sumrak.lists.DataPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application){
    private val repository: PlayerRepository

    val playerLv = MutableLiveData<DataPlayer>()
    val influenceLv = MutableLiveData<Int>()
    val xpLv = MutableLiveData<Int>()


    fun getProfileTextViewToPlayer(id : Int){
        val player = Player.getInstance().getPlayerToId(id)
        playerLv.postValue(player)
    }

    fun updateInfluencePlayer(id: Int, influence: Int){
        influenceLv.postValue(influence)
        viewModelScope.launch(Dispatchers.IO){
            repository.updatePlayerInfluence(id, influence)
        }
    }

    fun updateXpPlayer(id: Int, xp: Int){
        xpLv.postValue(xp)
        viewModelScope.launch(Dispatchers.IO){
            repository.updateXpPlayer(id, xp)
        }
    }

    fun updateSoundPlayer(id: Int, sound : String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateSoundPlayer(id, sound)
        }
    }

    fun saveDataProfilePlayer(id: Int, classPers: String, rank: String, bitchPlace:String, markFate:String, skills: String, profile: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateProfilePlayer(id, classPers, rank, bitchPlace, markFate, skills, profile)
        }
    }

    init {
        val profileDaoDb = DataBase.getDb(application).getPlayerDao()
        repository = PlayerRepository(profileDaoDb)
    }

    companion object{
        private var instance : ProfileViewModel? = null

        @JvmStatic
        fun getInstance(application: Application) : ProfileViewModel{
            return instance ?: synchronized(this){
                val newInstance = ProfileViewModel(application)
                instance = newInstance
                newInstance
            }
        }
    }
}