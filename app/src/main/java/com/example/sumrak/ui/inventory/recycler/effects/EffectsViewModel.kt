package com.example.sumrak.ui.inventory.recycler.effects

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sumrak.data.DataBase
import com.example.sumrak.data.inventory.effects.EffectsRepository
import com.example.sumrak.data.inventory.effects.TuplesEffectsCheck
import com.example.sumrak.Player
import com.example.sumrak.ui.inventory.recycler.effects.item.EffectsItem
import com.example.sumrak.ui.inventory.recycler.effects.item.EffectsItemManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class EffectsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: EffectsRepository
    private val effItemManager = EffectsItemManager.getInstance()

    private var visiblev = true
    private var visibleView = MutableLiveData<Boolean>()
    var index = -1
    var indexTvValue = MutableLiveData<Int>()
    var modeSettings = MutableLiveData<Int>()
    var effectConstructor = listOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0).toMutableList()
    var nameEff = ""

    fun tvClick(){
        if (visiblev){
            visiblev = false
            visibleView.postValue(false)
        }
        else{
            visiblev = true
            visibleView.postValue(true)
        }
    }

    fun modeSettings(visible: Boolean) {
        if (visible){
            effectConstructor = listOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0).toMutableList()
            modeSettings.postValue(1)
        }
        else{
            modeSettings.postValue(0)
        }
    }

    fun addEffect(name : String){
        val newEffect = EffectsItem(0,
            Player.getInstance().getIdActivePlayer(),
            name,
            1,
            effectConstructor[0],
            effectConstructor[1],
            effectConstructor[2],
            effectConstructor[3],
            effectConstructor[4],
            effectConstructor[5],
            effectConstructor[6],
            effectConstructor[7],
            effectConstructor[8],
            effectConstructor[9],
            effectConstructor[10],
            effectConstructor[11]
            )

        newEffect.id = runBlocking {
            val result = CoroutineScope(Dispatchers.IO).async {
                repository.addEffects(newEffect.toEffectsDbEntity())
            }
            result.await()
        }.toInt()
        effItemManager.addItem(newEffect)
    }

    fun getEffectsToPlayerId(id : Int){
        viewModelScope.launch(Dispatchers.IO) {
            effItemManager.clearEffectsList()
            repository.getEffectsToIdPlayer(id).forEach {
                effItemManager.loadEffectsToPlayer(it.toEffectsItem())
            }
        }
    }

    fun deleteEffect(id: Int){
        modeSettings.postValue(0)
        effItemManager.deleteItem(id)
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteEffectsToId(id)
        }
    }

    fun updateCheckEffectsToPosition(id: Int, check : Int){
        effItemManager.updateCheckItemToPosition(id, check)
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateCheckEffects(TuplesEffectsCheck(id, check))
        }
    }


    fun updateEffect(name: String){
        val newEffect = EffectsItem(
            effectConstructor[12],
            effectConstructor[13],
            name,
            effectConstructor[14],
            effectConstructor[0],
            effectConstructor[1],
            effectConstructor[2],
            effectConstructor[3],
            effectConstructor[4],
            effectConstructor[5],
            effectConstructor[6],
            effectConstructor[7],
            effectConstructor[8],
            effectConstructor[9],
            effectConstructor[10],
            effectConstructor[11]
        )
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateEffects(newEffect.toEffectsDbEntity())
        }
        effItemManager.updateItem(effectConstructor[12], newEffect)
    }

    fun updateMode(id : Int){
        if (id == effectConstructor[12]){
            modeSettings.postValue(0)
        }
        else{
            val effect = effItemManager.getItemToId(id)
            nameEff = effect.name
            effectConstructor = listOf(
                effect.db,
                effect.bb,
                effect.power,
                effect.dexterity,
                effect.volition,
                effect.endurance,
                effect.intelect,
                effect.insihgt,
                effect.observation,
                effect.chsarisma,
                effect.bonusPower,
                effect.bonusEndurance,
                effect.id,
                effect.idPlayer,
                effect.isActive
            ).toMutableList()
            modeSettings.postValue(2)
        }


    }

    fun changeValueSettings(change: Int) : List<Int>{
        if (change==0){
            effectConstructor = listOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0).toMutableList()
        }
        if (index!=-1){
            if (index!=20){
                effectConstructor[index] = effectConstructor[index] + change
            }
            else{
                for (i in 0..9){
                    effectConstructor[i] = effectConstructor[i] + change
                }
            }

        }
        return effectConstructor
    }

    fun observationRadioButton(nameButton: String) {

        when (nameButton){
            "Дальний бой: " -> index = 0
            "Ближний бой: " -> index = 1
            "Сила: " -> index = 2
            "Ловкость: " -> index = 3
            "Воля: " -> index = 4
            "Стойковсть: " -> index = 5
            "Интелект: " -> index = 6
            "Сообразительность: " -> index = 7
            "Наблюдательность: " -> index = 8
            "Харизма: " -> index = 9
            "Бонус Силы: " -> index = 10
            "Бонус Устойчивости: " -> index = 11
            "Все Характеристики" -> index = 20
        }
        indexTvValue.postValue(index)

    }

    init {
        val daoEffectsDb = DataBase.getDb(application).getEffectsDao()
        repository = EffectsRepository(daoEffectsDb)
    }


    companion object{
        private var instance : EffectsViewModel? = null

        @JvmStatic
        fun getInstance(application: Application) : EffectsViewModel{
            return instance ?: synchronized(this){
                val newInstance = EffectsViewModel(application)
                instance = newInstance
                return newInstance
            }
        }
    }
}