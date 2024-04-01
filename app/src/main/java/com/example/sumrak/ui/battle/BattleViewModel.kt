package com.example.sumrak.ui.battle

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sumrak.Data.DataBase
import com.example.sumrak.Data.inventory.armor.ArmorRepository
import com.example.sumrak.Data.playerdb.PlayerReposiroty
import com.example.sumrak.Lists.HistoryRollManager
import com.example.sumrak.Lists.PlayerVariable
import com.example.sumrak.Player
import com.example.sumrak.ui.battle.recycler.initiative.InitiativeItem
import com.example.sumrak.ui.inventory.recycler.armor.item.ArmorItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class BattleViewModel(application: Application) : AndroidViewModel(application) {
    private val hpReposiroty : PlayerReposiroty
    private val armorRepository : ArmorRepository
    private val historyRollManager = HistoryRollManager.getInstance()
    private val bonusEndurance: Int
    private val bonusPower : Int



    val playerV = MutableLiveData<PlayerVariable>()
    private lateinit var playerVariable : PlayerVariable
    val armorV = MutableLiveData<ArmorItem>()
    private lateinit var armor : ArmorItem
    val initiativeV = MutableLiveData<InitiativeItem>()
    lateinit var initiative : InitiativeItem
    val initiativeVisible = MutableLiveData<Boolean>()

    fun getInitiativePlayer(id: Int){
        initiative = historyRollManager.getLastRollIniToidPlayer(id).toInitiativeItem()
        if (initiative.resultRoll!=0){
            initiativeV.postValue(initiative)
            initiativeVisible.postValue(true)
        }
        else{
            initiativeVisible.postValue(false)
        }

    }

    fun updateStepInitiative(step : Int){
        initiative.step = initiative.step + step
        initiativeV.postValue(initiative)
        val pos = historyRollManager.getPositionLastIniToIdPlayer(initiative.idPlayer)
        historyRollManager.updateStepIniToPos(pos, initiative.step)
    }

    fun getPlayerVariableToId(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            playerVariable = hpReposiroty.getPlayerVariableToId(id).toPlayerVariable()
            playerV.postValue(playerVariable)
        }
    }

    fun getArmorToId(id: Int){
        if (id!=0){
            viewModelScope.launch(Dispatchers.IO) {
                armor = armorRepository.getArmorToId(id).toArmorItem()
                armorV.postValue(armor)
            }
        }
        else{
            armor = ArmorItem(0,0,"","Без Брони",0,0,0,"")
            armorV.postValue(armor)
        }
    }

    fun updateDodgeParrying(step: Int) {
        var dodge = playerVariable.dodge
        var parrying = playerVariable.parrying
        if (step == 0){
            dodge = Player.getInstance().getActivePlayer().dexterity
            parrying = Player.getInstance().getActivePlayer().bb
        }
        else{
            dodge +=step
            parrying +=step
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
        playerVariable.dodge = dodge
        playerVariable.parrying = parrying
        playerV.postValue(playerVariable)
        Player.getInstance().playerEntity = playerVariable
    }

    fun degradeDodge(){
        val step = when(armor.classArmor){
            "Легкая" -> 1
            "Средняя" -> 2
            "Тяжелая" -> 3
            else -> 1
        }
        updateDodgeParrying(-step)
    }


    fun damageToPlayer(editDamage : Int, penetration : Int){
        var damage = editDamage
        var penetrations = penetration
        var armorZ = armor.copy()
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
                    playerVariable.hp--
                }
            }
            damage--
        }
        armor.endurance = armorZ.endurance
        armorV.postValue(armor)
        playerV.postValue(playerVariable)

        updateArmorEndurance(armor.id, armor.endurance)
        updateHpPlayer(playerVariable.hp)
    }

    private fun updateHpPlayer(hp : Int){
        val id = Player.getInstance().getIdActivePlayer()
        viewModelScope.launch(Dispatchers.IO) {
            hpReposiroty.updateHpPlayer(id, hp)
        }
    }
    fun updateArmorEndurance(id: Int, endurance: Int){
        viewModelScope.launch(Dispatchers.IO) {
            armorRepository.updateEnduranceArmor(id, endurance)
        }
    }





    init {
        val daoPlayerDb = DataBase.getDb(application).getPlayerDao()
        hpReposiroty = PlayerReposiroty(daoPlayerDb)
        val daoArmorDb = DataBase.getDb(application).getArmorDao()
        armorRepository = ArmorRepository(daoArmorDb)

        getPlayerVariableToId(Player.getInstance().getIdActivePlayer())
        getArmorToId(Player.getInstance().getActivePlayer().active_armor)
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