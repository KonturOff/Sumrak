package com.example.sumrak.ui.calculator

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sumrak.databinding.FragmentMainCalculatorBinding
import com.google.android.material.tabs.TabLayoutMediator


class MainCalculatorFragment : Fragment() {
    private var viewBinding : FragmentMainCalculatorBinding? = null

//    lateinit var tabLayout : TabLayout
//    lateinit var viewPager : ViewPager2

    private var myInterfaceCalculatorMain: Interface? = null
//    private lateinit var viewModel: CalculatorViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myInterfaceCalculatorMain = context as Interface
    }
    // Инициализируем интерфейс для работы с Активити
    interface Interface{
        fun touch_screen()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMainCalculatorBinding.inflate(layoutInflater)
        viewBinding?.apply {
            pagerCalculator.adapter = MainCalculatorAdapter(this@MainCalculatorFragment)

            TabLayoutMediator(
                tabLayout,
                pagerCalculator
            ) {
                    tab, pos ->
                when (pos){
                    0 -> tab.text = "Калькулятор"
                    1 -> tab.text = "История бросков"
                }

            }.attach()

            pagerCalculator.setOnClickListener {myInterfaceCalculatorMain?.touch_screen() }
        }
        return viewBinding?.root
    }
}
