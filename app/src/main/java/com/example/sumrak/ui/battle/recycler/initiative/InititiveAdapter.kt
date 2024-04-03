package com.example.sumrak.ui.battle.recycler.initiative

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.Player
import com.example.sumrak.R
import com.example.sumrak.databinding.MaketBattleInitiativeBinding
import com.example.sumrak.ui.battle.BattleViewModel
import com.example.sumrak.ui.battle.recycler.DelegateAdapterB

class InititiveAdapter(
    private val battleViewModel: BattleViewModel,
    private val lifecycleOwner: LifecycleOwner,
//    private val context: Context,
    private val buttonClickListener: OnButtonClickListener
): DelegateAdapterB<Initiative, InititiveAdapter.InitiativeViewHolder, BattleViewModel>() {

    interface OnButtonClickListener {
        fun rollInitiativeActivePlayer()
        fun rollInitiativeAllPlayers()
    }


    override fun onCreateViewHolder(parent: ViewGroup): InitiativeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.maket_battle_initiative, parent, false)
        return InitiativeViewHolder(view, battleViewModel, lifecycleOwner, buttonClickListener)
    }

    override fun onBindViewHolder(holder: InitiativeViewHolder, item: Initiative) {
        holder.bind(
//            item,
            viewModel = battleViewModel,
//            lifecycleOwner,
//            context,
//            buttonClickListener
        )
    }

    class InitiativeViewHolder(
        itemView: View,
        viewModel: BattleViewModel,
        lifecycleOwner: LifecycleOwner,
        private val buttonClickListener: OnButtonClickListener
    ) : RecyclerView.ViewHolder(itemView) {
        private val viewBinding = MaketBattleInitiativeBinding.bind(itemView)

        init {
            viewModel.getInitiativePlayer(Player.getInstance().getIdActivePlayer())
            viewModel.initiativeV.observe(lifecycleOwner) {
                viewBinding.apply {
                    tvStep.text = it.step.toString()
                    tvResultRoll.text = it.resultRoll.toString()
                    tvResultInitiative.text = it.resultInitiative.toString()
                }
            }
            viewModel.initiativeVisible.observe(lifecycleOwner) {
                viewBinding.initiativeModeVis.isVisible = it
            }
        }

        fun bind(
//            item: Initiative,
            viewModel: BattleViewModel,
//            lifecycleOwner: LifecycleOwner,
//            context: Context,
//            buttonClickListener: OnButtonClickListener
            ) {
            viewBinding.apply {
                btnRollAllPers.isEnabled = Player.getInstance().getPlayerCount() != 1
                btnRollActivePers.setOnClickListener {
                    this@InitiativeViewHolder.buttonClickListener.rollInitiativeActivePlayer()
                    viewModel.getInitiativePlayer(Player.getInstance().getIdActivePlayer())
                }
                btnRollAllPers.setOnClickListener {
                    this@InitiativeViewHolder.buttonClickListener.rollInitiativeAllPlayers()
                    viewModel.getInitiativePlayer(Player.getInstance().getIdActivePlayer())
                }

                btnAddStep.setOnClickListener { viewModel.updateStepInitiative(1) }
                btnRemStep.setOnClickListener { viewModel.updateStepInitiative(-1) }
            }
        }

    }
}