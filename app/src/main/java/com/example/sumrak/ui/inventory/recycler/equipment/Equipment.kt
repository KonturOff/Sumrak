package com.example.sumrak.ui.inventory.recycler.equipment

import com.example.sumrak.R
import com.example.sumrak.ui.inventory.recycler.InventoryList

class Equipment() : InventoryList {
    override val name: String
        get() = "Снаряжение"
    override val viewType: Int
        get() = R.layout.maket_inventory_equipment
}