package com.example.sumrak.ui.profile

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.sumrak.Data.playerdb.PlayerDbEntity
import com.example.sumrak.Data.playerdb.PlayerVariableEntity
import com.example.sumrak.Data.playerdb.PlayerViewModel
import com.example.sumrak.Player
import com.example.sumrak.R
import com.example.sumrak.databinding.FragmentAddPlayerBinding
import com.example.sumrak.ui.inventory.InventoryItem
import com.example.sumrak.ui.inventory.InventoryViewModel
import com.example.sumrak.ui.inventory.recycler.armor.Armor
import com.example.sumrak.ui.inventory.recycler.arsenal.Arsenal
import com.example.sumrak.ui.inventory.recycler.consumables.Consumbles
import com.example.sumrak.ui.inventory.recycler.effects.Effects
import com.example.sumrak.ui.inventory.recycler.equipment.Equipment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception


class AddPlayer : Fragment() {

    private lateinit var myInterfaceAddPlayer: Interface
    private lateinit var b: FragmentAddPlayerBinding
    private lateinit var navController: NavController
    private lateinit var playerViewModel : PlayerViewModel
    private lateinit var inventoryViewModel: InventoryViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myInterfaceAddPlayer = context as Interface
    }

    interface Interface{
        fun save_player(player: PlayerDbEntity)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentAddPlayerBinding.inflate(layoutInflater)



        return b.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Player.getInstance().getPlayerCount()==0){
            b.btnBack.visibility = View.GONE
        }
        inventoryViewModel = ViewModelProvider(this).get(InventoryViewModel::class.java)
        playerViewModel = ViewModelProvider(this).get(PlayerViewModel::class.java)

        navController = Navigation.findNavController(view)
        b.btnBack.setOnClickListener { navController.navigate(R.id.action_addPlayer_to_navigation_profile) }



        b.saveStats.setOnClickListener {
            try {
                val l = b.editLightKarm.text.toString().toInt()
                val d = b.editDarkKarm.text.toString().toInt()
                val player = PlayerDbEntity(null,
                    b.editNamePlayer.text.toString(),
                    b.editDb.text.toString().toInt(),
                    b.editBb.text.toString().toInt(),
                    b.editPower.text.toString().toInt(),
                    b.editDexterity.text.toString().toInt(),
                    b.editVolition.text.toString().toInt(),
                    b.editEndurance.text.toString().toInt(),
                    b.editIntelect.text.toString().toInt(),
                    b.editInsihgt.text.toString().toInt(),
                    b.editObservation.text.toString().toInt(),
                    b.editChsarisma.text.toString().toInt(),
                    b.editHP.text.toString().toInt(),
                    b.editFate.text.toString().toInt(),
                    b.editInfluence.text.toString().toInt(),
                    b.editXP.text.toString().toInt(),
                0,
                0)
                val id = playerViewModel.addPlayer(player)
                val variable = PlayerVariableEntity(
                    id.toInt(),
                    b.editHP.text.toString().toInt(),
                    l,
                    d,
                    b.editFate.text.toString().toInt(),
                    0,
                    0
                )
                playerViewModel.addPlayerVariable(variable)
                playerViewModel.setIdActivePlayer(id.toInt())
                createInventoryItem(id.toInt())

                navController.navigate(R.id.navigation_home)
            }
            catch (e: Exception){
                Snackbar.make(b.root, "Для сохранения персонажа необходимо заполнить все поля!", Snackbar.LENGTH_SHORT).show()
            }
        }

    }

    fun createInventoryItem(idPlayer: Int){
        inventoryViewModel.addInventoryView(InventoryItem(0, idPlayer, "Эффекты", 0, true, Effects()))
        inventoryViewModel.addInventoryView(InventoryItem(0, idPlayer, "Арсенал", 1, true,  Arsenal()))
        inventoryViewModel.addInventoryView(InventoryItem(0, idPlayer, "Расходники", 3, true,  Consumbles()))
        inventoryViewModel.addInventoryView(InventoryItem(0, idPlayer, "Броня", 2, true,  Armor()))
        inventoryViewModel.addInventoryView(InventoryItem(0, idPlayer, "Снаряжение", 4, true, Equipment()))
    }

    override fun onStop() {
        super.onStop()
        //navController.navigate(R.id.navigation_profile)
    }
}