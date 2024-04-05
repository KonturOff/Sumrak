package com.example.sumrak.data.inventory.arsenal

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DaoArsenalDb {

    @Insert
    suspend fun addArsenalItem(arsenalDbEntity: ArsenalDbEntity) : Long

    @Update
    suspend fun updateArsemalItem(arsenalDbEntity: ArsenalDbEntity)

    @Update(entity = ArsenalDbEntity::class)
    suspend fun updatePatronsTuples(tuplesArsenalPatrons: TuplesArsenalPatrons)

    @Query("Select * From Arsenal Where idPlayer = :id")
    suspend fun getArsenalToIdPlayer(id: Int) : List<ArsenalDbEntity>

    @Query("Delete From Arsenal WHERE id = :id")
    suspend fun deleteArsenalToId(id: Int)
}