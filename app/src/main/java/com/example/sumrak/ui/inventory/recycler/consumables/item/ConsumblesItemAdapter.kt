package com.example.sumrak.ui.inventory.recycler.consumables.item

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.R
import com.example.sumrak.databinding.MaketInventoryConsumblesRecBinding
import com.example.sumrak.ui.inventory.recycler.consumables.ConsumblesViewModel



class ConsumblesItemAdapter(
    private val viewModel: ConsumblesViewModel
) : RecyclerView.Adapter<ConsumblesItemViewHolder>() {

    private val consumablesItemManager = ConsumablesItemManager.getInstance()

    init {
        consumablesItemManager.setAdapter(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsumblesItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.maket_inventory_consumbles_rec, parent, false)
        return ConsumblesItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConsumblesItemViewHolder, position: Int) {
        val item = consumablesItemManager.getItem(position)
        holder.bind(item, position, viewModel)
    }

    override fun getItemCount(): Int {
        return consumablesItemManager.getItemCount()
    }
}

class ConsumblesItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val b = MaketInventoryConsumblesRecBinding.bind(itemView)


    fun bind(item: ConsumblesItem, position: Int, viewModel: ConsumblesViewModel, ) {
        b.nameConsumblesItem.text = item.name
        b.valueConsumdlesItem.text = item.value.toString()

        b.btnDeleteConsumbles.setOnClickListener {
            viewModel.deleteItem(item.id)
        }

        b.btnAddValueConsumbles.setOnClickListener {
            viewModel.updateItem(item.id, 1)
            b.valueConsumdlesItem.text = ConsumablesItemManager.getInstance().getItemToId(item.id).value.toString()
            viewModel.updateConsumbles(ConsumablesItemManager.getInstance().getItemToId(item.id).id, ConsumablesItemManager.getInstance().getItemToId(item.id).value)
        }
        b.btnRemValueConsumbles.setOnClickListener {
            viewModel.updateItem(item.id, -1)
            b.valueConsumdlesItem.text = ConsumablesItemManager.getInstance().getItemToId(item.id).value.toString()
            viewModel.updateConsumbles(ConsumablesItemManager.getInstance().getItemToId(item.id).id, ConsumablesItemManager.getInstance().getItemToId(item.id).value)
        }


    }
}




