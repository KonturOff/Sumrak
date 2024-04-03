package com.example.sumrak.ui.inventory.recycler.effects

import com.example.sumrak.R
import com.example.sumrak.ui.inventory.recycler.InventoryList

class Effects : InventoryList {
    override val name: String
        get() = "Эффекты"

    override val viewType: Int
        get() = R.layout.maket_inventory_effects
}