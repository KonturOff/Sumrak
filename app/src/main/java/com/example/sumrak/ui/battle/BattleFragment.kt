package com.example.sumrak.ui.battle

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sumrak.Player
import com.example.sumrak.databinding.FragmentBattleBinding
import com.example.sumrak.ui.battle.recycler.damage.Damage
import com.example.sumrak.ui.battle.recycler.damage.DamageAdapter
import com.example.sumrak.ui.battle.recycler.equipment.EquipmentB
import com.example.sumrak.ui.battle.recycler.equipment.item.EquipmentBItemAdapter
import com.example.sumrak.ui.battle.recycler.information.Information
import com.example.sumrak.ui.battle.recycler.initiative.Initiative
import com.example.sumrak.ui.battle.recycler.initiative.InititiveAdapter
import com.example.sumrak.ui.inventory.recycler.equipment.EquipmentViewModel

class BattleFragment : Fragment(), InititiveAdapter.OnButtonClickListener, DamageAdapter.buttonClick, EquipmentBItemAdapter.equipmentRollClick {


    companion object {
        fun newInstance() = BattleFragment()
    }

    private lateinit var b: FragmentBattleBinding
    private lateinit var myInterface: Interface
    private lateinit var viewModel: BattleViewModel
    private lateinit var equipmentViewModel: EquipmentViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myInterface = context as Interface
    }

    interface Interface{
        fun get_result_roll(text: String, player: Int, mode : String, param: Int, position: Int?)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        b = FragmentBattleBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(BattleViewModel::class.java)
        equipmentViewModel = ViewModelProvider(this).get(EquipmentViewModel::class.java)

        val battleManager = BattleManager.getInstance()
        //ВРЕМЕННОЕ РЕШЕНИЕ
        loadBattleView(battleManager)

        //equipmentViewModel.getEquipmentToIdPlayer(Player.getInstance().getIdActivePlayer())

        val layoutManager = LinearLayoutManager(context)
        b.recBattle.layoutManager = layoutManager

        val items = listOf(
            Information(),
            Damage(),
            Initiative())

        val battleAdapter = BattleAdapter(battleManager, viewLifecycleOwner, requireContext(), viewModel , this, equipmentViewModel)
        b.recBattle.adapter = battleAdapter

        val itemTouchHelperCallback = BattleItemTouchHelper(battleAdapter, requireContext())
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(b.recBattle)

        return b.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



    }

    fun loadBattleView(battleManager: BattleManager) {
        battleManager.clearList()
        battleManager.addItem(BattleItem(1,1,"Информация", 0,true, Information()))
        battleManager.addItem(BattleItem(1,1,"Получение Урона", 1,true, Damage()))
        battleManager.addItem(BattleItem(1,1,"Инициатива", 2,true, Initiative()))
        battleManager.addItem(BattleItem(1,1,"Снаряжение", 3,true, EquipmentB()))

    }

    override fun rollInitiativeActivePlayer() {
        myInterface.get_result_roll("d20", Player.getInstance().getIdActivePlayer(),"Проверка Инициативы", 0, null)
    }


    override fun rollInitiativeAllPlayers() {
        for (i in Player.getInstance().getIdPlayerToPlayerList()){
            myInterface.get_result_roll("d20", i,"Проверка Инициативы", 0,null)
        }
    }

    override fun rollDodgeActivePlayer(){
        myInterface.get_result_roll("d20", Player.getInstance().getIdActivePlayer(), "Проверка Уклонения", 0,null)
    }

    override fun rollParryingActivePlayer(){
        myInterface.get_result_roll("d20", Player.getInstance().getIdActivePlayer(), "Проверка Парирования", 0,null)
    }

    override fun rollEquipmentTest(mode: String, param: Int) {
        myInterface.get_result_roll("d20", Player.getInstance().getIdActivePlayer(), mode, param, null)
    }

}