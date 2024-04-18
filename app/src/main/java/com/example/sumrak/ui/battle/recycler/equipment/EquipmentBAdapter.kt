package com.example.sumrak.ui.battle.recycler.equipment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.R
import com.example.sumrak.databinding.MaketBattleEquipmentBinding
import com.example.sumrak.ui.battle.BattleFragment
import com.example.sumrak.ui.battle.BattleViewModel
import com.example.sumrak.ui.battle.recycler.DelegateAdapterB
import com.example.sumrak.ui.battle.recycler.atack.AttackViewModel
import com.example.sumrak.ui.battle.recycler.equipment.item.EquipmentBItemAdapter

class EquipmentBAdapter(
    private val battleViewModel: BattleViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val context: Context,
    private val battleFragment: BattleFragment,
    private val equipmentBViewModel: EquipmentBViewModel
) : DelegateAdapterB<EquipmentB, EquipmentBAdapter.EquipmentBViewHolder, BattleViewModel>() {


    override fun onCreateViewHolder(parent: ViewGroup): EquipmentBViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.maket_battle_equipment, parent, false)
        val viewHolder = EquipmentBViewHolder(view, battleViewModel, lifecycleOwner)
        viewHolder.viewBinding.tvBEquipment.setOnClickListener { battleViewModel.updateVisibleView("Снаряжение") }
        viewHolder.viewBinding.rvBEquipment.layoutManager = LinearLayoutManager(context)
//        viewHolder.viewBinding.rvBEquipment.adapter = EquipmentBItemAdapter(equipmentViewModel)//, lifecycleOwner)
        viewHolder.viewBinding.rvBEquipment.adapter = EquipmentBItemAdapter(
            equipmentBViewModel,
//            lifecycleOwner,
            battleFragment
        )
        return viewHolder
    }

    override fun onBindViewHolder(
        holder: EquipmentBViewHolder,
        item: EquipmentB,
        attackViewModel: AttackViewModel
    ) {
        holder.bind(
            item,
            battleViewModel,
            lifecycleOwner,
            context = context,
            battleFragment = battleFragment,
            equipmentBViewModel = equipmentBViewModel
        )
    }

    class EquipmentBViewHolder(
        itemView: View,
        battleViewModel: BattleViewModel,
        lifecycleOwner: LifecycleOwner
    ) : RecyclerView.ViewHolder(itemView){
        val viewBinding = MaketBattleEquipmentBinding.bind(itemView)

        init {
            battleViewModel.getVisibleView("Снаряжение")
            battleViewModel.equipmentBVisible.observe(lifecycleOwner){
                viewBinding.bEquipmentVisible.isVisible = it
            }
        }

        fun bind(
            item: EquipmentB,
            battleViewModel: BattleViewModel,
            lifecycleOwner: LifecycleOwner,
            context: Context,
            battleFragment: BattleFragment,
            equipmentBViewModel: EquipmentBViewModel
        ) {

            viewBinding.apply {
                rvBEquipment.layoutManager = LinearLayoutManager(context)
//                rvBEquipment.adapter = EquipmentBItemAdapter(equipmentViewModel)//,lifecycleOwner)
                rvBEquipment.adapter = EquipmentBItemAdapter(
                    equipmentBViewModel,
//                    lifecycleOwner,
                    battleFragment
                )
            }

        }



    }
}