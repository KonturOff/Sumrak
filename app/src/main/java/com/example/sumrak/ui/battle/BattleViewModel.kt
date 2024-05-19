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
import com.example.sumrak.data.battle.BattleItemEntity
import com.example.sumrak.data.battle.BattleRepository
import com.example.sumrak.sound.Sound
import com.example.sumrak.ui.battle.recycler.initiative.InitiativeItem
import com.example.sumrak.ui.inventory.recycler.armor.item.ArmorItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class BattleViewModel(application: Application) : AndroidViewModel(application) {
    private val repository : BattleRepository
    private val hpReposiroty : PlayerRepository
    private val armorRepository : ArmorRepository
    private val historyRollManager = HistoryRollManager.getInstance()
    private val battleManager = BattleManager.getInstance()
    private val bonusEndurance: Int
    private val bonusPower : Int

    val infoVisible = MutableLiveData<Boolean>()
    val damageVisible = MutableLiveData<Boolean>()
    val initiativeViewVisible = MutableLiveData<Boolean>()
    val equipmentBVisible = MutableLiveData<Boolean>()
    val atackVisible = MutableLiveData<Boolean>()


    val playerV = MutableLiveData<PlayerVariable>()
    private var playerVariable : PlayerVariable? = null
    val armorV = MutableLiveData<ArmorItem>()
    private var armor : ArmorItem? = null
    val initiativeV = MutableLiveData<InitiativeItem>()
    private var initiative : InitiativeItem? = null
    val initiativeVisible = MutableLiveData<Boolean>()
    private var hits : Int? = null
    val hitsV = MutableLiveData<Int>()
    val hitsVisible = MutableLiveData<Boolean>()


    fun getBattleItemToPlayer(idPlayer: Int){
        battleManager.clearList()
        viewModelScope.launch(Dispatchers.IO) {
            repository.getBattleItemToIdPlayer(idPlayer).forEach {
                battleManager.addItem(it.toBattleItem())
            }
        }
    }

    fun updateItemsToBattleList(){
        battleManager.updatePosition()
        viewModelScope.launch(Dispatchers.IO) {
            val items = battleManager.getAllItems().toList()
            for (i in items.indices){
                repository.updateBattleItemPosition(items[i].toBattleItemEntity())
            }
        }
    }
    fun updateVisibleView(name: String){
        battleManager.toggleItemState(name)
        val item = battleManager.getItemToName(name)
        getVisibleView(name)
        updateVisibleViewToBd(item.id, item.isExpanded)
    }

    private fun updateVisibleViewToBd(id : Int, isExpanded:Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateVisibleToId(id, isExpanded)
        }
    }
    fun getVisibleView(name : String) {
        val visible = battleManager.getVisibleToName(name)
        when (name) {
            "Информация" -> infoVisible.postValue(visible)
            "Получение Урона" -> damageVisible.postValue(visible)
            "Инициатива" -> initiativeViewVisible.postValue(visible)
            "Снаряжение" -> equipmentBVisible.postValue(visible)
            "Атака" -> atackVisible.postValue(visible)
        }
    }

    fun getNumberHitsPlayer(id: Int){
        if (historyRollManager.getLastRollHitToIdPlayer(id)!= null){
            val hit = historyRollManager.getLastRollHitToIdPlayer(id)!!.value
            hits = hit
            hitsV.postValue(hit)
            hitsVisible.postValue(true)
        }
        else{
            hitsVisible.postValue(false)
        }
    }

    fun updateValueHitToidPlayer(step: Int){
        if (historyRollManager.getLastRollHitToIdPlayer(Player.getInstance().getIdActivePlayer())!= null){
            val idPlayer = Player.getInstance().getIdActivePlayer()
            hits = hits!! + step
            hitsV.postValue(hits!!)
            historyRollManager.updateValueHitToIdPlayer(idPlayer, hits!!)
        }
    }

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
        if (historyRollManager.getLastRollIniToIdPlayer(Player.getInstance().getIdActivePlayer()).mode != "не найдено"){
            initiative?.let {
                it.step = it.step + step
                initiativeV.postValue(it)
                val pos = historyRollManager.getPositionLastIniToIdPlayer(it.idPlayer)
                historyRollManager.updateStepIniToPos(pos, it.step)
            }
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
        val reaction = Player.getInstance().getActivePlayer().bonusReaction
        var step = when(armor?.classArmor){
            "Легкая" -> 2 - reaction
            "Средняя" -> 3 - reaction
            "Тяжелая" -> 4 - reaction
            else -> 2 - reaction
        }
        if (step<1){
            step = 1
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
                        if (playerVariable?.hp==0){
                            Sound.getInstance().playSound0Hp(playerVariable?.id!!)
                        }
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
        val daoBattleItem = DataBase.getDb(application).getBattleDao()
        repository = BattleRepository(daoBattleItem)
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