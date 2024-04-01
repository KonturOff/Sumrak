package com.example.sumrak.Data.inventory.consumables

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.example.sumrak.Data.playerdb.PlayerDbEntity
import com.example.sumrak.ui.inventory.recycler.consumables.item.ConsumblesItem


@Entity(tableName = "Consumables",
    foreignKeys = [ForeignKey(
        entity = PlayerDbEntity::class,
        parentColumns = ["id"],
        childColumns = ["idPlayer"],
        onDelete = CASCADE)
    ]
)
data class ConsumablesDbEntity(
    @PrimaryKey(autoGenerate = true) val id : Int? = null,
    val idPlayer: Int,
    val name : String,
    val value : Int
) {
    fun toConsumablesItem() : ConsumblesItem = ConsumblesItem(
            id = id!!,
            idPlayer = idPlayer,
            name = name,
            value = value
        )

}