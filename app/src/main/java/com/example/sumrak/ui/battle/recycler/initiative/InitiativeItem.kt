package com.example.sumrak.ui.battle.recycler.initiative

data class InitiativeItem(
    val idPlayer : Int,
    var resultRoll: Int,
    var resultInitiative: Int,
    var bonus: Int,
    var step : Int
)