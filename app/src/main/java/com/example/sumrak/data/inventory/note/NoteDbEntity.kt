package com.example.sumrak.data.inventory.note

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.sumrak.data.playerdb.PlayerDbEntity

@Entity(tableName = "PlayerNote",
    foreignKeys = [ForeignKey(
        entity = PlayerDbEntity::class,
        parentColumns = ["id"],
        childColumns = ["id"],
        onDelete = ForeignKey.CASCADE)
    ])
data class NoteDbEntity(
    @PrimaryKey val id : Int,
    var note : String
) {
}