package com.example.sumrak.ui.inventory.recycler.consumables.item

import com.example.sumrak.data.inventory.consumables.ConsumablesDbEntity

data class ConsumablesItem(
    val id: Int,
    val idPlayer: Int,
    val name : String,
    var value : Int,
    val link : Boolean
){
 fun toConsumablesDbEntity(): ConsumablesDbEntity = ConsumablesDbEntity(
     id = id,
     idPlayer = idPlayer,
     name = name,
     value = value,
     link = link
 )
}
