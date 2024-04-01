package com.example.sumrak.ui.calculator

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sumrak.ui.calculator.calculator.CalculatorFragment
import com.example.sumrak.ui.calculator.historyRoll.HirstoryCalculatorFragment

class MainCalculatorAdapter(
    fragmentActivity: MainCalculatorFragment
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> CalculatorFragment()
            else -> HirstoryCalculatorFragment()
        }
    }


}