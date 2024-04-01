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
import com.example.sumrak.data.playerdb.PlayerViewModel
import com.example.sumrak.lists.HistoryRollManager
import com.example.sumrak.Player
import com.example.sumrak.R
import com.example.sumrak.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private var viewModel: ProfileViewModel? = null
    private var viewBinding: FragmentProfileBinding? = null
    private var myInterfaceProfile: Interface? = null
    private var navController: NavController? = null
    private var playerViewModel: PlayerViewModel? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myInterfaceProfile = context as Interface
    }

    interface Interface{
        fun editPlayer()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentProfileBinding.inflate(layoutInflater)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Player.getInstance().getPlayerCount()==1){
            viewBinding?.deletePers?.isEnabled = false
        }

        playerViewModel = ViewModelProvider(this).get(PlayerViewModel::class.java)

        navController = Navigation.findNavController(view)

        viewBinding?.apply {
            newPers.setOnClickListener {
                navController?.navigate(R.id.action_navigation_profile_to_addPlayer2)
                myInterfaceProfile?.editPlayer()
            }

            editPers.setOnClickListener {
                navController?.navigate(R.id.action_navigation_profile_to_updatePlayer)
                myInterfaceProfile?.editPlayer()
            }

            deletePers.setOnClickListener { deletePlayer() }
        }
    }

    private fun deletePlayer() {
        val name = Player.getInstance().getActivePlayer().namePlayer
        val builder = AlertDialog.Builder(context)
        builder.setPositiveButton("Да") { dialog, which ->
            HistoryRollManager.getInstance().removeItemToIdPlayer(Player.getInstance().getIdActivePlayer())
            playerViewModel?.deletePlayer(Player.getInstance().getIdActivePlayer())
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
}
