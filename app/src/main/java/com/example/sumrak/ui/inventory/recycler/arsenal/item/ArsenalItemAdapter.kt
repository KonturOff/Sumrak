package com.example.sumrak.ui.inventory.recycler.arsenal.item

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.Player
import com.example.sumrak.R
import com.example.sumrak.databinding.MaketInventoryArsenalRecBinding
import com.example.sumrak.ui.inventory.recycler.arsenal.ArsenalViewModel

class ArsenalItemAdapter(
    private val viewModel: ArsenalViewModel,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<ArsenalItemAdapter.ArsenalItemViewHolder>() {
    private val arsenalItemManager = ArsenalItemManager.getInstance()
    private var selectedPosition = arsenalItemManager.getPosItemToId(Player.getInstance().getActivePlayer().activeArsenal)
        init {

            arsenalItemManager.setAdapter(this)
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArsenalItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.maket_inventory_arsenal_rec, parent, false)
        return ArsenalItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArsenalItemViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val item = arsenalItemManager.getItem(position)
        holder.bind(item, viewModel)

        holder.viewBinding.rBtnNameArsenal.isChecked = position == selectedPosition
        holder.viewBinding.rBtnNameArsenal.setOnClickListener {
            if (selectedPosition != position) {
                selectedPosition = position
                viewModel.setActiveArsenalToPlayer(position)
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return arsenalItemManager.getItemCount()
    }

    class ArsenalItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val viewBinding = MaketInventoryArsenalRecBinding.bind(itemView)
        fun bind(item: ArsenalItem, viewModel: ArsenalViewModel) {
            viewBinding.apply {
                rBtnNameArsenal.text = item.name

                if (item.id ==0){
                    btnSettingsArsenal.isVisible = false
                }

                if (item.classArsenal == 2){
                    btnRemoteArsenal.isVisible = true
                    tvPatronsArsenal.isVisible = true
                    tvPatronsArsenal.text = viewModel.getPatronsArsenal(item.id)
                }
                else{
                    btnRemoteArsenal.isVisible = false
                    tvPatronsArsenal.text = ""
                }

                btnSettingsArsenal.setOnClickListener { viewModel.setMode(2, item.id) }
                btnRemoteArsenal.setOnClickListener { viewModel.setMode(3, item.id) }
            }

        }

    }
}