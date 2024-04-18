package com.example.sumrak.ui.inventory

import android.content.Context
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.R
import com.example.sumrak.ui.inventory.recycler.armor.ArmorAdapter
import com.example.sumrak.ui.inventory.recycler.arsenal.ArsenalAdapter
import com.example.sumrak.ui.inventory.recycler.armor.Armor
import com.example.sumrak.ui.inventory.recycler.armor.ArmorViewModel
import com.example.sumrak.ui.inventory.recycler.arsenal.Arsenal
import com.example.sumrak.ui.inventory.recycler.arsenal.ArsenalViewModel
import com.example.sumrak.ui.inventory.recycler.consumables.Consumbles
import com.example.sumrak.ui.inventory.recycler.consumables.ConsumablesAdapter
import com.example.sumrak.ui.inventory.recycler.consumables.ConsumablesViewModel
import com.example.sumrak.ui.inventory.recycler.effects.Effects
import com.example.sumrak.ui.inventory.recycler.effects.EffectsAdapter
import com.example.sumrak.ui.inventory.recycler.effects.EffectsViewModel
import com.example.sumrak.ui.inventory.recycler.equipment.Equipment
import com.example.sumrak.ui.inventory.recycler.equipment.EquipmentAdapter
import com.example.sumrak.ui.inventory.recycler.equipment.EquipmentViewModel
import com.example.sumrak.ui.inventory.recycler.note.Note
import com.example.sumrak.ui.inventory.recycler.note.NoteAdapter
import com.example.sumrak.ui.inventory.recycler.start.Start
import com.example.sumrak.ui.inventory.recycler.start.StartAdapter

class InventoryAdapter(
    private val inventoryManager: InventoryManager,
    private val viewModels: Map<Int, ViewModel>,
    private val lifecycleOwner: LifecycleOwner,
    private val context: Context,
    private val inventoryViewModel: InventoryViewModel,
    private val inventoryFragment: InventoryFragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun onItemMove(fromPosition: Int, toPosition: Int) {
        inventoryManager.items.add(toPosition, inventoryManager.items.removeAt(fromPosition))
        notifyItemMoved(fromPosition, toPosition)
        inventoryViewModel.updateItemsToInventoryList()
    }

    override fun getItemViewType(position: Int): Int {
        return inventoryManager.getItem(position).view.viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.maket_inventory_effects -> EffectsAdapter(viewModels.getValue(viewType) as EffectsViewModel, lifecycleOwner, context, inventoryViewModel).onCreateViewHolder(parent)
            R.layout.maket_inventory_arsenal -> ArsenalAdapter(viewModels.getValue(viewType) as ArsenalViewModel, lifecycleOwner, context, inventoryViewModel).onCreateViewHolder(parent)
            R.layout.maket_inventory_armor -> ArmorAdapter(viewModels.getValue(viewType) as ArmorViewModel, lifecycleOwner, context, inventoryViewModel).onCreateViewHolder(parent)
            R.layout.maket_inventory_consumables -> ConsumablesAdapter(viewModels.getValue(viewType) as ConsumablesViewModel, lifecycleOwner, context, inventoryViewModel).onCreateViewHolder(parent)
            R.layout.maket_inventory_equipment -> EquipmentAdapter(viewModels.getValue(viewType) as EquipmentViewModel, lifecycleOwner, context, inventoryViewModel, inventoryFragment).onCreateViewHolder(parent)
            R.layout.maket_inventory_start -> StartAdapter(inventoryViewModel, inventoryFragment, lifecycleOwner).onCreateViewHolder(parent)
            R.layout.maket_inventory_note -> NoteAdapter(inventoryViewModel, inventoryFragment, lifecycleOwner).onCreateViewHolder(parent)
            else -> throw UnsupportedOperationException("ViewType not supported")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val items = inventoryManager.getItem(position)
        when (items.view.viewType) {
            R.layout.maket_inventory_effects -> (holder as EffectsAdapter.EffectsViewHolder).bind(
                items.view as Effects,
                viewModels.getValue(items.view.viewType) as EffectsViewModel,
//                lifecycleOwner,
                context,
                inventoryViewModel
            )
            R.layout.maket_inventory_arsenal -> (holder as ArsenalAdapter.ArsenalViewHolder).bind(
                items.view as Arsenal,
                viewModels.getValue(items.view.viewType) as ArsenalViewModel,
                lifecycleOwner,
                context,
                inventoryViewModel
            )
            R.layout.maket_inventory_armor -> (holder as ArmorAdapter.ArmorViewHolder).bind(
                items.view as Armor,
                viewModels.getValue(items.view.viewType) as ArmorViewModel,
                lifecycleOwner,
                context,
                inventoryViewModel
            )
            R.layout.maket_inventory_consumables -> (holder as ConsumablesAdapter.ConsumablesViewHolder).bind(
                items.view as Consumbles,
                viewModels.getValue(items.view.viewType) as ConsumablesViewModel,
//                lifecycleOwner,
                context,
                inventoryViewModel
            )
            R.layout.maket_inventory_equipment -> (holder as EquipmentAdapter.EquipmentViewHolder).bind(
                items.view as Equipment,
                viewModels.getValue(items.view.viewType) as EquipmentViewModel,
//                lifecycleOwner,
                context,
                inventoryViewModel,
                inventoryFragment
            )
            R.layout.maket_inventory_start -> (holder as StartAdapter.StartViewHolder).bind(
                items.view as Start,
                inventoryViewModel,
                inventoryFragment
            )
            R.layout.maket_inventory_note -> (holder as NoteAdapter.NoteViewHolder).bind(
                items.view as Note,
                inventoryViewModel,
                inventoryFragment
            )
        }

    }

    override fun getItemCount(): Int {
        return inventoryManager.getCountItem()
    }
}
