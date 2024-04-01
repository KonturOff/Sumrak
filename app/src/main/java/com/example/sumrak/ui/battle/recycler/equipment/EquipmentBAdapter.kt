package com.example.sumrak.ui.battle.recycler.equipment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.Player
import com.example.sumrak.R
import com.example.sumrak.databinding.MaketBattleEquipmentBinding
import com.example.sumrak.ui.battle.BattleFragment
import com.example.sumrak.ui.battle.BattleViewModel
import com.example.sumrak.ui.battle.recycler.DelegateAdapterB
import com.example.sumrak.ui.battle.recycler.equipment.item.EquipmentBItemAdapter
import com.example.sumrak.ui.inventory.recycler.equipment.EquipmentViewModel

class EquipmentBAdapter(
    private val battleViewModel: BattleViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val context: Context,
    private val battleFragment: BattleFragment,
    private val equipmentViewModel: EquipmentViewModel
) : DelegateAdapterB<EquipmentB, EquipmentBAdapter.EquipmentBViewHolder, BattleViewModel>() {


    override fun onCreateViewHolder(parent: ViewGroup): EquipmentBViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.maket_battle_equipment, parent, false)
        val viewHolder = EquipmentBViewHolder(view)
        viewHolder.b.rvBEquipment.layoutManager = LinearLayoutManager(context)
        viewHolder.b.rvBEquipment.adapter = EquipmentBItemAdapter(equipmentViewModel, lifecycleOwner)
        return viewHolder
    }



    override fun onBindViewHolder(
        holder: EquipmentBViewHolder,
        item: EquipmentB
    ) {
        holder.bind(
            item,
            battleViewModel,
            lifecycleOwner,
            context,
            battleFragment,
            equipmentViewModel
        )
    }

    class EquipmentBViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val b = MaketBattleEquipmentBinding.bind(itemView)

        fun bind(
            item: EquipmentB,
            battleViewModel: BattleViewModel,
            lifecycleOwner: LifecycleOwner,
            context: Context,
            battleFragment: BattleFragment,
            equipmentViewModel: EquipmentViewModel
        ) {
            equipmentViewModel.getEquipmentToIdPlayer(Player.getInstance().getIdActivePlayer())

            b.rvBEquipment.layoutManager = LinearLayoutManager(context)
            b.rvBEquipment.adapter = EquipmentBItemAdapter(equipmentViewModel,lifecycleOwner)

        }

    }
}