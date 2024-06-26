package com.example.sumrak.lists


import com.example.sumrak.ui.battle.recycler.initiative.InitiativeItem

// TODO класс работай давай
data class HistoryRoll(
    val cube:String,
    val resultRoll: String,
    val textRoll : String,
    val maxCube : String,
    val player : Int,
    val mode: String,
    val parameter: Int,
    var value: Int,
    val bonus: Int
) {
    fun toInitiativeItem() : InitiativeItem = InitiativeItem(
        idPlayer = player,
        resultRoll = resultRoll.toInt(),
        resultInitiative = parameter * 2 - resultRoll.toInt(),
        step = value
    )
}

