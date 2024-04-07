package com.example.sumrak.ui.calculator.historyRoll

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.lists.HistoryRoll
import com.example.sumrak.Player
import com.example.sumrak.R
import com.example.sumrak.databinding.RecyclerHistoryRollBinding

// TODO передавать фрагмент в конструктор какого0либо класса - очень плохая идея.
//  Лучше использовать коллбэки или прослойки в виде интерфейсов
//  Вот тут пример реализации
class OtherAdapter(
    private val clickListener: HistoryCalculatorClickListener
) : DelegateAdapterH<HistoryRoll, OtherAdapter.OtherViewHolder>() {

    //interface RecyclerViewClickListener {
    //    fun onRecyclerViewItemClick(view: View, position: Int)
    //    fun getInfoHistoryCard(view: View, position: Int)
    //}

    override fun onCreateViewHolder(parent: ViewGroup): OtherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_history_roll, parent, false)
        return OtherViewHolder(view)
    }

    override fun onBindViewHolder(holder: OtherViewHolder, item: HistoryRoll, position: Int) {
        holder.bind(item, position, clickListener)
    }

    class OtherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val viewBinding = RecyclerHistoryRollBinding.bind(itemView)
        fun bind(
            historyRoll: HistoryRoll,
            position: Int,
            clickListener: HistoryCalculatorClickListener
        ) {
            viewBinding.apply {
                resultSumRoll.text = historyRoll.resultRoll
                textResultCube.text = getResultText(historyRoll.parameter, historyRoll.resultRoll)

                textNamePers.text = Player.getInstance().getPlayerToId(historyRoll.player).namePlayer
                if (historyRoll.mode.contains("Убывающий Тест")){
                    textParametr.text = "Критерий успеха: ${historyRoll.parameter}"
                    textPersCheck.text = "Убывающий Тест"
                    textResultCube.text = "Предмет: " + historyRoll.mode.substring(14)
                }
                else if (historyRoll.mode == "Проверка Уклонения" || historyRoll.mode == "Проверка Парирования"){
                    textParametr.text = "Критерий успеха: ${historyRoll.parameter}"
                    textPersCheck.text = historyRoll.mode
                }
                else{
                    textParametr.text= ""
                    textPersCheck.text = historyRoll.mode
                }
                colorCheck.setBackgroundResource(getColorCheck(historyRoll.parameter, historyRoll.resultRoll))
                colorRecyclerRoll.setBackgroundResource(getColorCube(historyRoll.maxCube))

                btnReroll.setOnClickListener {clickListener.onRecyclerViewItemClick(position) }
                resultSumRoll.setOnClickListener { println(historyRoll) }
            }
        }

        private fun getResultText(value: Int, resultRoll: String): String{
            return if (resultRoll.toInt() <= value){
                "Степени успеха: ${value - resultRoll.toInt()}"
            }
            else{
                "Степени провала: ${resultRoll.toInt() - value}"
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