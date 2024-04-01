package com.example.sumrak.ui.calculator

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.sumrak.databinding.FragmentMainCalculatorBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainCalculatorFragment : Fragment() {
    private lateinit var b : FragmentMainCalculatorBinding

    lateinit var tabLayout : TabLayout
    lateinit var viewPager : ViewPager2

    private lateinit var myInterfaceCalculatorMain: Interface
    private lateinit var viewModel: CalculatorViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myInterfaceCalculatorMain = context as Interface
    }
    // Инициализируем интерфейс для работы с Активити
    interface Interface{
        fun touch_screen()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentMainCalculatorBinding.inflate(layoutInflater)

        b.pagerCalculator.adapter = MainCalculatorAdapter(this)

        TabLayoutMediator(b.tabLayout, b.pagerCalculator){
            tab, pos ->
            when (pos){
                0 -> tab.text = "Калькулятор"
                1 -> tab.text = "История бросков"
            }

        }.attach()
        b.pagerCalculator.setOnClickListener {myInterfaceCalculatorMain.touch_screen() }



        return b.root
    }




}