package com.example.sumrak.ui.inventory.recycler.consumables

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.data.inventory.consumables.ConsumablesDbEntity
import com.example.sumrak.Player
import com.example.sumrak.R
import com.example.sumrak.databinding.MaketInventoryConsumablesBinding
import com.example.sumrak.ui.inventory.InventoryViewModel
import com.example.sumrak.ui.inventory.recycler.DelegateAdapter
import com.example.sumrak.ui.inventory.recycler.consumables.item.ConsumablesItemAdapter
import com.google.android.material.snackbar.Snackbar

class ConsumablesAdapter(
    private val viewModel: ConsumablesViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val context: Context,
    private val inventoryViewModel: InventoryViewModel
): DelegateAdapter<Consumbles, ConsumablesAdapter.ConsumablesViewHolder, ConsumablesViewModel>() {

    // Внутренний адаптер для списка элементов
    //private val innerAdapter = ConsumablesItemAdapter(viewModel,lifecycleOwner)
    
    override fun onCreateViewHolder(parent: ViewGroup): ConsumablesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.maket_inventory_consumables, parent, false)
        return ConsumablesViewHolder(
            itemView = view,
//            viewModel, 
            lifecycleOwner = lifecycleOwner,
//            context, 
            inventoryViewModel = inventoryViewModel
        )
    }

    override fun onBindViewHolder(
        holder: ConsumablesViewHolder,
        item: Consumbles
    ) {
        holder.bind(
            item = item,
            viewModel = viewModel,
//            lifecycleOwner, 
            lifecycleOwner = lifecycleOwner,
            context = context,
            inventoryViewModel = inventoryViewModel
        )
    }

    class ConsumablesViewHolder(
        itemView: View,
//        viewModel: ConsumablesViewModel,
        lifecycleOwner: LifecycleOwner,
//        context: Context,
        inventoryViewModel: InventoryViewModel
    ) : RecyclerView.ViewHolder(itemView) {

        private val binding = MaketInventoryConsumablesBinding.bind(itemView)

        init {
            inventoryViewModel.consumablesVisibility.observe(lifecycleOwner) { value ->
                // Реакция на изменения в LiveData
                binding.consumablesVisible.isVisible = value
            }
        }
        fun bind(
            item: Consumbles,
            viewModel: ConsumablesViewModel,
//            lifecycleOwner: LifecycleOwner,
            lifecycleOwner: LifecycleOwner,
            context: Context,
            inventoryViewModel: InventoryViewModel
        ) {

            inventoryViewModel.getVisibleView(item.name)
            binding.apply {
                consumablesRecView.layoutManager = LinearLayoutManager(context)
                consumablesRecView.adapter = ConsumablesItemAdapter(viewModel, lifecycleOwner)
                tvConsumables.setOnClickListener { inventoryViewModel.updateVisibleView(item.name) }

                addBtnConsumables.setOnClickListener {
                    modSettings(true)
                }
                btnEscSettingsConsumables.setOnClickListener {
                    modSettings(false)
                }
                btnSaveConsumables.setOnClickListener {
                    if (editConsumables.text.length > 30){
                        Snackbar
                            .make(
                                itemView,
                                "Название не должно превышать 30 знаков!",
                                Snackbar.LENGTH_SHORT
                            ).show()
                    }
                    else{
                        val itemEntity = ConsumablesDbEntity(
                            null,
                            Player.getInstance().getIdActivePlayer(),
                            editConsumables.text.toString(),
                            0,
                            false
                        )
                        viewModel.addItem(itemEntity)
                        editConsumables.text.clear()
                        modSettings(false)
                    }
                }
            }
        }

        private fun modSettings(mode: Boolean){
            if (mode){
                binding.addBtnConsumables.isVisible = false
                binding.settingsConsumables.isVisible = true
            }
            else{
                binding.addBtnConsumables.isVisible = true
                binding.settingsConsumables.isVisible = false
            }
        }
    }

}
