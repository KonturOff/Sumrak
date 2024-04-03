package com.example.sumrak.ui.other_fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.sumrak.lists.HistoryRollManager
import com.example.sumrak.Player
import com.example.sumrak.R
import com.example.sumrak.databinding.FragmentResultRollBinding


class ResultRoll : Fragment() {

    private var viewBinding : FragmentResultRollBinding? = null

    private var resultRoll : String? = ""
    private var cube : String? = ""
    private var textRoll : String? = ""
    private var color : String? = ""
    private var position : Int = 0
    private var mode = ""
    private var value : Int = 0


    private var myInterfaceResultRoll: InterfaceResulRoll? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myInterfaceResultRoll = context as InterfaceResulRoll
    }
    interface InterfaceResulRoll{
        fun touch_screen()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultRoll = arguments?.getString("result_roll")
        cube = arguments?.getString("cube")
        textRoll = arguments?.getString("text_roll")
        color = arguments?.getString("color")
        position = requireArguments().getInt("position")
        mode = HistoryRollManager.getInstance().getItem(position).mode
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentResultRollBinding.inflate(layoutInflater)

        // инициализируем переменную которая будет хранить информацию о цвете и устанавливаем цвет фрагменту

        viewBinding?.apply {
            resultRollLayout.setBackgroundResource(getСolorСube(color))
            resultSumRoll.text = resultRoll
            textCube.text = cube
            textResultCube.text = textRoll
            resultRollLayout.setOnClickListener { myInterfaceResultRoll?.touch_screen() }
        }

        //Проверяем в каком режиме совершен бросок

        when(mode){
            "Калькулятор" -> modeOne()
            "Проверка Суеты" -> checkFuss()
            "Проверка Инициативы" -> initiativeMode()
            else -> checkValue(mode)
        }

        return viewBinding?.root
    }

    private fun checkFuss() {
        modeTwo()
        viewBinding?.apply {
            checkResult.text = mode
            rateResult.text = ""
            resultValue.text = ""
        }
        val resultFuss = textRoll!!.trim('[', ']').split(',').map { it.trim().toInt() }
        val oneCubFuss = resultFuss[0]
        val twoCubFuss = resultFuss[1]

        viewBinding?.resultSumRoll?.text = "[$oneCubFuss]:[$twoCubFuss]"

        if (oneCubFuss == twoCubFuss){
            when(oneCubFuss){
                1-> viewBinding?.resultValue?.text = "Светлая СУЕТА!"
                2-> viewBinding?.resultValue?.text = "Светлая СУЕТА!!"
                3-> viewBinding?.resultValue?.text = "Сумрачная СУЕТА!"
                4-> viewBinding?.resultValue?.text = "Сумрачная СУЕТА!"
                5-> viewBinding?.resultValue?.text = "Темная СУЕТА!"
                6-> {
                    viewBinding?.resultValue?.text = "Темная СУЕТА!!"
                    viewBinding?.rateResult?.text = "Ну ты даешь...."
                }

            }
        }
    }

    //Проверка инициативы
    private fun initiativeMode(){
        modeTwo()
        val item = HistoryRollManager.getInstance().getItem(position)
        viewBinding?.apply {
            resultSumRoll.text = item.resultRoll
            resultValue.text = ""
            checkResult.text = "Проверка Инициативы"
            rateResult.text = "Инициатива: ${item.parameter * 2 - item.resultRoll.toInt()}"
        }
    }


    //режим проверки Характеристик
    private fun checkValue(mode: String){
        val player = position.let { HistoryRollManager.getInstance().getItem(it).player }
        modeTwo()
        viewBinding?.checkResult?.text = mode
        when(mode){
            "Проверка Дальнего боя" -> value = Player.getInstance().getPlayerToId(player).db
            "Проверка Ближнего боя" -> value = Player.getInstance().getPlayerToId(player).bb
            "Проверка Силы" -> value = Player.getInstance().getPlayerToId(player).power
            "Проверка Ловкости" -> value = Player.getInstance().getPlayerToId(player).dexterity
            "Проверка Воли" -> value = Player.getInstance().getPlayerToId(player).volition
            "Проверка Стойкости" -> value = Player.getInstance().getPlayerToId(player).endurance
            "Проверка Интелекта" -> value = Player.getInstance().getPlayerToId(player).intelect
            "Проверка Сообразительности" -> value = Player.getInstance().getPlayerToId(player).insihgt
            "Проверка Наблюдательности" -> value = Player.getInstance().getPlayerToId(player).observation
            "Проверка Харизмы" -> value = Player.getInstance().getPlayerToId(player).chsarisma
            "Проверка Инициативы" -> value = Player.getInstance().getPlayerToId(player).dexterity
            "Проверка Уклонения" -> value = HistoryRollManager.getInstance().getItem(position).parameter
            "Проверка Парирования" -> value = HistoryRollManager.getInstance().getItem(position).parameter
            else -> {
                value = HistoryRollManager.getInstance().getItem(position).parameter
                viewBinding?.checkResult?.text = "Убывающий Тест"
            }
        }

        when {
            HistoryRollManager.getInstance().getItem(position).resultRoll.toInt() == 1 -> {
                viewBinding?.apply {
                    resultValue.text = "КРИТИЧЕСКИЙ УСПЕХ!"
                    rateResult.text = ""
                }
            }
            HistoryRollManager.getInstance().getItem(position).resultRoll.toInt() == 20 -> {
                viewBinding?.apply {
                    resultValue.text = "КРИТИЧЕСКИЙ ПРОВАЛ"
                    rateResult.text = ""
                }
            }
            HistoryRollManager.getInstance().getItem(position).resultRoll.toInt()<= value -> {
                viewBinding?.apply {
                    resultValue.text = "Успех"
                    rateResult.text =
                        "Степени успеха: " + (value - HistoryRollManager.getInstance()
                            .getItem(position).resultRoll.toInt())
                }
            }
            else -> {
                viewBinding?.apply {
                    resultValue.text = "Провал"
                    rateResult.text =
                        "Степени провала: " + (HistoryRollManager.getInstance()
                            .getItem(position).resultRoll.toInt() - value)
                }
            }
        }
    }
    // Переключаем визуал под броски В калькуляторе и тд
    private fun modeOne(){
        viewBinding?.apply {
            modeOne.isVisible = true
            modeTwo.isVisible = false
        }
    }
    // Переключаем визуал в режим проверки характеристик
    private fun modeTwo(){
        viewBinding?.apply {
            modeOne.isVisible = false
            modeTwo.isVisible = true
        }
    }

    private fun getСolorСube(maxСube: String?): Int {
        return when (maxСube) {
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

    companion object{
        @JvmStatic
        fun newInstance() = ResultRoll()
    }
}