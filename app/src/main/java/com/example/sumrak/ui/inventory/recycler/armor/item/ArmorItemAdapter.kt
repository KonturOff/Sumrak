package com.example.sumrak.ui.inventory.recycler.armor.item

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.Player
import com.example.sumrak.R
import com.example.sumrak.databinding.MaketInventoryArmorRecBinding
import com.example.sumrak.ui.inventory.recycler.armor.ArmorViewModel

class ArmorItemAdapter(
    private val viewModel: ArmorViewModel,
    private val lifecycleOwner: LifecycleOwner
): RecyclerView.Adapter<ArmorItemAdapter.ArmorItemViewHolder>() {
    private val armorItemManager = ArmorItemManager.getInstance()
    private var selectedPosition =  armorItemManager.getPosItemToId(Player.getInstance().getActivePlayer().active_armor)
    init {
        armorItemManager.setAdepter(this)

        viewModel.selectedNoArmor.observe(lifecycleOwner) {
            if (it) {
                selectedPosition = 0
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArmorItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.maket_inventory_armor_rec, parent, false)
        return ArmorItemViewHolder(view, viewModel, lifecycleOwner)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ArmorItemViewHolder, @SuppressLint("RecyclerView") position: Int) {


        val item = armorItemManager.getItem(position)
        holder.bind(item, position, viewModel)
        holder.b.rBtnArmor.isChecked = position == selectedPosition
        holder.b.rBtnArmor.setOnClickListener {
            if (selectedPosition != position) {
                selectedPosition = position
                viewModel.setActiveArmorToPlayer(position)
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return armorItemManager.getItemCount()
    }

    class ArmorItemViewHolder(itemView: View, viewModel: ArmorViewModel, lifecycleOwner: LifecycleOwner) : RecyclerView.ViewHolder(itemView) {
        val b = MaketInventoryArmorRecBinding.bind(itemView)



        @SuppressLint("SetTextI18n")
        fun bind(item: ArmorItem, position: Int, viewModel: ArmorViewModel) {


            if (item.id == 0){
                b.btnRepairArmor.visibility = View.GONE
                b.btnSettingsArmor.visibility = View.GONE
                b.tvArmorEndurance.visibility = View.GONE
            }

            b.tvArmorEndurance.text = "${item.params} (${item.endurance}/${item.enduranceMax})"


            b.rBtnArmor.text = item.name
            b.btnSettingsArmor.setOnClickListener { viewModel.modeSettingsArmor( item.id) }
            b.btnRepairArmor.setOnClickListener { viewModel.modeRepairArmor(item.id) }
        }

    }
}