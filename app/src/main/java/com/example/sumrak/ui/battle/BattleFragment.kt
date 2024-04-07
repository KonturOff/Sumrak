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
import com.example.sumrak.ui.battle.recycler.atack.Attack
import com.example.sumrak.ui.battle.recycler.atack.AttackAdapter
import com.example.sumrak.ui.battle.recycler.atack.AttackViewModel
import com.example.sumrak.ui.battle.recycler.damage.Damage
import com.example.sumrak.ui.battle.recycler.damage.DamageAdapter
import com.example.sumrak.ui.battle.recycler.equipment.EquipmentB
import com.example.sumrak.ui.battle.recycler.equipment.EquipmentBViewModel
import com.example.sumrak.ui.battle.recycler.equipment.item.EquipmentBItemAdapter
import com.example.sumrak.ui.battle.recycler.information.Information
import com.example.sumrak.ui.battle.recycler.initiative.Initiative
import com.example.sumrak.ui.battle.recycler.initiative.InititiveAdapter
import com.example.sumrak.ui.inventory.recycler.arsenal.item.ArsenalItem
import com.example.sumrak.ui.inventory.recycler.equipment.EquipmentViewModel

class BattleFragment :
    Fragment(),
    InititiveAdapter.OnButtonClickListener,
    DamageAdapter.ButtonClick,
    EquipmentBItemAdapter.equipmentRollClick,
    AttackAdapter.attackClick
{


    companion object {
        fun newInstance() = BattleFragment()
    }

    private lateinit var viewBinding: FragmentBattleBinding
    private lateinit var myInterface: Interface
    private lateinit var viewModel: BattleViewModel
    private lateinit var equipmentBViewModel: EquipmentBViewModel
    private lateinit var attackViewModel: AttackViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myInterface = context as Interface
    }

    interface Interface{
        fun get_result_roll(text: String, player: Int, mode : String, param: Int, chenge: Int, bonus:Int,  position: Int?, weapon: ArsenalItem?)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewBinding = FragmentBattleBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(BattleViewModel::class.java)
        equipmentBViewModel = ViewModelProvider(this).get(EquipmentBViewModel::class.java)
        attackViewModel = ViewModelProvider(this).get(AttackViewModel::class.java)

        val battleManager = BattleManager.getInstance()
        //ВРЕМЕННОЕ РЕШЕНИЕ
        loadBattleView(battleManager)

        equipmentBViewModel.getEquipmentToIdPlayer(Player.getInstance().getIdActivePlayer())

        val layoutManager = LinearLayoutManager(context)
        viewBinding.recBattle.layoutManager = layoutManager

//        val items = listOf(
//            Information(),
//            Damage(),
//            Initiative())

        val battleAdapter =
            BattleAdapter(
                battleManager,
                viewLifecycleOwner,
                requireContext(),
                viewModel ,
                this,
                equipmentBViewModel,
                attackViewModel
            )
        viewBinding.recBattle.adapter = battleAdapter

        val itemTouchHelperCallback = BattleItemTouchHelper(battleAdapter, requireContext())
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(viewBinding.recBattle)

        return viewBinding.root
    }

    private fun loadBattleView(battleManager: BattleManager) {
        battleManager.clearList()
        battleManager.addItem(BattleItem(1,1,"Информация", 0,true, Information()))
        battleManager.addItem(BattleItem(1,1,"Получение Урона", 1,true, Damage()))
        battleManager.addItem(BattleItem(1,1,"Инициатива", 2,true, Initiative()))
        battleManager.addItem(BattleItem(1,1,"Снаряжение", 3,true, EquipmentB()))
        battleManager.addItem(BattleItem(1,1,"Атака", 1, true, Attack()))

    }

    override fun rollInitiativeActivePlayer() {
        myInterface.get_result_roll("d20", Player.getInstance().getIdActivePlayer(),"Проверка Инициативы", 0,0, 0,null, null)
    }

    override fun rollInitiativeAllPlayers() {
        for (i in Player.getInstance().getIdPlayerToPlayerList()){
            myInterface.get_result_roll("d20", i,"Проверка Инициативы", 0,0,0,null, null)
        }
    }

    override fun rollDodgeActivePlayer(){
        myInterface.get_result_roll("d20", Player.getInstance().getIdActivePlayer(), "Проверка Уклонения", 0,0,0,null, null)
    }

    override fun rollParryingActivePlayer(){
        myInterface.get_result_roll("d20", Player.getInstance().getIdActivePlayer(), "Проверка Парирования", 0,0,0,null, null)
    }

    override fun rollEquipmentTest(mode: String, param: Int) {
        myInterface.get_result_roll("d20", Player.getInstance().getIdActivePlayer(), mode, param,0, 0,null, null)
    }

    override fun rollHit(chenge: Int, bonusHit: Int, weapon: ArsenalItem) {
        myInterface.get_result_roll("d20", Player.getInstance().getIdActivePlayer(), "Проверка Попадания", 0, chenge, bonusHit,null, weapon)
    }

    override fun rollDamage(cube : String,  weapon: ArsenalItem) {
        myInterface.get_result_roll(cube, Player.getInstance().getIdActivePlayer(), "Расчет Урона", 0, 0, 0,null, weapon)
    }

}