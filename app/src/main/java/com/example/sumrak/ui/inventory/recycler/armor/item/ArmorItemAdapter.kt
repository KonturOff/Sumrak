package com.example.sumrak.ui.inventory.recycler.armor.item

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.Player
import com.example.sumrak.R
import com.example.sumrak.databinding.MaketInventoryArmorRecBinding
import com.example.sumrak.ui.inventory.recycler.armor.ArmorViewModel

class ArmorItemAdapter(
    private val viewModel: ArmorViewModel,
    lifecycleOwner: LifecycleOwner
): RecyclerView.Adapter<ArmorItemAdapter.ArmorItemViewHolder>() {
    private val armorItemManager = ArmorItemManager.getInstance()
    private var selectedPosition =  armorItemManager.getPosItemToId(Player.getInstance().getActivePlayer().activeArmor)
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
        return ArmorItemViewHolder(itemView = view)//, viewModel, lifecycleOwner)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ArmorItemViewHolder, @SuppressLint("RecyclerView") position: Int) {


        val item = armorItemManager.getItem(position)
        holder.bind(
            item = item,
//            position,
            viewModel = viewModel
        )
        holder.viewBinding.rBtnArmor.isChecked = position == selectedPosition
        holder.viewBinding.rBtnArmor.setOnClickListener {
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

    class ArmorItemViewHolder(
        itemView: View,
//        viewModel: ArmorViewModel,
//        lifecycleOwner: LifecycleOwner
    ) : RecyclerView.ViewHolder(itemView) {
        val viewBinding = MaketInventoryArmorRecBinding.bind(itemView)

        fun bind(
            item: ArmorItem,
//            position: Int,
            viewModel: ArmorViewModel
        ) {
            viewBinding.apply {
                if (item.id == 0) {
                    btnRepairArmor.isVisible = false
                    btnSettingsArmor.isVisible = false
                    tvArmorEndurance.isVisible = false
                }

                tvArmorEndurance.text =
                    "${item.params} (${item.endurance}/${item.enduranceMax})"

                rBtnArmor.text = item.name
                btnSettingsArmor.setOnClickListener { viewModel.modeSettingsArmor(item.id) }
                btnRepairArmor.setOnClickListener { viewModel.modeRepairArmor(item.id) }
            }
        }

    }
}