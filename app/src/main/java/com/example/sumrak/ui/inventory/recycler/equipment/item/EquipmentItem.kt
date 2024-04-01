package com.example.sumrak.ui.inventory.recycler.equipment.item

import com.example.sumrak.data.inventory.equipment.EquipmentDbEntity

data class EquipmentItem(
    val id : Int,
    val idPlayer: Int,
    val name : String,
    var charge: Int,
    val maxCharge: Int,
    val step: Int,
    val test: Boolean
) {
    fun toEquipmentDbEntity() : EquipmentDbEntity = EquipmentDbEntity(
        id = id,
        idPlayer = idPlayer,
        name = name,
        charge = charge,
        maxCharge = maxCharge,
        step = step,
        test = test
    )
}