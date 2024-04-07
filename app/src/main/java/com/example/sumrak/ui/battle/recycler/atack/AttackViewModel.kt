package com.example.sumrak.ui.battle.recycler.atack

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sumrak.Player
import com.example.sumrak.data.DataBase
import com.example.sumrak.data.inventory.arsenal.ArsenalRepository
import com.example.sumrak.lists.HistoryRollManager
import com.example.sumrak.ui.inventory.recycler.arsenal.item.ArsenalItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AttackViewModel(application: Application) : AndroidViewModel(application) {
    private val historyRollManager = HistoryRollManager.getInstance()
    private val repository : ArsenalRepository
    val activeWeaponLiveData : LiveData<ArsenalItem> = MutableLiveData()


    private fun updateArsenalPatronsDb(id: Int, clip1: Int, clip2: Int, clip3: Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePatronsTuples(id, clip1, clip2, clip3)
        }
    }

    fun refreshPatrons(id: Int, clip1: Int, clip2: Int, clip3: Int){
        updateArsenalPatronsDb(id, clip1, clip2, clip3)
    }


    fun hitArsenal(change: Int){
        if (activeWeaponLiveData.value?.classArsenal != 1){
            val id = activeWeaponLiveData.value?.id!!
            val clip1 = activeWeaponLiveData.value?.clip1!! - change
            val clip2 = activeWeaponLiveData.value?.clip2!!
            val clip3 = activeWeaponLiveData.value?.clip3!!
            updateArsenalPatronsDb(id, clip1, clip2, clip3)
        }



    }


    init {
        val daoArsenalDb = DataBase.getDb(application).getArsenalDao()
        repository = ArsenalRepository(daoArsenalDb)

        viewModelScope.launch {
            repository.readActiveWeapon
                .map { weapon ->
                    if (weapon == null) {
                        return@map ArsenalItem(
                            id = 0,
                            idPlayer = Player.getInstance().getIdActivePlayer(),
                            classArsenal = 1,
                            name = "Без Оружия",
                            damageX = 1,
                            damageY = 5,
                            damageZ = 0,
                            penetration = 0,
                            step = 1,
                            change = 0,
                            maxPatrons = 0,
                            clip1 = 0,
                            clip2 = 0,
                            clip3 = 0,
                            distanse = 0,
                            valueTest = 4,
                            bonusPower = true,
                            paired = false,
                            features = ""
                        )
                    } else {
                        return@map weapon.toArsenalItem()
                    }
                }
                .collect { weapon ->
                    (activeWeaponLiveData as MutableLiveData).postValue(weapon)
                }
        }
    }
    companion object{
        private var instance : AttackViewModel? = null

        @JvmStatic
        fun getInstance(application: Application) : AttackViewModel{
            return instance ?: synchronized(this){
                val newInstance = AttackViewModel(application)
                instance = newInstance
                newInstance
            }
        }
    }
}