package com.example.sumrak.data.inventory.consumables

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DaoConsumablesDb {

    @Insert
    suspend fun addConsumables(consumablesDbEntity: ConsumablesDbEntity): Long

    @Update
    suspend fun updateConsumables(consumablesDbEntity: ConsumablesDbEntity)

    @Update (entity = ConsumablesDbEntity::class)
    suspend fun updateValueConsumablesTaples(updateTuplesConsumables: UpdateTuplesConsumables)

    @Query("SELECT * FROM CONSUMABLES WHERE idPlayer = :id")
    suspend fun getConsumablesToPlayerId(id : Int):List<ConsumablesDbEntity>

    @Query("DELETE FROM CONSUMABLES WHERE id = :id")
    suspend fun deleteConsumables(id: Int)
}