package com.example.sumrak.ui.calculator.historyRoll

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sumrak.Lists.HistoryRollManager
import com.example.sumrak.databinding.FragmentHirstoryCalculatorBinding
import com.example.sumrak.ui.calculator.CalculatorViewModel


class HirstoryCalculatorFragment : Fragment() {
    private lateinit var b : FragmentHirstoryCalculatorBinding
    private lateinit var adapter : HistoryAdapter

    private lateinit var myInterfaceHirstoryCalculatorFragment: Interface

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myInterfaceHirstoryCalculatorFragment = context as Interface
    }
    // Инициализируем интерфейс для работы с Активити
    interface Interface{
        fun get_result_roll(cube: String, player: Int, mode : String, param : Int, position: Int?)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentHirstoryCalculatorBinding.inflate(layoutInflater)



        return b.root
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

    override fun onPause() {
        super.onPause()
    }

    private fun init(){

        b.recyclerCalculatorHistory.layoutManager = LinearLayoutManager(requireContext())
        b.recyclerCalculatorHistory.adapter = adapter


    }
    fun onRecyclerViewItemClick(position: Int) {
        val player = HistoryRollManager.getInstance().getItem(position).player
        val mode = HistoryRollManager.getInstance().getItem(position).mode
        myInterfaceHirstoryCalculatorFragment.get_result_roll("0", player,mode,0,position)

        //

    }






}