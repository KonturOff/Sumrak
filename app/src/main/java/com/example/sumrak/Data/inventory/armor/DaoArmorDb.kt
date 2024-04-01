package com.example.sumrak.Data.inventory.armor

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface DaoArmorDb {

    @Insert
    suspend fun addArmor(armorDbEntity: ArmorDbEntity) : Long

    @Update
    suspend fun updateArmor(armorDbEntity: ArmorDbEntity)

    @Update (entity = ArmorDbEntity::class)
    suspend fun updateEnduranceArmor(tuplesArmorEndurance: TuplesArmorEndurance)

    @Query("SELECT * FROM Armor Where idPlayer = :idPlayer")
    suspend fun getArmorListToPlayer(idPlayer: Int) : List<ArmorDbEntity>

    @Query("SELECT * FROM Armor WHERE id = :id")
    suspend fun getArmorToId(id: Int) : ArmorDbEntity

    @Query("DELETE FROM Armor WHERE id = :id")
    suspend fun deleteArmorToId(id : Int)
}