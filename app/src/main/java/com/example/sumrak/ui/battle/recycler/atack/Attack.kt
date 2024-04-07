package com.example.sumrak.ui.battle.recycler.atack

import com.example.sumrak.R
import com.example.sumrak.ui.battle.recycler.BattleList

class Attack : BattleList {
    override val name: String
        get() = "Атака"
    override val viewType: Int
        get() = R.layout.maket_battle_arsenal
}