package com.example.sumrak.Lists


import com.example.sumrak.ui.battle.recycler.initiative.InitiativeItem

data class HistoryRoll(
    val cube:String,
    val result_roll: String,
    val text_roll : String,
    val max_cube : String,
    val player : Int,
    val mode: String,
    val parameter: Int,
    var value: Int,
    val bonus: Int)
{

fun toInitiativeItem() : InitiativeItem = InitiativeItem(
    idPlayer = player,
    resultRoll = result_roll.toInt(),
    resultInitiative = parameter*2 - result_roll.toInt(),
    step = value
)


}

