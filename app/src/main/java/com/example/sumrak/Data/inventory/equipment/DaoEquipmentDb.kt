package com.example.sumrak.Data.inventory.equipment

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DaoEquipmentDb {

    @Insert
    suspend fun addEquipment(equipmentDbEntity: EquipmentDbEntity) : Long

    @Update
    suspend fun updateEquipment(equipmentDbEntity: EquipmentDbEntity)

    @Update(entity = EquipmentDbEntity::class)
    suspend fun updateEquipmentCharge(tuplesUpdateEquipment: TuplesUpdateEquipment)

    @Query("SELECT *  FROM Equipment Where idPlayer =:id")
    suspend fun getEquipmentToIdPlayer(id : Int) : List<EquipmentDbEntity>

    @Query("DELETE FROM Equipment Where id =:id")
    suspend fun deleteEquipmentToId(id: Int)

}