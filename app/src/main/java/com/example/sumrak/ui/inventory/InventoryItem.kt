package com.example.sumrak.ui.inventory

import com.example.sumrak.Data.inventory.inventoryItem.InventoryItemEntity
import com.example.sumrak.ui.inventory.recycler.InventoryList


data class InventoryItem(
    val id: Int, // Уникальный идентификатор элемента
    val idPlayer: Int,
    val type: String, // Тип элемента (например, "Armor", "Effects" и т.д.)
    var position: Int,
    var isExpanded: Boolean, // Состояние элемента: true - раскрыт, false - свернут
    var view : InventoryList

)
{


    fun toInventoryItemEntity() : InventoryItemEntity = InventoryItemEntity(
        id = id,
        idPlayer = idPlayer,
        type = type,
        position = position,
        isExpanded = isExpanded
        )
}
