package com.example.sumrak.data.inventory.consumables

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.example.sumrak.data.playerdb.PlayerDbEntity
import com.example.sumrak.ui.inventory.recycler.consumables.item.ConsumablesItem


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
    fun toConsumablesItem() : ConsumablesItem = ConsumablesItem(
            id = id!!,
            idPlayer = idPlayer,
            name = name,
            value = value
        )

}