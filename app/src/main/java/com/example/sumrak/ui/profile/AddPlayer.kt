package com.example.sumrak.ui.profile

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.sumrak.data.playerdb.PlayerDbEntity
import com.example.sumrak.data.playerdb.PlayerVariableEntity
import com.example.sumrak.data.playerdb.PlayerViewModel
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
import java.lang.Exception


class AddPlayer : Fragment() {

    private var myInterfaceAddPlayer: Interface? = null
    private var viewBinding: FragmentAddPlayerBinding? = null
    private var navController: NavController? = null
    private lateinit var playerViewModel : PlayerViewModel
    private var inventoryViewModel: InventoryViewModel? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myInterfaceAddPlayer = context as Interface
    }

    interface Interface{
        fun savePlayer(player: PlayerDbEntity)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentAddPlayerBinding.inflate(layoutInflater)
        return viewBinding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Player.getInstance().getPlayerCount() == 0) {
            viewBinding?.btnBack?.isVisible = false
        }
        inventoryViewModel = ViewModelProvider(this).get(InventoryViewModel::class.java)
        playerViewModel = ViewModelProvider(this).get(PlayerViewModel::class.java)

        navController = Navigation.findNavController(view)

        viewBinding?.apply {
            btnBack.setOnClickListener {
                navController?.navigate(R.id.action_addPlayer_to_navigation_profile)
            }
            saveStats.setOnClickListener {
                try {
                    val l = editLightKarm.text.toString().toInt()
                    val d = editDarkKarm.text.toString().toInt()
                    val player = PlayerDbEntity(
                        null,
                        editNamePlayer.text.toString(),
                        editDb.text.toString().toInt(),
                        editBb.text.toString().toInt(),
                        editPower.text.toString().toInt(),
                        editDexterity.text.toString().toInt(),
                        editVolition.text.toString().toInt(),
                        editEndurance.text.toString().toInt(),
                        editIntelect.text.toString().toInt(),
                        editInsihgt.text.toString().toInt(),
                        editObservation.text.toString().toInt(),
                        editChsarisma.text.toString().toInt(),
                        editHP.text.toString().toInt(),
                        editFate.text.toString().toInt(),
                        editInfluence.text.toString().toInt(),
                        editXP.text.toString().toInt(),
                        0,
                        0
                    )
                    val id = playerViewModel.addPlayer(player)
                    val variable = PlayerVariableEntity(
                        id.toInt(),
                        editHP.text.toString().toInt(),
                        l,
                        d,
                        editFate.text.toString().toInt(),
                        0,
                        0
                    )
                    playerViewModel.addPlayerVariable(variable)
                    playerViewModel.setIdActivePlayer(id.toInt())
                    createInventoryItem(id.toInt())

                    navController?.navigate(R.id.navigation_home)
                } catch (e: Exception) {
                    viewBinding?.root?.let { view ->
                        Snackbar
                            .make(
                                view,
                                "Для сохранения персонажа необходимо заполнить все поля!",
                                Snackbar.LENGTH_SHORT
                            ).show()
                    }
                }
            }
        }
    }

    private fun createInventoryItem(idPlayer: Int) {
        inventoryViewModel?.apply {
            addInventoryView(
                InventoryItem(
                    0,
                    idPlayer,
                    "Эффекты",
                    0,
                    true,
                    Effects()
                )
            )
            addInventoryView(
                InventoryItem(
                    0,
                    idPlayer,
                    "Арсенал",
                    1,
                    true,
                    Arsenal()
                )
            )
            addInventoryView(
                InventoryItem(
                    0,
                    idPlayer,
                    "Расходники",
                    3,
                    true,
                    Consumbles()
                )
            )
            addInventoryView(
                InventoryItem(
                    0,
                    idPlayer,
                    "Броня",
                    2,
                    true,
                    Armor()
                )
            )
            addInventoryView(
                InventoryItem(
                    0,
                    idPlayer,
                    "Снаряжение",
                    4,
                    true,
                    Equipment()
                )
            )
        }
    }
}