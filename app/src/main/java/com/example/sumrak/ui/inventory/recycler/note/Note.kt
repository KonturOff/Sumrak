package com.example.sumrak.ui.inventory.recycler.note

import com.example.sumrak.R
import com.example.sumrak.ui.inventory.recycler.InventoryList

class Note : InventoryList {
    override val name: String
        get() = "Инвентарь"
    override val viewType: Int
        get() = R.layout.maket_inventory_note
}