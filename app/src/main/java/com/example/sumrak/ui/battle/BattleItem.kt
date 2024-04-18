package com.example.sumrak.ui.battle

import com.example.sumrak.data.battle.BattleItemEntity
import com.example.sumrak.ui.battle.recycler.BattleList

data class BattleItem(
    val id: Int, // Уникальный идентификатор элемента
    val idPlayer: Int,
    val type: String, // Тип элемента (например, "info", "Damage" и т.д.)
    var position: Int,
    var isExpanded: Boolean, // Состояние элемента: true - раскрыт, false - свернут
    var view : BattleList
)
{
    fun toBattleItemEntity():BattleItemEntity = BattleItemEntity(
        id = id,
        idPlayer = idPlayer,
        type = type,
        position = position,
        isExpanded = isExpanded
    )
}