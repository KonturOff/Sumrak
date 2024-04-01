package com.example.sumrak.ui.inventory.recycler.armor

import com.example.sumrak.R
import com.example.sumrak.ui.inventory.recycler.InventoryList

class Armor() : InventoryList {
    override val name: String
        get() = "Броня"
    override val viewType: Int
        get() = R.layout.maket_inventory_armor // Макет для элемента Armor
}