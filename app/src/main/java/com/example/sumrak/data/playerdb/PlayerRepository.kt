package com.example.sumrak.data.playerdb


import com.example.sumrak.data.settings.SettingsDbEntity
import kotlinx.coroutines.flow.Flow

class PlayerRepository(
    private val daoPlayerDb: DaoPlayerDb
) {
    val readSettings: Flow<List<SettingsDbEntity>> = daoPlayerDb.getSettings()
    val readAllPlayer: Flow<List<PlayerIsEffectsDb>> = daoPlayerDb.getAllPlayer()


    suspend fun addPlayer(playerDbEntity: PlayerDbEntity) : Long{
        return daoPlayerDb.addPlayer(playerDbEntity)
    }

    suspend fun addPlayerVariable(playerVariableEntity: PlayerVariableEntity){
        daoPlayerDb.addPlayerVariable(playerVariableEntity)
    }

    suspend fun updatePlayer(playerDbEntity: PlayerDbEntity){
        daoPlayerDb.updatePlayer(playerDbEntity)
    }

    suspend fun updatePlayerVariable(playerVariableEntity: PlayerVariableEntity){
        return daoPlayerDb.updatePlayerVariable(playerVariableEntity)
    }

    suspend fun updateHpPlayer(id : Int, hp: Int){
        daoPlayerDb.updateHpPlayer(
            UpdateHpPlayerTuples(id, hp)
        )
    }

    suspend fun updateFatePlayer(id: Int, fate: Int){
        daoPlayerDb.updateFatePlayer(
            UpdateFatePlayerTuples(id, fate)
        )
    }

    suspend fun updateLightKarm(id: Int, light : Int){
        daoPlayerDb.updateLightKarm(
            UpdateLightKarmTuples(id, light)
        )
    }

    suspend fun updateDarkKarm(id: Int, dark : Int){
        daoPlayerDb.updateDarkKarm(
            UpdateDarkKarmTuples(id, dark)
        )
    }

    suspend fun updateArmorIdPlayer(id: Int, armorId : Int){
        daoPlayerDb.updateArmorIdPlayer(
            UpdateArmorIdPlayerTuples(id, armorId)
        )
    }

    suspend fun updateDodgeParryingPlayer(id: Int, dodge: Int, parrying : Int){
        daoPlayerDb.updateDodgeParryingPlayer(
            UpdateDodgeParryingTuples(id, dodge, parrying)
        )
    }

    suspend fun addSettings(settingsDbEntity: SettingsDbEntity){
        daoPlayerDb.insertSettings(settingsDbEntity)
    }

    suspend fun updateActivePlayer(settingsDbEntity: SettingsDbEntity){
        daoPlayerDb.updateActivePlayer(settingsDbEntity)
    }

    suspend fun getCountSettings() : Int{
        return daoPlayerDb.getCountSettings()
    }

    suspend fun getPlayerToId(id: Int) : PlayerDbEntity{
        return daoPlayerDb.getPlayerToId(id)
    }

    suspend fun getPlayerVariableToId(id: Int) : PlayerVariableEntity{
        return daoPlayerDb.getPlayerVariableToId(id)
    }

    suspend fun getCountPlayer() : Int{
        return daoPlayerDb.getCountPlayer()
    }

    suspend fun deletePlayer(id: Int){
        daoPlayerDb.deletePlayer(id)
    }
}


