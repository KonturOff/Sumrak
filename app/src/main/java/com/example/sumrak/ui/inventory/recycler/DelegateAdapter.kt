package com.example.sumrak.ui.inventory.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class DelegateAdapter<T : InventoryList, VH : RecyclerView.ViewHolder, U> {
    abstract fun onCreateViewHolder(parent: ViewGroup): VH
    abstract fun onBindViewHolder(holder: VH, item: T)
}