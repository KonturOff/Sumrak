package com.example.sumrak.data.battle

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sumrak.data.inventory.inventoryItem.InventoryItemEntity
import com.example.sumrak.data.inventory.inventoryItem.TuplesInventoryItem

@Dao
interface DaoBattleItem {
    @Insert
    suspend fun addBattleItem(battleItemEntity: BattleItemEntity)

    @Query("SELECT * FROM Battle_Item WHERE idPlayer = :idPlayer ORDER BY position ")
    suspend fun getBattleItemToPlayer(idPlayer: Int) : List<BattleItemEntity>

    @Update
    suspend fun updateBattleItemPosition(battleItemEntity: BattleItemEntity)
    @Update(entity = BattleItemEntity::class)
    suspend fun updateVisibleToId(tuplesBattleItem: TuplesBattleItem)

}