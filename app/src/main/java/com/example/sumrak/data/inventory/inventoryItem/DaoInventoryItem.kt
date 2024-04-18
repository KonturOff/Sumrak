package com.example.sumrak.data.inventory.inventoryItem

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sumrak.data.inventory.note.NoteDbEntity


@Dao
interface DaoInventoryItem {

    @Insert
    suspend fun addInventoryItem(inventoryItemEntity: InventoryItemEntity)

    @Insert(entity = NoteDbEntity::class)
    suspend fun addNotePlayer(noteDbEntity: NoteDbEntity)

    @Query("SELECT note FROM PlayerNote WHERE id = :idPlayer")
    suspend fun getNotePlayer(idPlayer: Int) : String

    @Query("SELECT * FROM Inventory_Item WHERE idPlayer = :idPlayer ORDER BY position ")
    suspend fun getInventoryItemToPlayer(idPlayer: Int) : List<InventoryItemEntity>

    @Update
    suspend fun updateInventoryItemPosition(inventoryItemEntity: InventoryItemEntity)
    @Update(entity = NoteDbEntity::class)
    suspend fun updateNotePlayer(noteDbEntity: NoteDbEntity)

    @Update(entity = InventoryItemEntity::class)
    suspend fun updateVisibleToId(tuplesInventoryItem: TuplesInventoryItem)

    @Query("UPDATE Arsenal " +
            "SET clip1 = maxPatrons, " +
            "clip2 = maxPatrons, " +
            "clip3 = maxPatrons " +
            "WHERE idPlayer = (SELECT value FROM Settings WHERE name_settings = 'active_id')")
    suspend fun recoverArsenalActivePers()

    @Query("UPDATE Arsenal " +
            "SET clip1 = maxPatrons, " +
            "clip2 = maxPatrons, " +
            "clip3 = maxPatrons ")
    suspend fun recoverArsenalAllPers()

    @Query("UPDATE Armor " +
            "SET endurance = enduranceMax " +
            "WHERE idPlayer = (SELECT value FROM Settings WHERE name_settings = 'active_id')")
    suspend fun recoverArmorActivePers()

    @Query("UPDATE Armor " +
            "SET endurance = enduranceMax")
    suspend fun recoverArmorAllPers()

    @Query("UPDATE PlayerVariable " +
            "SET hp = maxHp, " +
            "fate = maxFate " +
            "WHERE id = (SELECT value FROM Settings WHERE name_settings = 'active_id')")
    suspend fun recoverVariableActivePers()
    @Query("UPDATE PlayerVariable " +
            "SET hp = maxHp, " +
            "fate = maxFate")
    suspend fun recoverVariableAllPers()



}