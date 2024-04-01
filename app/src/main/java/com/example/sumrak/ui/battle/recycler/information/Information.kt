package com.example.sumrak.ui.battle.recycler.information

import com.example.sumrak.R
import com.example.sumrak.ui.battle.recycler.BattleList

class Information() : BattleList {
    override val name: String
        get() = "Информация"
    override val viewType: Int
        get() = R.layout.maket_battle_information
}