package com.example.sumrak.ui.inventory.recycler.equipment.item

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.R
import com.example.sumrak.databinding.MaketInventoryEquipmentRecBinding
import com.example.sumrak.ui.inventory.recycler.equipment.EquipmentViewModel

class EquipmentItemAdapter(private val viewModel: EquipmentViewModel) : RecyclerView.Adapter<EquipmentItemAdapter.EquipmentItemViewHolder>() {
    private val equipmentItemManager = EquipmentItemManager.getInstance()

    init {
        equipmentItemManager.setAdapter(this)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EquipmentItemAdapter.EquipmentItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.maket_inventory_equipment_rec, parent, false)
        return EquipmentItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: EquipmentItemViewHolder, position: Int) {
        val item = equipmentItemManager.getItemToPos(position)
        holder.bind(item, position, viewModel)
    }

    override fun getItemCount(): Int {
        return equipmentItemManager.getItemCount()
    }


    class EquipmentItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val b = MaketInventoryEquipmentRecBinding.bind(itemView)
        fun bind(item: EquipmentItem, position: Int, viewModel: EquipmentViewModel) {
            b.tvNameEquipment.text = item.name
            b.tvEquipmentRepair.text = "(${item.charge}/${item.maxCharge}) : ${item.step}"

            b.btnSettingsEquipment.setOnClickListener { viewModel.setMode(3, item.id) }

            b.btnRepairEquipment.setOnClickListener { viewModel.setMode(4, item.id) }

            b.colorTest.setBackgroundResource(getColor(item.test))
        }

        fun getColor(test : Boolean) : Int{
            return if (test){
                R.color.d20
            }
            else{
                R.color.sumrak_grey50
            }
        }

    }


}