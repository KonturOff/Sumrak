package com.example.sumrak.ui.inventory.recycler.start

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.Player
import com.example.sumrak.R
import com.example.sumrak.databinding.MaketInventoryStartBinding
import com.example.sumrak.ui.inventory.InventoryFragment
import com.example.sumrak.ui.inventory.InventoryViewModel
import com.example.sumrak.ui.inventory.recycler.DelegateAdapter

class StartAdapter(
    private val inventoryViewModel: InventoryViewModel,
    private val inventoryFragment: InventoryFragment,
    private val lifecycleOwner: LifecycleOwner
) : DelegateAdapter<Start, StartAdapter.StartViewHolder, InventoryViewModel>() {


    override fun onCreateViewHolder(parent: ViewGroup): StartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.maket_inventory_start, parent, false)
        return StartViewHolder(view, inventoryViewModel, lifecycleOwner)
    }

    override fun onBindViewHolder(holder: StartViewHolder, item: Start) {
        holder.bind(item, inventoryViewModel, inventoryFragment)
    }

    class StartViewHolder(
        itemView: View,
        inventoryViewModel: InventoryViewModel,
        lifecycleOwner: LifecycleOwner
    ) : RecyclerView.ViewHolder(itemView) {
        val viewBinding = MaketInventoryStartBinding.bind(itemView)

        init {
            inventoryViewModel.startVisibility.observe(lifecycleOwner){
                viewBinding.startVisible.isVisible = it
            }
        }
        fun bind(
            item: Start,
            inventoryViewModel: InventoryViewModel,
            inventoryFragment: InventoryFragment
        ) {
            viewBinding.apply {
                inventoryViewModel.getVisibleView(item.name)
                tvStart.setOnClickListener { inventoryViewModel.updateVisibleView(item.name) }

                btnInfluenceRoll.text = "Проверка Влияния: ${Player.getInstance().getActivePlayer().influence}"
                btnStartMissiomAllPlayers.isEnabled = Player.getInstance().getPlayerCount()!=1

                btnStartMissiomAllPlayers.setOnClickListener { inventoryViewModel.recoverAllPers() }
                btnStartMaiisonActivePlayer.setOnClickListener { inventoryViewModel.recoverActivePers() }

                btnInfluenceRoll.setOnClickListener { inventoryFragment.influenceCheck() }
            }



        }

    }
}