package com.example.sumrak.ui.calculator.historyRoll

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.Lists.HistoryRoll

abstract class DelegateAdapterH<T : HistoryRoll,  VH : RecyclerView.ViewHolder> {
    abstract fun onCreateViewHolder(parent: ViewGroup): VH
    abstract fun onBindViewHolder(holder: VH, item: T, position: Int)
}