package com.example.sumrak.ui.battle.recycler.damage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.R
import com.example.sumrak.databinding.MaketBattleDamageBinding
import com.example.sumrak.ui.battle.BattleViewModel
import com.example.sumrak.ui.battle.recycler.DelegateAdapterB


class DamageAdapter(
    private val battleViewModel: BattleViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val context: Context,
    private val battleFragment: ButtonClick,
) : DelegateAdapterB<Damage, DamageAdapter.DamageViewHolder, BattleViewModel>() {

    interface ButtonClick{
        fun rollDodgeActivePlayer()
        fun rollParryingActivePlayer()
    }


    override fun onCreateViewHolder(parent: ViewGroup): DamageViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.maket_battle_damage, parent, false)
        return DamageViewHolder(view, battleViewModel, lifecycleOwner, battleFragment)
    }

    override fun onBindViewHolder(holder: DamageViewHolder, item: Damage) {
        //holder.bind(item, battleViewModel, lifecycleOwner, context, battleFragment)
        holder.bind(
            battleViewModel = battleViewModel,
            context = context
        )
    }

    class DamageViewHolder(
        itemView: View,
        battleViewModel: BattleViewModel,
        lifecycleOwner: LifecycleOwner,
        private val battleFragment: ButtonClick
    ): RecyclerView.ViewHolder(itemView) {
        private val viewBinding = MaketBattleDamageBinding.bind(itemView)

        init {
            battleViewModel.playerV.observe(lifecycleOwner) {
                viewBinding.apply {
                    tvDodgeValue.text = it.dodge.toString()
                    tvParryingValue.text = it.parrying.toString()
                    btnDodge.isEnabled = it.dodge > 0
                    btnParrying.isEnabled = it.parrying > 0
                }
            }
        }


        fun bind(
//            item: Damage,
            battleViewModel: BattleViewModel,
//            lifecycleOwner: LifecycleOwner,
            context: Context,
//            battleFragment: ButtonClick
        ) {
            viewBinding.apply {
                btnAddPenetration.setOnClickListener {
                    tvArmorPenetration.text =
                        (tvArmorPenetration.text.toString().toInt() + 1).toString()
                }
                btnRemPenetratoin.setOnClickListener {
                    if (tvArmorPenetration.text.toString().toInt() != 0) {
                        tvArmorPenetration.text =
                            (tvArmorPenetration.text.toString().toInt() - 1).toString()
                    }
                }

                editDamageValue.setOnEditorActionListener { v, actionId, event ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        resultDamage(battleViewModel, context)
                        return@setOnEditorActionListener true
                    }
                    false
                }

                btnDodge.setOnClickListener {
                    this@DamageViewHolder.battleFragment.rollDodgeActivePlayer()
                    battleViewModel.degradeDodge()
                }

                btnParrying.setOnClickListener {
                    this@DamageViewHolder.battleFragment.rollParryingActivePlayer()
                    battleViewModel.degradeDodge()
                }

                btnResultDamage.setOnClickListener {
                    resultDamage(battleViewModel, context)

                }

                btnUpdateDodge.setOnClickListener { battleViewModel.updateDodgeParrying(0) }

                btnAddDodge.setOnClickListener { battleViewModel.updateDodgeParrying(1) }
                btnRemDodge.setOnClickListener { battleViewModel.updateDodgeParrying(-1) }
            }

        }

        private fun resultDamage(battleViewModel: BattleViewModel, context: Context) {
            val damageString = viewBinding.editDamageValue.text.toString()
            val damage : Int = if (damageString != ""){
                damageString.toInt()
            } else 0
            val penetration = viewBinding.tvArmorPenetration.text.toString().toInt()
            battleViewModel.damageToPlayer(damage, penetration)
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm.isAcceptingText) {
                // Закрываем клавиатуру
                imm.hideSoftInputFromWindow(itemView.windowToken, 0)
            }
            viewBinding.editDamageValue.text.clear()
        }

    }
}