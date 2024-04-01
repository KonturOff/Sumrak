package com.example.sumrak.ui.battle.recycler.initiative

import com.example.sumrak.R
import com.example.sumrak.ui.battle.recycler.BattleList

class Initiative : BattleList {
    override val name: String
        get() = "Инициатива"
    override val viewType: Int
        get() = R.layout.maket_battle_initiative
}