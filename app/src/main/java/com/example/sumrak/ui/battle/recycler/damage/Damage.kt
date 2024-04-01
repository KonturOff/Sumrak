package com.example.sumrak.ui.battle.recycler.damage

import com.example.sumrak.R
import com.example.sumrak.ui.battle.recycler.BattleList

class Damage : BattleList {
    override val name: String
        get() = "Получение Урона"
    override val viewType: Int
        get() = R.layout.maket_battle_damage
}