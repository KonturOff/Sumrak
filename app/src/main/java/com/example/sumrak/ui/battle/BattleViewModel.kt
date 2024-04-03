package com.example.sumrak.ui.battle

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sumrak.data.DataBase
import com.example.sumrak.data.inventory.armor.ArmorRepository
import com.example.sumrak.data.playerdb.PlayerRepository
import com.example.sumrak.lists.HistoryRollManager
import com.example.sumrak.lists.PlayerVariable
import com.example.sumrak.Player
import com.example.sumrak.ui.battle.recycler.initiative.InitiativeItem
import com.example.sumrak.ui.inventory.recycler.armor.item.ArmorItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class BattleViewModel(application: Application) : AndroidViewModel(application) {
    private val hpReposiroty : PlayerRepository
    private val armorRepository : ArmorRepository
    private val historyRollManager = HistoryRollManager.getInstance()
    private val bonusEndurance: Int
    private val bonusPower : Int



    val playerV = MutableLiveData<PlayerVariable>()
    private var playerVariable : PlayerVariable? = null
    val armorV = MutableLiveData<ArmorItem>()
    private var armor : ArmorItem? = null
    val initiativeV = MutableLiveData<InitiativeItem>()
    private var initiative : InitiativeItem? = null
    val initiativeVisible = MutableLiveData<Boolean>()

    fun getInitiativePlayer(id: Int) {
        initiative = historyRollManager.getLastRollIniToIdPlayer(id).toInitiativeItem()
        if (initiative?.resultRoll != 0){
            initiative?.let {
                initiativeV.postValue(it)
                initiativeVisible.postValue(true)
            }
        }
        else{
            initiativeVisible.postValue(false)
        }
    }

    fun updateStepInitiative(step : Int) {
        initiative?.let {
            it.step = it.step + step
            initiativeV.postValue(it)
            val pos = historyRollManager.getPositionLastIniToIdPlayer(it.idPlayer)
            historyRollManager.updateStepIniToPos(pos, it.step)
        }
    }

    private fun getPlayerVariableToId(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            playerVariable = hpReposiroty.getPlayerVariableToId(id).toPlayerVariable()
            playerVariable?.let { playerV.postValue(it) }
        }
    }

    private fun getArmorToId(id: Int) {
        if (id != 0){
            viewModelScope.launch(Dispatchers.IO) {
                armor = armorRepository.getArmorToId(id).toArmorItem()
                armor?.let { armorV.postValue(it) }
            }
        }
        else{
            armor = ArmorItem(0,0,"","Без Брони",0,0,0,"")
            armor?.let { armorV.postValue(it) }
        }
    }

    fun updateDodgeParrying(step: Int) {
        var dodge = playerVariable?.dodge ?: 0
        var parrying = playerVariable?.parrying ?: 0
        if (step == 0) {
            dodge = Player.getInstance().getActivePlayer().dexterity
            parrying = Player.getInstance().getActivePlayer().bb
        }
        else {
            dodge += step
            parrying += step
        }
        if (dodge < 0){
            dodge = 0
        }
        if (parrying < 0){
            parrying = 0
        }

        viewModelScope.launch(Dispatchers.IO) {
            hpReposiroty.updateDodgeParryingPlayer(Player.getInstance().getIdActivePlayer(), dodge, parrying)
        }
        playerVariable?.let {
            it.dodge = dodge
            it.parrying = parrying
            playerV.postValue(it)
        }
        Player.getInstance().playerEntity = playerVariable
    }

    fun degradeDodge(){
        val step = when(armor?.classArmor){
            "Легкая" -> 1
            "Средняя" -> 2
            "Тяжелая" -> 3
            else -> 1
        }
        updateDodgeParrying(-step)
    }


    fun damageToPlayer(editDamage : Int, penetration : Int){
        var damage = editDamage
        val penetrations = penetration
        armor?.let {
            val armorZ = it.copy()
            var endurance = bonusEndurance
            if (penetrations >= armorZ.params){
                armorZ.params = 0
            }
            else armorZ.params -= penetrations

            while (damage!=0){
                if (armorZ.params > 0){
                    armorZ.params--
                }
                else if (armorZ.endurance > 0){
                    armorZ.endurance--
                }
                else{
                    if (endurance > 0){
                        endurance--
                    }
                    else{
                        playerVariable?.let { playerVariable -> playerVariable.hp-- }
                    }
                }
                damage--
            }
            it.endurance = armorZ.endurance
            armorV.postValue(it)
            playerVariable?.let { playerVariable -> playerV.postValue(playerVariable) }

            updateArmorEndurance(it.id, it.endurance)
            playerVariable?.let { playerVariable -> updateHpPlayer(playerVariable.hp) }
        }
    }

    private fun updateHpPlayer(hp : Int){
        val id = Player.getInstance().getIdActivePlayer()
        viewModelScope.launch(Dispatchers.IO) {
            hpReposiroty.updateHpPlayer(id, hp)
        }
    }
    private fun updateArmorEndurance(id: Int, endurance: Int){
        viewModelScope.launch(Dispatchers.IO) {
            armorRepository.updateEnduranceArmor(id, endurance)
        }
    }

    init {
        val daoPlayerDb = DataBase.getDb(application).getPlayerDao()
        hpReposiroty = PlayerRepository(daoPlayerDb)
        val daoArmorDb = DataBase.getDb(application).getArmorDao()
        armorRepository = ArmorRepository(daoArmorDb)

        getPlayerVariableToId(Player.getInstance().getIdActivePlayer())
        getArmorToId(Player.getInstance().getActivePlayer().activeArmor)
        bonusEndurance = Player.getInstance().getBonusEndurance()
        bonusPower = Player.getInstance().getBonusPower()
    }

    companion object{
        private var instance: BattleViewModel? = null

        @JvmStatic
        fun getInstance(application: Application): BattleViewModel{
            return instance ?: synchronized(this){
                val newInstance = BattleViewModel(application)
                instance = newInstance
                newInstance
            }
        }
    }
}