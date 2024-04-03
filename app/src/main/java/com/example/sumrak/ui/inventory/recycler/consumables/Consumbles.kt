package com.example.sumrak.ui.inventory.recycler.consumables

import com.example.sumrak.R
import com.example.sumrak.ui.inventory.recycler.InventoryList

class Consumbles : InventoryList {
    override val name: String
        get() = "Расходники"
    override val viewType: Int
        get() = R.layout.maket_inventory_consumables // Макет
}