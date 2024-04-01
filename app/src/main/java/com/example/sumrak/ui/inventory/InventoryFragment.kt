package com.example.sumrak.ui.inventory

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sumrak.Player
import com.example.sumrak.R
import com.example.sumrak.databinding.FragmentInventoryBinding
import com.example.sumrak.ui.inventory.recycler.armor.Armor
import com.example.sumrak.ui.inventory.recycler.armor.ArmorViewModel
import com.example.sumrak.ui.inventory.recycler.arsenal.Arsenal
import com.example.sumrak.ui.inventory.recycler.consumables.Consumbles
import com.example.sumrak.ui.inventory.recycler.consumables.ConsumblesViewModel
import com.example.sumrak.ui.inventory.recycler.effects.Effects
import com.example.sumrak.ui.inventory.recycler.effects.EffectsViewModel
import com.example.sumrak.ui.inventory.recycler.equipment.EquipmentViewModel

class InventoryFragment : Fragment() {

    private lateinit var b: FragmentInventoryBinding
    private lateinit var consumblesViewModel: ConsumblesViewModel
    private lateinit var effectsViewModel: EffectsViewModel
    private lateinit var armorViewModel: ArmorViewModel
    private lateinit var inventoryViewModel: InventoryViewModel
    private lateinit var equipmentViewModel : EquipmentViewModel

    private lateinit var myInterface : Interface

    companion object {
        private val instance = InventoryFragment()

        @JvmStatic
        fun getInstance(): InventoryFragment {
            return instance
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myInterface = context as Interface
    }

    interface Interface{
        fun get_result_roll(text: String, player: Int, mode : String, param: Int,position: Int?)
    }

    private lateinit var viewModel: InventoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentInventoryBinding.inflate(layoutInflater)

        val idPlayer = Player.getInstance().getIdActivePlayer()

        val inventoryManager = InventoryManager.getInstance()

        val layoutManager = LinearLayoutManager(context)
        b.recyclerInventory.layoutManager = layoutManager

        effectsViewModel = ViewModelProvider(this).get(EffectsViewModel::class.java)
        consumblesViewModel = ViewModelProvider(this).get(ConsumblesViewModel::class.java)
        armorViewModel = ViewModelProvider(this).get(ArmorViewModel::class.java)
        inventoryViewModel = ViewModelProvider(this).get(InventoryViewModel::class.java)
        equipmentViewModel = ViewModelProvider(this).get(EquipmentViewModel::class.java)

        load(idPlayer)


        val items = listOf(
            Effects(),
            Arsenal(),
            Consumbles(),
            Armor()
            // Добавьте другие элементы по мере необходимости
        )
        val viewModels: Map<Int, ViewModel> = mapOf(
            R.layout.maket_inventory_effects to effectsViewModel,
            //R.layout.maket_inventory_arsenal to arsenalViewModel,
            R.layout.maket_inventory_consumables to consumblesViewModel,
            R.layout.maket_inventory_armor to armorViewModel,
            R.layout.maket_inventory_equipment to equipmentViewModel
        )
        //val viewModels: Map<String, ViewModel> = mapOf(
        //    "Эффекты" to effectsViewModel,
        //    //R.layout.maket_inventory_arsenal to arsenalViewModel,
        //    "Расходники" to consumblesViewModel,
        //    "Броня" to armorViewModel
        //)
        // Создание и установка адаптера с передачей списка элементов и Map с ViewModel
        val inventoryAdapter = InventoryAdapter(inventoryManager, viewModels, viewLifecycleOwner, requireContext(), inventoryViewModel, this)
        b.recyclerInventory.adapter = inventoryAdapter

        val itemTouchHelperCallback = InventoryItemTouchHelper(inventoryAdapter, requireContext())
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(b.recyclerInventory)




        return b.root
    }

    fun useEquipment(mode: String, param : Int){
        myInterface.get_result_roll("d20", Player.getInstance().getIdActivePlayer(), mode, param,null)
    }

    private fun load(idPlayer: Int) {
        consumblesViewModel.getCosumablesToPlayerId(idPlayer)
        effectsViewModel.getEffectsToPlayerId(idPlayer)
        armorViewModel.getArmorToPlayer(idPlayer)
        inventoryViewModel.getInventoryItemToPlayer(idPlayer)
        equipmentViewModel.getEquipmentToIdPlayer(idPlayer)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(InventoryViewModel::class.java)

    }



}