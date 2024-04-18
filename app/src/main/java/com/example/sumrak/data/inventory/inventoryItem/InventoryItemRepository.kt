package com.example.sumrak.data.inventory.inventoryItem

import com.example.sumrak.data.inventory.note.NoteDbEntity

class InventoryItemRepository(
    private val daoInventoryItem: DaoInventoryItem
) {

    suspend fun addInventoryItem(inventoryItemEntity: InventoryItemEntity){
        return daoInventoryItem.addInventoryItem(inventoryItemEntity)
    }

    suspend fun addNotePlayer(noteDbEntity: NoteDbEntity){
        return daoInventoryItem.addNotePlayer(noteDbEntity)
    }

    suspend fun getNotePlayer(idPlayer: Int) : String{
        return daoInventoryItem.getNotePlayer(idPlayer)
    }

    suspend fun updateNotePlayer(noteDbEntity: NoteDbEntity){
        return daoInventoryItem.updateNotePlayer(noteDbEntity)
    }
    suspend fun getInventoryItemToPlayer(idPlayer : Int) : List<InventoryItemEntity>{
        return daoInventoryItem.getInventoryItemToPlayer(idPlayer)
    }

    suspend fun updateInventoryItemPos(inventoryItemEntity: InventoryItemEntity){
        return daoInventoryItem.updateInventoryItemPosition(inventoryItemEntity)
    }

    suspend fun updateVisibleToId (id: Int, isExpanded : Boolean){
        return daoInventoryItem.updateVisibleToId(TuplesInventoryItem(id, isExpanded))
    }

    suspend fun recoverArsenalActivePers(){
        return daoInventoryItem.recoverArsenalActivePers()
    }

    suspend fun recoverArsenalAllPers(){
        return daoInventoryItem.recoverArsenalAllPers()
    }

    suspend fun recoverArmorActivePers(){
        return daoInventoryItem.recoverArmorActivePers()
    }

    suspend fun recoverArmorAllPers(){
        return daoInventoryItem.recoverArmorAllPers()
    }

    suspend fun recoverVariableActivePers(){
        return daoInventoryItem.recoverVariableActivePers()
    }

    suspend fun recoverVariableAllPers(){
        return daoInventoryItem.recoverVariableAllPers()
    }
}