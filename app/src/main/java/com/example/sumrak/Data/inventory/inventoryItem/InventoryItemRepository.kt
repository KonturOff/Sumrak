package com.example.sumrak.Data.inventory.inventoryItem

class InventoryItemRepository(
    private val daoInventoryItem: DaoInventoryItem
) {

    suspend fun addInventoryItem(inventoryItemEntity: InventoryItemEntity){
        return daoInventoryItem.addInventoryItem(inventoryItemEntity)
    }

    suspend fun getInventoryItemToPlayer(idPlayer : Int) : List<InventoryItemEntity>{
        return daoInventoryItem.getInventoryItemToPlayer(idPlayer)
    }

    suspend fun updateInventoryItemPos(inventoryItemEntity: InventoryItemEntity){
        return daoInventoryItem.updateInventoryItemPosution(inventoryItemEntity)
    }

    suspend fun updateVisibleToId (id: Int, isExpanded : Boolean){
        return daoInventoryItem.updateVisibileToId(TuplesInventoryItem(id, isExpanded))
    }
}