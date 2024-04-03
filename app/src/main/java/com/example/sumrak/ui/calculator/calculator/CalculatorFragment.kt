package com.example.sumrak.ui.calculator.calculator

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.example.sumrak.Player
import com.example.sumrak.databinding.FragmentCalculatorBinding
import com.example.sumrak.ui.calculator.CalculatorViewModel

class CalculatorFragment : Fragment(), View.OnTouchListener {

    companion object {
        fun newInstance() = CalculatorFragment()
    }
    private var viewBinding: FragmentCalculatorBinding? = null
    private lateinit var viewModel: CalculatorViewModel
    private lateinit var myInterface: Interface

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myInterface = context as Interface
    }
    // Инициализируем интерфейс для работы с Активити
    interface Interface{
        fun get_result_roll(text: String, player: Int, mode : String, param : Int, position: Int?)
        fun touch_screen()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentCalculatorBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(CalculatorViewModel::class.java)

        //Создаем наблюдателя для изменения текста в дисплее при обновлении переменной
        viewModel.lv.observe(viewLifecycleOwner) {
            viewBinding?.tvDisplay?.text = it
            viewBinding?.btnRoll?.isEnabled = viewModel.enableRollIfCub(it)
        }

        viewBinding?.apply {
            btnD100.setOnClickListener { viewModel.addCube("d100") }
            btnD20.setOnClickListener { viewModel.addCube("d20") }
            btnD12.setOnClickListener { viewModel.addCube("d12") }
            btnD10.setOnClickListener { viewModel.addCube("d10") }
            btnD8.setOnClickListener { viewModel.addCube("d8") }
            btnD6.setOnClickListener { viewModel.addCube("d6") }
            btnD5.setOnClickListener { viewModel.addCube("d5") }
            btnD4.setOnClickListener { viewModel.addCube("d4") }
            btnD3.setOnClickListener { viewModel.addCube("d3") }


            btnNum0.setOnClickListener { viewModel.addSymbol("0") }
            btnNum1.setOnClickListener { viewModel.addSymbol("1") }
            btnNum2.setOnClickListener { viewModel.addSymbol("2") }
            btnNum3.setOnClickListener { viewModel.addSymbol("3") }
            btnNum4.setOnClickListener { viewModel.addSymbol("4") }
            btnNum5.setOnClickListener { viewModel.addSymbol("5") }
            btnNum6.setOnClickListener { viewModel.addSymbol("6") }
            btnNum7.setOnClickListener { viewModel.addSymbol("7") }
            btnNum8.setOnClickListener { viewModel.addSymbol("8") }
            btnNum9.setOnClickListener { viewModel.addSymbol("9") }
            btnNumPlus.setOnClickListener { viewModel.addSymbol("+") }
            btnNumMinus.setOnClickListener { viewModel.addSymbol("-") }

            btnClear.setOnClickListener { viewModel.clear() }
            btnRoll.setOnClickListener {
                // если дисплей не пустой
                if (tvDisplay.text != ""){
                    //если в дисплее в конце стоит оператор, убираем его и запускаем функцию
                    if (tvDisplay.text.endsWith("+") || tvDisplay.text.endsWith("-")){
                        tvDisplay.text = tvDisplay.text.dropLast(1)
                    }
                    myInterface.get_result_roll(tvDisplay.text.toString(), Player.getInstance().getIdActivePlayer(),"Калькулятор", 0,null)
                }
            }
            btnDelete.setOnClickListener { viewModel.deleteSymdol() }
            container?.setOnClickListener{myInterface.touch_screen()}
        }

        return viewBinding?.root
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        println("попали")
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                // Обработка нажатия на экран
            }
            MotionEvent.ACTION_MOVE -> {
                // Обработка перемещения пальца по экрану
            }
            MotionEvent.ACTION_UP -> {
                // Обработка отпускания пальца с экрана
                // удаляем фрагмент "Результат броска"
                println("дошли")
            }
        }
        return true
    }


}