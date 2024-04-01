package com.example.sumrak.ui.inventory.recycler.consumables.item

import com.example.sumrak.data.inventory.consumables.ConsumablesDbEntity

data class ConsumablesItem(
    val id: Int,
    val idPlayer: Int,
    val name : String,
    val value : Int
){
 fun toConsumablesDbEntity(): ConsumablesDbEntity = ConsumablesDbEntity(
     id = id,
     idPlayer = idPlayer,
     name = name,
     value = value
 )
}
