package com.example.sumrak.ui.calculator.historyRoll

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sumrak.lists.HistoryRollManager
import com.example.sumrak.databinding.FragmentHirstoryCalculatorBinding


class HistoryCalculatorFragment : Fragment(), HistoryCalculatorClickListener {
    private var viewBinding : FragmentHirstoryCalculatorBinding? = null
    private var adapter : HistoryAdapter? = null

    private var myInterfaceHistoryCalculatorFragment: Interface? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myInterfaceHistoryCalculatorFragment = context as Interface
    }

    // Инициализируем интерфейс для работы с Активити
    interface Interface{
        fun get_result_roll(cube: String, player: Int, mode : String, param : Int, position: Int?)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentHirstoryCalculatorBinding.inflate(layoutInflater)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = HistoryAdapter( this)
        init()
    }

    override fun onResume() {
        super.onResume()
        adapter = HistoryAdapter(this)
        init()
    }

    private fun init() {
        viewBinding?.apply {
            recyclerCalculatorHistory.layoutManager = LinearLayoutManager(requireContext())
            recyclerCalculatorHistory.adapter = adapter
        }
    }

    override fun onRecyclerViewItemClick(position: Int) {
        val player = HistoryRollManager.getInstance().getItem(position).player
        val mode = HistoryRollManager.getInstance().getItem(position).mode
        myInterfaceHistoryCalculatorFragment?.get_result_roll("0", player,mode,0,position)
    }
}
