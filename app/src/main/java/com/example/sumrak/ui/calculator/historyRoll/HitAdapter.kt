package com.example.sumrak.ui.calculator.historyRoll

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.Player
import com.example.sumrak.R
import com.example.sumrak.databinding.RecyclerHistoryRollHitBinding
import com.example.sumrak.lists.HistoryRoll

class HitAdapter(private val clickListener: HistoryCalculatorClickListener) : DelegateAdapterH<HistoryRoll,HitAdapter.HitViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup): HitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_history_roll_hit, parent, false)
        return HitViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: HitViewHolder,
        item: HistoryRoll,
        position: Int
    ) {
        holder.bind(item, position, clickListener)
    }

    class HitViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val viewBinding = RecyclerHistoryRollHitBinding.bind(itemView)
        fun bind(
            historyRoll: HistoryRoll,
            position: Int,
            clickListener: HistoryCalculatorClickListener
        ) {
            viewBinding.apply {
                val successfulHit = Player.getInstance().getSuccessfulHit(
                    historyRoll.resultRoll.toInt(),
                    historyRoll.parameter,
                    historyRoll.bonus,
                    historyRoll.change,
                    historyRoll.weapon!!.valueTest,
                    historyRoll.weapon.paired
                )
                resultSumRoll.text = historyRoll.resultRoll
                textNamePers.text = Player.getInstance().getPlayerToId(historyRoll.player).namePlayer
                textParametrWeapon.text = historyRoll.weapon!!.name
                if (historyRoll.weapon!!.paired){
                    textParametrWeapon.text = historyRoll.weapon!!.name + " x2"
                }
                textValueHit.text = "Количество попаданий: ${successfulHit}"
                if (historyRoll.bonus>0){
                    textPersCheck.text = "${historyRoll.mode} +${historyRoll.bonus}"
                }
                else if (historyRoll.bonus<0){
                    textPersCheck.text = "${historyRoll.mode} ${historyRoll.bonus}"
                }
                else{
                    textPersCheck.text = historyRoll.mode
                }
                colorCheck.setBackgroundResource(getColorCheck(historyRoll.parameter + historyRoll.bonus, historyRoll.resultRoll))
                btnReroll.setOnClickListener { clickListener.onRecyclerViewItemClick(position) }

                resultSumRoll.setOnClickListener { println(historyRoll) }
            }

        }
        private fun getColorCheck(value: Int, resultRoll: String): Int {
            return when {
                value == -1 -> {
                    (R.color.d3)
                }
                resultRoll.toInt() <= value -> {
                    (R.color.d10)
                }
                else -> {
                    (R.color.d4)
                }
            }
        }

    }
}