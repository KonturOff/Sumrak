package com.example.sumrak.ui.profile

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.sumrak.Data.playerdb.PlayerViewModel
import com.example.sumrak.Lists.HistoryRollManager
import com.example.sumrak.Player
import com.example.sumrak.R
import com.example.sumrak.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
    private lateinit var b: FragmentProfileBinding
    private lateinit var myInterfaceProfile: Interface
    private lateinit var navController: NavController
    private lateinit var playerViewModel: PlayerViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myInterfaceProfile = context as Interface
    }

    interface Interface{
        fun edit_player()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentProfileBinding.inflate(layoutInflater)

        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Player.getInstance().getPlayerCount()==1){
            b.deletePers.isEnabled = false
        }

        playerViewModel = ViewModelProvider(this).get(PlayerViewModel::class.java)

        navController = Navigation.findNavController(view)

        b.newPers.setOnClickListener {
            navController.navigate(R.id.action_navigation_profile_to_addPlayer2)
            myInterfaceProfile.edit_player()
        }

        b.editPers.setOnClickListener {
            navController.navigate(R.id.action_navigation_profile_to_updatePlayer)
            myInterfaceProfile.edit_player()
        }


        b.deletePers.setOnClickListener { deletePlayer() }


    }

    private fun deletePlayer() {
        val name = Player.getInstance().getActivePlayer().name_player
        val builder = AlertDialog.Builder(context)
        builder.setPositiveButton("Да") { dialog, which ->
            HistoryRollManager.getInstance().removeItemToIdPlayer(Player.getInstance().getIdActivePlayer())
            playerViewModel.deletePlayer(Player.getInstance().getIdActivePlayer())
        }
        builder.setNegativeButton(
            "Нет"
        ) { dialog, which ->
            // Stuff to do
        }

        builder.setMessage("Внимание! В случае подтвержения персонаж $name будет удален\nВместе с персонажем будет удалено всё его снаряжение")
        builder.setTitle("Удалить персонажа $name?")

        val d = builder.create()
        d.show()


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onPause() {
        super.onPause()

    }

    override fun onStop() {
        super.onStop()

    }
}