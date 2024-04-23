package com.example.sumrak.ui.inventory.recycler.consumables.item

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.R
import com.example.sumrak.databinding.MaketInventoryConsumblesRecBinding
import com.example.sumrak.ui.inventory.recycler.consumables.ConsumablesViewModel



class ConsumablesItemAdapter(
    private val viewModel: ConsumablesViewModel,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<ConsumablesItemViewHolder>() {

    private val consumablesItemManager = ConsumablesItemManager.getInstance()

    init {
        consumablesItemManager.setAdapter(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsumablesItemViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.maket_inventory_consumbles_rec, parent, false)
        return ConsumablesItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConsumablesItemViewHolder, position: Int) {
        val item = consumablesItemManager.getItem(position)
        holder.bind(
            item = item,
//            position,
            viewModel = viewModel,
            lifecycleOwner

        )
    }

    override fun getItemCount(): Int {
        return consumablesItemManager.getItemCount()
    }
}

class ConsumablesItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val viewBinding = MaketInventoryConsumblesRecBinding.bind(itemView)


    fun bind(
        item: ConsumablesItem,
//        position: Int,
        viewModel: ConsumablesViewModel,
        lifecycleOwner: LifecycleOwner
    ) {
        viewBinding.apply {

            viewModel.activeConumables.observe(lifecycleOwner){
                if (item.id==it){
                    setModeRepair(true)
                }
                else{
                    setModeRepair(false)
                }
            }

            lLFaceConsubles.setOnClickListener { viewModel.setActiveConsumables(item.id) }

            linkMode(viewModel.checkLinkToIdConsum(item.id))
            nameConsumblesItem.text = item.name
            valueConsumdlesItem.text = item.value.toString()
            valueConsumdlesItemRepair.text = item.value.toString()

            btnDeleteConsumbles.setOnClickListener {
                viewModel.deleteItem(item.id)
            }

            btnAddValueConsumbles.setOnClickListener {
                viewModel.updateItem(item.id, 1)
                valueConsumdlesItem.text =
                    ConsumablesItemManager.getInstance().getItemToId(item.id).value.toString()
                valueConsumdlesItemRepair.text =
                    ConsumablesItemManager.getInstance().getItemToId(item.id).value.toString()
                viewModel.updateConsumables(
                    ConsumablesItemManager.getInstance().getItemToId(item.id).id,
                    ConsumablesItemManager.getInstance().getItemToId(item.id).value
                )
            }
            btnRemValueConsumbles.setOnClickListener {
                viewModel.updateItem(item.id, -1)
                valueConsumdlesItem.text =
                    ConsumablesItemManager.getInstance().getItemToId(item.id).value.toString()
                valueConsumdlesItemRepair.text =
                    ConsumablesItemManager.getInstance().getItemToId(item.id).value.toString()
                viewModel.updateConsumables(
                    ConsumablesItemManager.getInstance().getItemToId(item.id).id,
                    ConsumablesItemManager.getInstance().getItemToId(item.id).value
                )
            }
        }
    }

    private fun setModeRepair(mode: Boolean) {
        viewBinding.apply {
            if (mode){
                lLRepairConsumables.isVisible = true
                lLFaceConsubles.setBackgroundResource(R.color.consumables200)
                valueConsumdlesItem.setBackgroundResource(R.color.consumables100)
            }
            else{
                lLRepairConsumables.isVisible = false
                lLFaceConsubles.setBackgroundResource(R.color.white)
                valueConsumdlesItem.setBackgroundResource(R.color.white)
            }
        }
    }

    fun linkMode(mode : Boolean){
        if (mode){
            viewBinding.imLink.isVisible = true
            viewBinding.btnDeleteConsumbles.isVisible = false
        }
        else{
            viewBinding.imLink.isVisible = false
            viewBinding.btnDeleteConsumbles.isVisible = true
        }

    }
}
