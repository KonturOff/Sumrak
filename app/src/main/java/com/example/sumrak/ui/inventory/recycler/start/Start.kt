package com.example.sumrak.ui.inventory.recycler.start

import com.example.sumrak.R
import com.example.sumrak.ui.inventory.recycler.InventoryList

class Start : InventoryList {
    override val name: String
        get() = "Старт Миссии"
    override val viewType: Int
        get() = R.layout.maket_inventory_start
}