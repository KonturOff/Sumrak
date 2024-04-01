package com.example.sumrak.ui.calculator.historyRoll

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.Lists.HistoryRoll
import com.example.sumrak.R
import com.example.sumrak.databinding.RecyclerHistoryRollCalculatorBinding
import com.example.sumrak.ui.calculator.CalculatorViewModel


class CalculatorAdaper(private val clickListener: HirstoryCalculatorFragment) : DelegateAdapterH<HistoryRoll, CalculatorAdaper.CalculatorViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup): CalculatorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_history_roll_calculator, parent, false)
        return CalculatorViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: CalculatorViewHolder,
        item: HistoryRoll,
        position: Int
    ) {
        holder.bind(item, position, clickListener)
    }

    class CalculatorViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val b = RecyclerHistoryRollCalculatorBinding.bind(itemView)
        fun bind(item: HistoryRoll, position: Int, clickListener: HirstoryCalculatorFragment ) {
            b.resultSumRoll.text = item.result_roll

            b.textCube.text = item.cube
            b.textResultCube.text = item.text_roll

            b.colorRecyclerRoll.setBackgroundResource(get_color_cube(item.max_cube))

            b.btnReroll.setOnClickListener { clickListener.onRecyclerViewItemClick(position) }

        }

        private fun get_color_cube(max_cube: String?): Int {
            return when (max_cube){
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