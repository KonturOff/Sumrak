package com.example.sumrak.ui.battle

import com.example.sumrak.ui.battle.recycler.BattleList
import com.example.sumrak.ui.inventory.recycler.InventoryList

data class BattleItem(
    val id: Int, // Уникальный идентификатор элемента
    val idPlayer: Int,
    val type: String, // Тип элемента (например, "info", "Damage" и т.д.)
    var position: Int,
    var isExpanded: Boolean, // Состояние элемента: true - раскрыт, false - свернут
    var view : BattleList
)