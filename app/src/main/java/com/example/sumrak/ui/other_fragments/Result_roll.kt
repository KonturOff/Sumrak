package com.example.sumrak.ui.other_fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.sumrak.Lists.HistoryRollManager
import com.example.sumrak.Player
import com.example.sumrak.R
import com.example.sumrak.R.color.*
import com.example.sumrak.databinding.FragmentResultRollBinding
import com.example.sumrak.ui.calculator.MainCalculatorFragment
import com.example.sumrak.ui.calculator.calculator.CalculatorFragment
import kotlin.properties.Delegates


class Result_roll : Fragment() {

    private lateinit var b : FragmentResultRollBinding

    private var result_roll : String? = ""
    private var cube : String? = ""
    private var text_roll : String? = ""
    private var color : String? = ""
    private var position : Int = 0
    private var mode = ""
    private var value : Int = 0


    private lateinit var myInterfaceResult_roll: InterfaceResulRoll

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myInterfaceResult_roll = context as InterfaceResulRoll
    }
    interface InterfaceResulRoll{
        fun touch_screen()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        result_roll = arguments?.getString("result_roll")
        cube = arguments?.getString("cube")
        text_roll = arguments?.getString("text_roll")
        color = arguments?.getString("color")
        position = requireArguments().getInt("position")
        mode = HistoryRollManager.getInstance().getItem(position).mode



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentResultRollBinding.inflate(layoutInflater)



        // инициализируем переменную которая будет хранить информацию о цвете и устанавливаем цвет фрагменту

        b.resultRollLayout.setBackgroundResource(get_color_cube(color))

        b.resultSumRoll.text = result_roll
        b.textCube.text = cube
        b.textResultCube.text = text_roll

        b.resultRollLayout.setOnClickListener { myInterfaceResult_roll.touch_screen() }

        //Проверяем в каком режиме совершен бросок

        when(mode){
            "Калькулятор" -> modeOne()
            "Проверка Суеты" -> chekFuss()
            "Проверка Инициативы" -> initiativeMode()
            else -> checkValue(mode)
        }



        return b.root
    }

    private fun chekFuss() {
        modeTwo()
        b.checkResult.text = mode
        b.rateResult.text = ""
        b.resultValue.text = ""
        val resultFuss = text_roll!!.trim('[', ']').split(',').map { it.trim().toInt() }
        val oneCubFuss = resultFuss[0]
        val twoCubFuss = resultFuss[1]

        b.resultSumRoll.text = "[$oneCubFuss]:[$twoCubFuss]"

        if (oneCubFuss == twoCubFuss){
            when(oneCubFuss){
                1-> b.resultValue.text = "Светлая СУЕТА!"
                2-> b.resultValue.text = "Светлая СУЕТА!!"
                3-> b.resultValue.text = "Сумрачная СУЕТА!"
                4-> b.resultValue.text = "Сумрачная СУЕТА!"
                5-> b.resultValue.text = "Темная СУЕТА!"
                6-> {b.resultValue.text = "Темная СУЕТА!!"
                    b.rateResult.text = "Ну ты даешь...."
                }

            }
        }
    }

    //Проверка инициативы
    fun initiativeMode(){
        modeTwo()
        val item = HistoryRollManager.getInstance().getItem(position)
        b.resultSumRoll.text = item.result_roll
        b.resultValue.text = ""
        b.checkResult.text = "Проверка Инициативы"
        b.rateResult.text = "Инициатива: ${item.parameter*2 - item.result_roll.toInt()}"
    }


    //режим проверки Характеристик
    fun checkValue(mode: String){
        val player = position?.let { HistoryRollManager.getInstance().getItem(it).player }!!
        modeTwo()
        b.checkResult.text = mode
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
                b.checkResult.text = "Убывающий Тест"
            }
        }

        if (HistoryRollManager.getInstance().getItem(position).result_roll.toInt() == 1){
            b.resultValue.text = "КРИТИЧЕСКИЙ УСПЕХ!"
            b.rateResult.text = ""
        }
        else if (HistoryRollManager.getInstance().getItem(position).result_roll.toInt() == 20){
            b.resultValue.text = "КРИТИЧЕСКИЙ ПРОВАЛ"
            b.rateResult.text = ""
        }
        else if (HistoryRollManager.getInstance().getItem(position).result_roll.toInt()<= value) {
            b.resultValue.text = "Успех"
            b.rateResult.text = "Степени успеха: " + (value - HistoryRollManager.getInstance().getItem(position).result_roll.toInt())
        }
        else{
            b.resultValue.text = "Провал"
            b.rateResult.text = "Степени провала: " + (HistoryRollManager.getInstance().getItem(position).result_roll.toInt() - value)
        }
    }
    // Переключаем визуал под броски В калькуляторе и тд
    fun modeOne(){
        b.modeOne.visibility = View.VISIBLE
        b.modeTwo.visibility = View.GONE
    }
    // Переключаем визуал в режим проверки характеристик
    fun modeTwo(){
        b.modeOne.visibility = View.GONE
        b.modeTwo.visibility = View.VISIBLE
    }

    fun get_color_cube(max_cube: String?): Int {
        return when (max_cube) {
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
        fun newInstance() = Result_roll()
    }




}