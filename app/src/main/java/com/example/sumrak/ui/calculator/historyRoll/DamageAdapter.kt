package com.example.sumrak.ui.calculator.historyRoll

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.Player
import com.example.sumrak.R
import com.example.sumrak.databinding.RecyclerHistoryRollDamageBinding
import com.example.sumrak.lists.HistoryRoll

class DamageAdapter(private val clickListener: HistoryCalculatorClickListener) : DelegateAdapterH<HistoryRoll,DamageAdapter.DamageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup): DamageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_history_roll_damage, parent, false)
        return DamageViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: DamageViewHolder,
        item: HistoryRoll,
        position: Int
    ) {
        holder.bind(item, position, clickListener)
    }

    class DamageViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val viewBinding = RecyclerHistoryRollDamageBinding.bind(itemView)
        fun bind(historyRoll: HistoryRoll, position: Int, clickListener: HistoryCalculatorClickListener) {
            viewBinding.apply {
                resultSumRoll.text = historyRoll.resultRoll
                textNamePers.text = Player.getInstance().getPlayerToId(historyRoll.player).namePlayer
                colorRecyclerRoll.setBackgroundResource(getColorCube(historyRoll.maxCube))
                textPersCheck.text = historyRoll.mode

                textParametrWeapon.text = historyRoll.weapon!!.name
                textValueDamage.text = historyRoll.cube + ": " + historyRoll.textRoll

                btnReroll.setOnClickListener { clickListener.onRecyclerViewItemClick(position) }

            }

        }

        private fun getColorCube(maxCube: String?): Int {
            return when (maxCube){
                "3" -> (R.color.d3)
                "4" -> (R.color.d4)
                "5" -> (R.color.d5)
                "6" -> (R.color.d6)
                "8" -> (R.color.d8)
                "10" -> (R.color.d10)
                "12" -> (R.color.d12)
                "20" -> (R.color.d20)
                "100" -> (R.color.d100)
                else -> (R.color.d20)
            }
        }

    }
}