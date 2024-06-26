package com.example.sumrak.data.inventory.inventoryItem

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface DaoInventoryItem {

    @Insert
    suspend fun addInventoryItem(inventoryItemEntity: InventoryItemEntity)

    @Query("SELECT * FROM Inventory_Item WHERE idPlayer = :idPlayer ORDER BY position ")
    suspend fun getInventoryItemToPlayer(idPlayer: Int) : List<InventoryItemEntity>

    @Update
    suspend fun updateInventoryItemPosition(inventoryItemEntity: InventoryItemEntity)

    @Update(entity = InventoryItemEntity::class)
    suspend fun updateVisibleToId(tuplesInventoryItem: TuplesInventoryItem)
}