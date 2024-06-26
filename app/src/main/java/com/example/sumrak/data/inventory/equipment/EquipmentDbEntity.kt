package com.example.sumrak.data.inventory.equipment

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.sumrak.data.playerdb.PlayerDbEntity
import com.example.sumrak.ui.inventory.recycler.equipment.item.EquipmentItem


@Entity(tableName = "Equipment",
    foreignKeys = [ForeignKey(
        entity = PlayerDbEntity::class,
        parentColumns = ["id"],
        childColumns = ["idPlayer"],
        onDelete = ForeignKey.CASCADE
    )
    ])
data class EquipmentDbEntity(
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
    val idPlayer: Int,
    val name : String,
    var charge: Int,
    val maxCharge: Int,
    val step: Int,
    val test: Boolean,
    val consumablesLink: Int
) {

    fun toEquipmentItem():EquipmentItem = EquipmentItem(
        id = id,
        idPlayer = idPlayer,
        name = name,
        charge = charge,
        maxCharge = maxCharge,
        step = step,
        test = test,
        consumablesLink = consumablesLink
    )
}