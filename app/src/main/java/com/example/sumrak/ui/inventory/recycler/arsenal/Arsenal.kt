package com.example.sumrak.ui.inventory.recycler.arsenal

import com.example.sumrak.R
import com.example.sumrak.ui.inventory.recycler.InventoryList

class Arsenal : InventoryList {
    override val name: String
        get() = "Арсенал"
    override val viewType: Int
        get() = R.layout.maket_inventory_arsenal
}