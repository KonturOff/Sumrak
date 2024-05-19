package com.example.sumrak.ui.calculator.historyRoll

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.lists.HistoryRoll
import com.example.sumrak.Player
import com.example.sumrak.R
import com.example.sumrak.databinding.RecyclerHistoryRollInitiativeBinding

class InitiativeRVAdapter(private val clickListener: HistoryCalculatorFragment) : DelegateAdapterH<HistoryRoll, InitiativeRVAdapter.InitiativeRVViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup): InitiativeRVViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_history_roll_initiative, parent, false)
        return InitiativeRVViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: InitiativeRVViewHolder,
        item: HistoryRoll,
        position: Int
    ) {
        holder.bind(item, position, clickListener)
    }

    class InitiativeRVViewHolder( itemView: View): RecyclerView.ViewHolder(itemView) {
        val viewBinding = RecyclerHistoryRollInitiativeBinding.bind(itemView)

        fun bind(item: HistoryRoll, position: Int, clickListener: HistoryCalculatorFragment) {
            viewBinding.apply {
                resultSumRoll.text = item.resultRoll
                textNamePers.text = Player.getInstance().getPlayerToId(item.player).namePlayer
                textPersCheck.text = item.mode
                btnReroll.setOnClickListener { clickListener.onRecyclerViewItemClick(position) }
                resultSumRoll.setOnClickListener { println(item) }

                textResultInitiative.text = "Инициатива: ${getInitiative(item)}"
            }
        }

        private fun getInitiative(item: HistoryRoll) : String{
            return (item.parameter * 2 - item.resultRoll.toInt() + item.bonus).toString()
        }

    }
}