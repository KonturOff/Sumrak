package com.example.sumrak.ui.calculator.calculator

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.sumrak.Player
import com.example.sumrak.databinding.FragmentCalculatorBinding
import com.example.sumrak.ui.calculator.CalculatorViewModel
import java.text.FieldPosition

class CalculatorFragment() : Fragment(), View.OnTouchListener {

    companion object {
        fun newInstance() = CalculatorFragment()
    }
    private lateinit var b: FragmentCalculatorBinding
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
        b = FragmentCalculatorBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(CalculatorViewModel::class.java)

        //Создаем наблюдателя для изменения текста в дисплее при обновлении переменной
        viewModel.lv.observe(viewLifecycleOwner, Observer {
            b.tvDisplay.text = it
            b.btnRoll.isEnabled = viewModel.enableRollIfCub(it)
        })

        b.btnD100.setOnClickListener { viewModel.add_cube("d100") }
        b.btnD20.setOnClickListener { viewModel.add_cube("d20") }
        b.btnD12.setOnClickListener { viewModel.add_cube("d12") }
        b.btnD10.setOnClickListener { viewModel.add_cube("d10") }
        b.btnD8.setOnClickListener { viewModel.add_cube("d8") }
        b.btnD6.setOnClickListener { viewModel.add_cube("d6") }
        b.btnD5.setOnClickListener { viewModel.add_cube("d5") }
        b.btnD4.setOnClickListener { viewModel.add_cube("d4") }
        b.btnD3.setOnClickListener { viewModel.add_cube("d3") }


        b.btnNum0.setOnClickListener { viewModel.add_symbol("0") }
        b.btnNum1.setOnClickListener { viewModel.add_symbol("1") }
        b.btnNum2.setOnClickListener { viewModel.add_symbol("2") }
        b.btnNum3.setOnClickListener { viewModel.add_symbol("3") }
        b.btnNum4.setOnClickListener { viewModel.add_symbol("4") }
        b.btnNum5.setOnClickListener { viewModel.add_symbol("5") }
        b.btnNum6.setOnClickListener { viewModel.add_symbol("6") }
        b.btnNum7.setOnClickListener { viewModel.add_symbol("7") }
        b.btnNum8.setOnClickListener { viewModel.add_symbol("8") }
        b.btnNum9.setOnClickListener { viewModel.add_symbol("9") }
        b.btnNumPlus.setOnClickListener { viewModel.add_symbol("+") }
        b.btnNumMinus.setOnClickListener { viewModel.add_symbol("-") }

        b.btnClear.setOnClickListener { viewModel.clear() }
        b.btnRoll.setOnClickListener {
            // если дисплей не пустой
                if (b.tvDisplay.text != ""){
                    //если в дисплее в конце стоит оператор, убираем его и запускаем функцию
                    if (b.tvDisplay.text.endsWith("+") || b.tvDisplay.text.endsWith("-")){
                        b.tvDisplay.text = b.tvDisplay.text.dropLast(1)
                    }
                    myInterface.get_result_roll(b.tvDisplay.text.toString(), Player.getInstance().getIdActivePlayer(),"Калькулятор", 0,null)
                }
            }
        b.btnDelete.setOnClickListener { viewModel.delete_symdol() }
        b.container.setOnClickListener{myInterface.touch_screen()}



        return b.root
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        System.out.println("попали")
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
                System.out.println("дошли")
            }
        }
        return true
    }


}