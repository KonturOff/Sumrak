package com.example.sumrak.data.inventory.inventoryItem

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.sumrak.data.playerdb.PlayerDbEntity
import com.example.sumrak.ui.inventory.InventoryItem
import com.example.sumrak.ui.inventory.recycler.armor.Armor
import com.example.sumrak.ui.inventory.recycler.arsenal.Arsenal
import com.example.sumrak.ui.inventory.recycler.consumables.Consumbles
import com.example.sumrak.ui.inventory.recycler.effects.Effects
import com.example.sumrak.ui.inventory.recycler.equipment.Equipment

@Entity(tableName = "Inventory_Item",foreignKeys = [ForeignKey(
    entity = PlayerDbEntity::class,
    parentColumns = ["id"],
    childColumns = ["idPlayer"],
    onDelete = ForeignKey.CASCADE
)
]
    )
data class InventoryItemEntity(
    @PrimaryKey (autoGenerate = true) val id: Int,
    val idPlayer : Int,
    val type: String,
    val position: Int,
    val isExpanded: Boolean
) {

    fun toInventoryItem() : InventoryItem = InventoryItem(
        id = id,
        idPlayer = idPlayer,
        type = type,
        position = position,
        isExpanded = isExpanded,
        view = when(type){
            "Эффекты" -> Effects()
            "Расходники" -> Consumbles()
            "Броня" -> Armor()
            "Арсенал" -> Arsenal()
            "Снаряжение" -> Equipment()
            else -> Effects()
        }
    )
}