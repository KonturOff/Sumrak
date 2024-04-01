package com.example.sumrak.ui.battle.recycler.equipment

import com.example.sumrak.R
import com.example.sumrak.ui.battle.recycler.BattleList

class EquipmentB : BattleList {
    override val name: String
        get() = "Снаряжение"
    override val viewType: Int
        get() = R.layout.maket_inventory_equipment
}