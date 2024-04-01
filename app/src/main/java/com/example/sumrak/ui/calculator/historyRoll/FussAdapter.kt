package com.example.sumrak.ui.calculator.historyRoll

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.Lists.HistoryRoll
import com.example.sumrak.Player
import com.example.sumrak.R
import com.example.sumrak.databinding.RecyclerHistoryRollFussBinding
import com.example.sumrak.ui.calculator.CalculatorViewModel

class FussAdapter(private val clickListener: HirstoryCalculatorFragment) : DelegateAdapterH<HistoryRoll,FussAdapter.FussViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup): FussAdapter.FussViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_history_roll_fuss, parent, false)
        return FussViewHolder(view)
    }

    override fun onBindViewHolder(holder: FussViewHolder, item: HistoryRoll, position: Int) {
        holder.bind(item, position, clickListener)
    }

    class FussViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val b = RecyclerHistoryRollFussBinding.bind(itemView)


        fun bind(historyRoll: HistoryRoll, position: Int, clickListener: HirstoryCalculatorFragment) {
            val resultFuss = historyRoll.text_roll.trim('[', ']').split(',').map { it.trim().toInt() }
            val oneCubFuss = resultFuss[0]
            val twoCubFuss = resultFuss[1]
            b.tvResultSummRoll.text = "[$oneCubFuss]:[$twoCubFuss]"
            b.tvColorCheck.setBackgroundResource(get_color_result_fuss(oneCubFuss, twoCubFuss))
            b.tvNamePers.text = Player.getInstance().getPlayerToId(historyRoll.player).name_player
            b.tvPersCheck.text = historyRoll.mode
            b.tvResultFuss.text = get_result_fuss(oneCubFuss, twoCubFuss)

            b.btnReroll.setOnClickListener { clickListener.onRecyclerViewItemClick(position) }

        }

        private fun get_color_result_fuss(oneCub: Int, twoCub: Int) : Int{
            var color: Int = (R.color.white)
            if (oneCub == twoCub){
                when (oneCub){
                    1-> color = (R.color.gold)
                    2-> color = (R.color.gold)
                    3-> color = (R.color.sumrak_grey)
                    4-> color = (R.color.sumrak_grey)
                    5-> color = (R.color.black)
                    6-> color = (R.color.black)
                }
                return color
            }
            return color
        }

        private fun get_result_fuss(oneCub: Int, twoCub: Int) : String{
            if (oneCub == twoCub){
                return when(oneCub){
                    1-> "Светлая суета"
                    2-> "Светлая суета"
                    3-> "Сумрачная суета"
                    4-> "Сумрачная суета"
                    5-> "Темная суета"
                    6-> "Темная суета"
                    else ->""
                }
            }
            else{
                return ""
            }
        }

    }


}