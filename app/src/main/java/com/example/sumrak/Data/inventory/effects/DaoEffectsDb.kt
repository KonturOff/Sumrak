package com.example.sumrak.Data.inventory.effects

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DaoEffectsDb {

    @Insert
    suspend fun addEffects (effectsDbEntity: EffectsDbEntity): Long

    @Update
    suspend fun updateEffects (effectsDbEntity: EffectsDbEntity)

    @Update(entity = EffectsDbEntity::class)
    suspend fun updateCheckEffects (tuplesEffectsCheck: TuplesEffectsCheck)

    @Query("SELECT * FROM Effects WHERE idPlayer = :id")
    suspend fun getEffectsToIdPlayer(id: Int): List<EffectsDbEntity>

    @Query("DELETE FROM Effects WHERE id = :id")
    suspend fun deleteEffectsToId(id: Int)
}