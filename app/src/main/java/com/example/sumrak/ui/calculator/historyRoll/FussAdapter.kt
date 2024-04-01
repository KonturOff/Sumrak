package com.example.sumrak.ui.calculator.historyRoll

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.lists.HistoryRoll
import com.example.sumrak.Player
import com.example.sumrak.R
import com.example.sumrak.databinding.RecyclerHistoryRollFussBinding

class FussAdapter(private val clickListener: HistoryCalculatorFragment) : DelegateAdapterH<HistoryRoll,FussAdapter.FussViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup): FussViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_history_roll_fuss, parent, false)
        return FussViewHolder(view)
    }

    override fun onBindViewHolder(holder: FussViewHolder, item: HistoryRoll, position: Int) {
        holder.bind(item, position, clickListener)
    }

    class FussViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val viewBinding = RecyclerHistoryRollFussBinding.bind(itemView)


        fun bind(historyRoll: HistoryRoll, position: Int, clickListener: HistoryCalculatorFragment) {
            val resultFuss = historyRoll.textRoll.trim('[', ']').split(',').map { it.trim().toInt() }
            val oneCubFuss = resultFuss[0]
            val twoCubFuss = resultFuss[1]
            viewBinding.apply {
                tvResultSummRoll.text = "[$oneCubFuss]:[$twoCubFuss]"
                tvColorCheck.setBackgroundResource(getColorResultFuss(oneCubFuss, twoCubFuss))
                tvNamePers.text = Player.getInstance().getPlayerToId(historyRoll.player).namePlayer
                tvPersCheck.text = historyRoll.mode
                tvResultFuss.text = getResultFuss(oneCubFuss, twoCubFuss)

                btnReroll.setOnClickListener { clickListener.onRecyclerViewItemClick(position) }
            }

        }

        private fun getColorResultFuss(oneCub: Int, twoCub: Int) : Int{
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

        private fun getResultFuss(oneCub: Int, twoCub: Int) : String{
            return if (oneCub == twoCub){
                when(oneCub){
                    1-> "Светлая суета"
                    2-> "Светлая суета"
                    3-> "Сумрачная суета"
                    4-> "Сумрачная суета"
                    5-> "Темная суета"
                    6-> "Темная суета"
                    else ->""
                }
            } else{
                ""
            }
        }

    }


}