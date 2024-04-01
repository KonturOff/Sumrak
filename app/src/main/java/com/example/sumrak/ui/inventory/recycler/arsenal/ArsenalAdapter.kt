package com.example.sumrak.ui.inventory.recycler.arsenal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.R
import com.example.sumrak.ui.inventory.recycler.DelegateAdapter

class ArsenalAdapter : DelegateAdapter<Arsenal, ArsenalAdapter.ArsenalViewHolder, Any?>() {
    override fun onCreateViewHolder(parent: ViewGroup): ArsenalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.maket_inventory_arsenal, parent, false)
        return ArsenalViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArsenalViewHolder, item: Arsenal) {
        // Установка значений и обработчиков для элемента Arsenal
        // ...
    }
    class ArsenalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(arsenal: Arsenal){

        }
    }
}
