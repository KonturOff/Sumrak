package com.example.sumrak.Data.playerdb


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sumrak.Data.settings.SettingsDbEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface DaoPlayerDb {
    @Insert
    suspend fun addPlayer(playerDbEntity: PlayerDbEntity) : Long

    @Insert
    suspend fun addPlayerVariable(playerVariableEntity: PlayerVariableEntity)

    @Insert
    suspend fun insertSettings(settingsDbEntity: SettingsDbEntity)

    @Update
    suspend fun updatePlayer(playerDbEntity: PlayerDbEntity)

    @Update
    suspend fun updatePlayerVariable(playerVariableEntity: PlayerVariableEntity)

    @Update
    suspend fun updateActivePlayer(settingsDbEntity: SettingsDbEntity)

    @Update(entity = PlayerVariableEntity::class)
    suspend fun updateHpPlayer(updateHpPlayerTuples: UpdateHpPlayerTuples)

    @Update(entity = PlayerVariableEntity::class)
    suspend fun updateFatePlayer(updateFatePlayerTuples: UpdateFatePlayerTuples)

    @Update(entity = PlayerVariableEntity::class)
    suspend fun updateLightKarm(updateLightKarmTuples: UpdateLightKarmTuples)

    @Update(entity = PlayerVariableEntity::class)
    suspend fun updateDarkKarm(updateDarkKarmTuples: UpdateDarkKarmTuples)

    @Update(entity = PlayerVariableEntity::class)
    suspend fun updateDodgeParryingPlayer(updateDodgeParryingTuples: UpdateDodgeParryingTuples)

    @Update(entity = PlayerDbEntity::class)
    suspend fun updateArmorIdPlayer(updateArmorIdPlayerTuples: UpdateArmorIdPlayerTuples)

    @Query("SELECT player.*, " +
            "SUM(effect.db) AS total_db, " +
            "SUM(effect.bb) AS total_bb, " +
            "SUM(effect.power) AS total_power, " +
            "SUM(effect.dexterity) AS total_dexterity, " +
            "SUM(effect.volition) AS total_volition, " +
            "SUM(effect.endurance) AS total_endurance, " +
            "SUM(effect.intelect) AS total_intelect, " +
            "SUM(effect.insihgt) AS total_insihgt, " +
            "SUM(effect.observation) AS total_observation, " +
            "SUM(effect.chsarisma) AS total_chsarisma, " +
            "SUM(effect.bonusPower) AS total_bonus_power, " +
            "SUM(effect.bonusEndurance) AS total_bonus_endurance " +
            "FROM PlayersTab player " +
            "LEFT JOIN Effects effect ON player.id = effect.idPlayer AND effect.isActive = 1 " +
            "GROUP BY player.id")
    fun getAllPlayer(): Flow<List<PlayerIsEffectsDb>>

    @Query("SELECT * FROM PlayerVariable WHERE id = :id")
    suspend fun getPlayerVariableToId(id: Int) : PlayerVariableEntity

    @Query("SELECT * FROM PlayersTab WHERE id = :id")
    suspend fun getPlayerToId(id: Int): PlayerDbEntity

    @Query("SELECT count(*) FROM PlayersTab")
    suspend fun getCountPlayer(): Int

    @Query("SELECT * FROM Settings")
    fun  getSettings(): Flow<List<SettingsDbEntity>>

    @Query("SELECT count(*) FROM Settings")
    suspend fun getCountSettings() : Int

    @Query("DELETE FROM PlayersTab WHERE id = :id")
    suspend fun deletePlayer(id : Int)



}