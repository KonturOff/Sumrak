package com.example.sumrak.ui.inventory.recycler.consumables.item

import com.example.sumrak.Data.inventory.consumables.ConsumablesDbEntity

data class ConsumblesItem(
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
