package com.example.sumrak.ui.inventory.recycler.consumables

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.Data.inventory.consumables.ConsumablesDbEntity
import com.example.sumrak.Player
import com.example.sumrak.R
import com.example.sumrak.databinding.MaketInventoryConsumablesBinding
import com.example.sumrak.ui.inventory.InventoryViewModel
import com.example.sumrak.ui.inventory.recycler.DelegateAdapter
import com.example.sumrak.ui.inventory.recycler.consumables.item.ConsumblesItemAdapter

class ConsumblesAdapter(
    private val viewModel: ConsumblesViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val context: Context,
    private val inventoryViewModel: InventoryViewModel
): DelegateAdapter<Consumbles, ConsumblesAdapter.ConsumblesViewHolder, ConsumblesViewModel>() {


    // Внутренний адаптер для списка элементов
    //private val innerAdapter = ConsumblesItemAdapter(viewModel,lifecycleOwner)



    override fun onCreateViewHolder(parent: ViewGroup): ConsumblesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.maket_inventory_consumables, parent, false)
        return ConsumblesViewHolder(view, viewModel, lifecycleOwner, context, inventoryViewModel)
    }

    override fun onBindViewHolder(
        holder: ConsumblesViewHolder,
        item: Consumbles
    ) {
        holder.bind(item, viewModel, lifecycleOwner, context, inventoryViewModel)
    }

    class ConsumblesViewHolder(
        itemView: View,
        viewModel: ConsumblesViewModel,
        lifecycleOwner: LifecycleOwner,
        context: Context,
        inventoryViewModel: InventoryViewModel
    ) : RecyclerView.ViewHolder(itemView) {

        private val binding = MaketInventoryConsumablesBinding.bind(itemView)

        init {
            inventoryViewModel.consumablesVisibility.observe(lifecycleOwner) { value ->
                // Реакция на изменения в LiveData
                if (value) {
                    binding.consumablesVisible.visibility = View.VISIBLE
                } else {
                    binding.consumablesVisible.visibility = View.GONE
                }
            }

        }
        fun bind(
            item : Consumbles,
            viewModel: ConsumblesViewModel,
            lifecycleOwner: LifecycleOwner,
            context: Context,
            inventoryViewModel: InventoryViewModel
        ){

            inventoryViewModel.getVisibleView(item.name)

            binding.consumablesRecView.layoutManager = LinearLayoutManager(context)
            binding.consumablesRecView.adapter = ConsumblesItemAdapter(viewModel)
            binding.tvConsumables.setOnClickListener { inventoryViewModel.updateVisbleView(item.name) }

            binding.addBtnConsumables.setOnClickListener {
                modSettings(true)
            }
            binding.btnEscSettingsConsumables.setOnClickListener {
                modSettings(false)
            }
            binding.btnSaveConsumables.setOnClickListener {
                val itemEntity = ConsumablesDbEntity(null,
                    Player.getInstance().getIdActivePlayer(),
                    binding.editConsumables.text.toString(),
                    0)
                viewModel.addItem(itemEntity)
                binding.editConsumables.text.clear()
                modSettings(false)
            }

        }

        private fun modSettings(mode: Boolean){
            if (mode){
                binding.addBtnConsumables.visibility = View.GONE
                binding.settingsConsumables.visibility = View.VISIBLE
            }
            else{
                binding.addBtnConsumables.visibility = View.VISIBLE
                binding.settingsConsumables.visibility = View.GONE
            }
        }
    }

}




