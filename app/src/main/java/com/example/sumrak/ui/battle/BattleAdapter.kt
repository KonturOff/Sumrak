package com.example.sumrak.ui.battle

import android.content.Context
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.R
import com.example.sumrak.ui.battle.recycler.atack.Attack
import com.example.sumrak.ui.battle.recycler.atack.AttackAdapter
import com.example.sumrak.ui.battle.recycler.atack.AttackViewModel
import com.example.sumrak.ui.battle.recycler.damage.DamageAdapter
import com.example.sumrak.ui.battle.recycler.equipment.EquipmentBAdapter
import com.example.sumrak.ui.battle.recycler.equipment.EquipmentBViewModel
import com.example.sumrak.ui.battle.recycler.information.InformationAdapter
import com.example.sumrak.ui.battle.recycler.initiative.InititiveAdapter


class BattleAdapter(
    private val battleManager: BattleManager,
    private val lifecycleOwner: LifecycleOwner,
    private val context: Context,
    private val battleViewModel: BattleViewModel,
    private val battleFragment: BattleFragment,
    private val equipmentBViewModel: EquipmentBViewModel,
    private val attackViewModel: AttackViewModel
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun onItemMove(fromPosition: Int, toPosition: Int) {
        battleManager.items.add(toPosition, battleManager.items.removeAt(fromPosition))
        notifyItemMoved(fromPosition, toPosition)
    }


    override fun getItemViewType(position: Int): Int {
        return battleManager.getItem(position).view.viewType
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.maket_battle_information ->
                InformationAdapter(
                    battleViewModel,
                    lifecycleOwner,
//                    context
                ).onCreateViewHolder(parent)
            R.layout.maket_battle_damage ->
                DamageAdapter(
                    battleViewModel,
                    lifecycleOwner,
                    context,
                    battleFragment
                ).onCreateViewHolder(parent)
            R.layout.maket_battle_initiative ->
                InititiveAdapter(
                    battleViewModel,
                    lifecycleOwner,
//                    context,
                    battleFragment
                ).onCreateViewHolder(parent)
            R.layout.maket_inventory_equipment ->
                EquipmentBAdapter(
//                    battleViewModel,
//                    lifecycleOwner,
                    context = context,
                    battleFragment = battleFragment,
                    equipmentBViewModel = equipmentBViewModel
                ).onCreateViewHolder(parent)
            R.layout.maket_battle_arsenal -> AttackAdapter(
                battleViewModel,
                lifecycleOwner,
                context,
                battleFragment,
                attackViewModel
            ).onCreateViewHolder(parent)
            else -> throw UnsupportedOperationException("ViewType not supported")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = battleManager.getItem(position)
        when (item.view.viewType){
            R.layout.maket_battle_information ->
                (holder as InformationAdapter.InformationViewHolder)
                    .bind(
//                        item.view as Information,
//                        battleViewModel,
//                        lifecycleOwner,
//                        context
                    )
            R.layout.maket_battle_damage ->
                (holder as DamageAdapter.DamageViewHolder)
                    .bind(
//                        item.view as Damage,
                        battleViewModel = battleViewModel,
//                        lifecycleOwner,
                        context = context,
//                        battleFragment
                    )
            R.layout.maket_battle_initiative ->
                (holder as InititiveAdapter.InitiativeViewHolder)
                    .bind(
        //                item.view as Initiative,
                        viewModel = battleViewModel,
        //                lifecycleOwner,
        //                context,
        //                battleFragment
                    )
            R.layout.maket_battle_equipment ->
                (holder as EquipmentBAdapter.EquipmentBViewHolder)
                    .bind(
//                        item.view as EquipmentB,
//                        battleViewModel,
//                        lifecycleOwner,
                        context = context,
                        battleFragment = battleFragment,
                        equipmentBViewModel = equipmentBViewModel
                    )
            R.layout.maket_battle_arsenal ->
                (holder as AttackAdapter.AttackViewHolder)
                    .bind(
                        item.view as Attack,
                        battleViewModel,
                        context,
                        battleFragment,
                        attackViewModel
                    )
        }
    }

    override fun getItemCount(): Int {
        return battleManager.getCountItem()
    }


}