package com.example.sumrak.ui.calculator.historyRoll

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.Lists.HistoryRoll
import com.example.sumrak.Player
import com.example.sumrak.R
import com.example.sumrak.databinding.RecyclerHistoryRollBinding

class OtherAdapter(private val clickListener: HirstoryCalculatorFragment) : DelegateAdapterH<HistoryRoll, OtherAdapter.OtherViewHolder>() {



    //interface RecyclerViewClickListener {
    //    fun onRecyclerViewItemClick(view: View, position: Int)
    //    fun getInfoHistoryCard(view: View, position: Int)
    //}


    override fun onCreateViewHolder(parent: ViewGroup): OtherAdapter.OtherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_history_roll, parent, false)
        return OtherViewHolder(view, clickListener)
    }



    override fun onBindViewHolder(holder: OtherViewHolder, item: HistoryRoll, position: Int) {
        holder.bind(item, position, clickListener)
    }

    class OtherViewHolder(itemView: View, clickListener: HirstoryCalculatorFragment) : RecyclerView.ViewHolder(itemView) {

        val b = RecyclerHistoryRollBinding.bind(itemView)
        fun bind(
            historyRoll: HistoryRoll,
            position: Int,
            clickListener: HirstoryCalculatorFragment
        ) {
            b.resultSumRoll.text = historyRoll.result_roll
            b.textResultCube.text = get_result_text(historyRoll.parameter, historyRoll.result_roll)

            b.textNamePers.text = Player.getInstance().getPlayerToId(historyRoll.player).name_player
            if (historyRoll.mode.contains("Убывающий Тест")){
                b.textParametr.text = "Критерий успеха: ${historyRoll.parameter}"
                b.textPersCheck.text = "Убывающий Тест"
                b.textResultCube.text = "Предмет: " + historyRoll.mode.substring(14)
            }
            else{
                b.textParametr.text= ""
                b.textPersCheck.text = historyRoll.mode
            }
            b.colorCheck.setBackgroundResource(get_color_check(historyRoll.parameter, historyRoll.result_roll))
            b.colorRecyclerRoll.setBackgroundResource(get_color_cube(historyRoll.max_cube))

            b.btnReroll.setOnClickListener {clickListener.onRecyclerViewItemClick(position) }
            b.resultSumRoll.setOnClickListener { println(historyRoll) }

        }

        private fun get_result_text(value: Int, resultRoll: String): String{
            return if (resultRoll.toInt() <= value){
                "Степени успеха: ${value - resultRoll.toInt()}"
            }
            else{
                "Степени провала: ${resultRoll.toInt() - value}"
            }
        }

        private fun get_color_check(value: Int, resultRoll: String): Int {

            val color: Int
            if (value == -1){
                color = (R.color.d3)
            }
            else if(resultRoll.toInt()<= value){
                color = (R.color.d10)
            }
            else{
                color = (R.color.d4)
            }

            return color


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