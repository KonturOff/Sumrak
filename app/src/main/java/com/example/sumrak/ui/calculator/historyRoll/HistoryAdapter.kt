package com.example.sumrak.ui.calculator.historyRoll

import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.lists.HistoryRoll
import com.example.sumrak.lists.HistoryRollManager

// TODO передавать фрагмент в конструктор какого0либо класса - очень плохая идея.
//  Лучше использовать коллбэки или прослойки в виде интерфейсов
class HistoryAdapter(
    private val historyCalculatorFragment: HistoryCalculatorFragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val historyRollManager = HistoryRollManager.getInstance()

    init {
        historyRollManager.setAdapter(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            TYPE_CALCULATOR -> CalculatorAdapter(historyCalculatorFragment).onCreateViewHolder(parent)
            TYPE_FUSS_CHECK -> FussAdapter(historyCalculatorFragment).onCreateViewHolder(parent)
            TYPE_INITIATIVE -> InitiativeRVAdapter(historyCalculatorFragment).onCreateViewHolder(parent)
            else -> OtherAdapter(historyCalculatorFragment).onCreateViewHolder(parent)
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val historyRoll = historyRollManager.getItem(position)

        when (getItemViewType(position)) {
            TYPE_CALCULATOR -> (holder as CalculatorAdapter.CalculatorViewHolder).bind(historyRoll, position, historyCalculatorFragment)
            TYPE_FUSS_CHECK ->(holder as FussAdapter.FussViewHolder).bind(historyRoll, position, historyCalculatorFragment)
            TYPE_INITIATIVE -> (holder as InitiativeRVAdapter.InitiativeRVViewHolder).bind(historyRoll, position, historyCalculatorFragment)
            else ->  (holder as OtherAdapter.OtherViewHolder).bind(historyRoll, position, historyCalculatorFragment)
        }
    }

    override fun getItemCount(): Int {
        return historyRollManager.getItemCount()
    }

    override fun getItemViewType(position: Int): Int {
        val historyRoll = historyRollManager.getItem(position)
        return when (historyRoll.mode) {
            "Калькулятор" -> TYPE_CALCULATOR
            "Проверка Суеты" -> TYPE_FUSS_CHECK
            "Проверка Инициативы" -> TYPE_INITIATIVE
            else -> TYPE_DEFAULT
        }
    }

    companion object {
        private const val TYPE_DEFAULT = 0
        private const val TYPE_CALCULATOR = 1
        private const val TYPE_FUSS_CHECK = 2
        private const val TYPE_INITIATIVE = 3
    }



    inner class HistoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //private val binding: ViewDataBinding = DataBindingUtil.bind(itemView)!!
        //private val bFuss = RecyclerHistoryRollFussBinding.bind(itemView)
        //private val binding = RecyclerHistoryRollBinding.bind(itemView)

        fun bind(historyRoll: HistoryRoll, position: Int) {
            

            //when (getItemViewType(position)) {
            //    TYPE_FUSS_CHECK -> {
//
            //    }
//
            //    else ->{
            //        //val binding = binding as RecyclerHistoryRollBinding
            //        binding.resultSumRoll.text = historyRoll.result_roll
            //        binding.textCube.text = historyRoll.cube
            //        binding.textResultCube.text = historyRoll.text_roll
            //        if (historyRoll.mode.equals("Калькулятор")){
            //            binding.textPersCheck.text = ""
            //            binding.colorCheck.setBackgroundResource(get_color_check(historyRoll.player,historyRoll.result_roll,historyRoll.mode))
            //        }
            //        //else if (historyRoll.mode.equals("Проверка Суеты")){
            //        //    binding.textPersCheck.text =
            //        //        historyRoll.player.let { Player.getInstance().getPlayerToId(it).name_player } + " " + historyRoll.mode
            //        //    val resultFuss = historyRoll.text_roll.trim('[', ']').split(',').map { it.trim().toInt() }
            //        //    val oneCubFuss = resultFuss[0]
            //        //    val twoCubFuss = resultFuss[1]
            //        //    binding.resultSumRoll.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30.0F)
            //        //    binding.resultSumRoll.text = "[$oneCubFuss]:[$twoCubFuss]"
            //        //    binding.colorCheck.setBackgroundResource(get_color_result_fuss(oneCubFuss, twoCubFuss))
////
            //        //}
            //        else{
            //            binding.textPersCheck.text =
            //                historyRoll.player.let { Player.getInstance().getPlayerToId(it).name_player } + " " + historyRoll.mode
            //            binding.colorCheck.setBackgroundResource(get_color_check(historyRoll.player,historyRoll.result_roll,historyRoll.mode))
            //        }
//
            //        binding.colorRecyclerRoll.setBackgroundResource(get_color_cube(historyRoll.max_cube))
//
            //        //binding.btnReroll.setOnClickListener { clickListener.onRecyclerViewItemClick(it, position) }
            //        binding.resultSumRoll.setOnClickListener { clickListener.getInfoHistoryCard(it, position) }
            //        //binding.btnReroll.setOnClickListener { System.out.println(position) }
            //    }
            //}



        }

        //fun bindFuss(historyRoll: HistoryRoll, position: Int) {
        //    //val bFuss = binding as RecyclerHistoryRollFussBinding
        //    val resultFuss = historyRoll.text_roll.trim('[', ']').split(',').map { it.trim().toInt() }
        //    val oneCubFuss = resultFuss[0]
        //    val twoCubFuss = resultFuss[1]
        //    bFuss.tvResultSummRoll.text = "[$oneCubFuss]:[$twoCubFuss]"
        //    bFuss.tvColorCheck.setBackgroundResource(get_color_result_fuss(oneCubFuss, twoCubFuss))
        //    bFuss.tvNamePers.text = Player.getInstance().getPlayerToId(historyRoll.player).name_player
        //    bFuss.tvPersCheck.text = historyRoll.mode
        //    bFuss.tvResultFuss.text = get_result_fuss(oneCubFuss, twoCubFuss)
        //}

    }





}




