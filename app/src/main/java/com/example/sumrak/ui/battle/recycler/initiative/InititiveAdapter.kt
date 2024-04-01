package com.example.sumrak.ui.battle.recycler.initiative

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.Player
import com.example.sumrak.R
import com.example.sumrak.databinding.MaketBattleInitiativeBinding
import com.example.sumrak.ui.battle.BattleViewModel
import com.example.sumrak.ui.battle.recycler.DelegateAdapterB

class InititiveAdapter(
    private val battleViewModel: BattleViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val context: Context,
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
        holder.bind(item, battleViewModel, lifecycleOwner, context, buttonClickListener)
    }

    class InitiativeViewHolder(
        itemView: View,
        viewModel: BattleViewModel,
        lifecycleOwner: LifecycleOwner,
        private val buttonClickListener: OnButtonClickListener
    ) : RecyclerView.ViewHolder(itemView) {
        val b = MaketBattleInitiativeBinding.bind(itemView)

        init {
            viewModel.getInitiativePlayer(Player.getInstance().getIdActivePlayer())
            viewModel.initiativeV.observe(lifecycleOwner, Observer {
                b.tvStep.text = it.step.toString()
                b.tvResultRoll.text = it.resultRoll.toString()
                b.tvResultInitiative.text = it.resultInitiative.toString()
            })
            viewModel.initiativeVisible.observe(lifecycleOwner, Observer {
                if (it){
                    b.initiativeModeVis.visibility = View.VISIBLE
                }
                else b.initiativeModeVis.visibility = View.GONE
            })
        }

        fun bind(
            item: Initiative,
            viewModel: BattleViewModel,
            lifecycleOwner: LifecycleOwner,
            context: Context,
            buttonClickListener: OnButtonClickListener


            ) {

            b.btnRollAllPers.isEnabled = Player.getInstance().getPlayerCount() != 1
            b.btnRollActivePers.setOnClickListener {
                this.buttonClickListener.rollInitiativeActivePlayer()
                viewModel.getInitiativePlayer(Player.getInstance().getIdActivePlayer())
            }
            b.btnRollAllPers.setOnClickListener {
                this.buttonClickListener.rollInitiativeAllPlayers()
                viewModel.getInitiativePlayer(Player.getInstance().getIdActivePlayer())
            }

            b.btnAddStep.setOnClickListener { viewModel.updateStepInitiative(1) }
            b.btnRemStep.setOnClickListener { viewModel.updateStepInitiative(-1) }
        }

    }
}