package com.example.sumrak.ui.calculator.historyRoll

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.Lists.HistoryRoll
import com.example.sumrak.Player
import com.example.sumrak.R
import com.example.sumrak.databinding.RecyclerHistoryRollInitiativeBinding

class InitiativeRVAdapter(private val clickListener: HirstoryCalculatorFragment) : DelegateAdapterH<HistoryRoll, InitiativeRVAdapter.InitiativeRVViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup): InitiativeRVAdapter.InitiativeRVViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_history_roll_initiative, parent, false)
        return InitiativeRVViewHolder(view)
    }



    override fun onBindViewHolder(
        holder: InitiativeRVAdapter.InitiativeRVViewHolder,
        item: HistoryRoll,
        position: Int
    ) {
        holder.bind(item, position, clickListener)
    }

    class InitiativeRVViewHolder( itemView: View): RecyclerView.ViewHolder(itemView) {
        val b = RecyclerHistoryRollInitiativeBinding.bind(itemView)

        fun bind(item: HistoryRoll, position: Int, clickListener: HirstoryCalculatorFragment) {
            b.resultSumRoll.text = item.result_roll
            b.textNamePers.text = Player.getInstance().getPlayerToId(item.player).name_player
            b.textPersCheck.text = item.mode
            b.btnReroll.setOnClickListener { clickListener.onRecyclerViewItemClick(position) }
            b.resultSumRoll.setOnClickListener { println(item) }

            b.textResultInitiative.text = "Инициатива: ${get_initiative(item)}"
        }

        fun get_initiative(item: HistoryRoll) : String{
            return (item.parameter*2 - item.result_roll.toInt()).toString()
        }

    }
}