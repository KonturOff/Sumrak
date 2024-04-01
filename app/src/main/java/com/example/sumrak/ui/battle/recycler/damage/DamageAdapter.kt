package com.example.sumrak.ui.battle.recycler.damage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.R
import com.example.sumrak.databinding.MaketBattleDamageBinding
import com.example.sumrak.ui.battle.BattleViewModel
import com.example.sumrak.ui.battle.recycler.DelegateAdapterB


class DamageAdapter(
    private val battleViewModel: BattleViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val context: Context,
    private val battleFragment: buttonClick,
) : DelegateAdapterB<Damage, DamageAdapter.DamageViewHolder, BattleViewModel>() {

    interface buttonClick{
        fun rollDodgeActivePlayer()
        fun rollParryingActivePlayer()
    }


    override fun onCreateViewHolder(parent: ViewGroup): DamageAdapter.DamageViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.maket_battle_damage, parent, false)
        return DamageViewHolder(view, battleViewModel, lifecycleOwner, context, battleFragment)
    }

    override fun onBindViewHolder(holder: DamageAdapter.DamageViewHolder, item: Damage) {
        holder.bind(item, battleViewModel, lifecycleOwner, context, battleFragment)
    }

    class DamageViewHolder(
        itemView: View,
        battleViewModel: BattleViewModel,
        lifecycleOwner: LifecycleOwner,
        context: Context,
        private val battleFragment: buttonClick
    ): RecyclerView.ViewHolder(itemView) {
        val b = MaketBattleDamageBinding.bind(itemView)

        init {
            battleViewModel.playerV.observe(lifecycleOwner, Observer {
                b.tvDodgeValue.text = it.dodge.toString()
                b.tvParryingValue.text = it.parrying.toString()
                b.btnDodge.isEnabled = it.dodge > 0
                b.btnParrying.isEnabled = it.parrying > 0
            })
        }


        fun bind(
            item: Damage,
            battleViewModel: BattleViewModel,
            lifecycleOwner: LifecycleOwner,
            context: Context,
            battleFragment: buttonClick
        ) {

            b.btnAddPenetration.setOnClickListener {
                b.tvArmorPenetration.text = (b.tvArmorPenetration.text.toString().toInt() + 1).toString()
            }
            b.btnRemPenetratoin.setOnClickListener {
                if (b.tvArmorPenetration.text.toString().toInt()!=0){
                    b.tvArmorPenetration.text = (b.tvArmorPenetration.text.toString().toInt() - 1).toString()
                }
            }

            b.editDamageValue.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    resultDamage(battleViewModel, context)
                    return@setOnEditorActionListener true
                }
                false
            }

            b.btnDodge.setOnClickListener {
                this.battleFragment.rollDodgeActivePlayer()
                battleViewModel.degradeDodge()
            }

            b.btnParrying.setOnClickListener {
                this.battleFragment.rollParryingActivePlayer()
                battleViewModel.degradeDodge()
            }

            b.btnResultDamage.setOnClickListener {
                resultDamage(battleViewModel, context)

            }

            b.btnUpdateDodge.setOnClickListener { battleViewModel.updateDodgeParrying(0) }

            b.btnAddDodge.setOnClickListener { battleViewModel.updateDodgeParrying(1) }
            b.btnRemDodge.setOnClickListener { battleViewModel.updateDodgeParrying(-1) }

        }

        private fun resultDamage(battleViewModel: BattleViewModel, context: Context) {
            val damageString = b.editDamageValue.text.toString()
            var damage : Int = if (damageString != ""){
                damageString.toInt()
            } else 0
            val penetration = b.tvArmorPenetration.text.toString().toInt()
            battleViewModel.damageToPlayer(damage, penetration)
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm.isAcceptingText) {
                // Закрываем клавиатуру
                imm.hideSoftInputFromWindow(itemView.windowToken, 0)
            }
            b.editDamageValue.text.clear()
        }

    }
}