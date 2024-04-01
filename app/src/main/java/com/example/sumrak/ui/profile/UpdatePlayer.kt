package com.example.sumrak.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.sumrak.Data.playerdb.PlayerDbEntity
import com.example.sumrak.Data.playerdb.PlayerViewModel
import com.example.sumrak.Player
import com.example.sumrak.R
import com.example.sumrak.databinding.FragmentUpdatePlayerBinding
import com.google.android.material.snackbar.Snackbar

class UpdatePlayer : Fragment() {

    private lateinit var b: FragmentUpdatePlayerBinding
    private lateinit var navController: NavController
    private lateinit var playerViewModel : PlayerViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        b = FragmentUpdatePlayerBinding.inflate(layoutInflater)

        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        playerViewModel = ViewModelProvider(this).get(PlayerViewModel::class.java)

        b.btnBack.setOnClickListener { navController.navigate(R.id.action_updatePlayer_to_navigation_profile)}

        val player = playerViewModel.getPlayerToId(Player.getInstance().getIdActivePlayer())
        b.editNamePlayer.setText(player.name_player)
        b.editDb.setText(player.db.toString())
        b.editBb.setText(player.bb.toString())
        b.editPower.setText(player.power.toString())
        b.editDexterity.setText(player.dexterity.toString())
        b.editVolition.setText(player.volition.toString())
        b.editEndurance.setText(player.endurance.toString())
        b.editIntelect.setText(player.intelect.toString())
        b.editInsihgt.setText(player.insihgt.toString())
        b.editObservation.setText(player.observation.toString())
        b.editChsarisma.setText(player.chsarisma.toString())
        b.editHP.setText(player.max_hp.toString())
        b.editXP.setText(player.xp.toString())
        b.editInfluence.setText(player.influence.toString())
        b.editFate.setText(player.max_fate.toString())

        b.saveStats.setOnClickListener {
            try {
                val player = PlayerDbEntity(
                    Player.getInstance().getActivePlayer().id,
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
                    Player.getInstance().getActivePlayer().active_armor,
                    Player.getInstance().getActivePlayer().active_arsenal)

                playerViewModel.updatePlayer(player)
                playerViewModel.setIdActivePlayer(Player.getInstance().getActivePlayer().id!!)
                navController.navigate(R.id.navigation_home)
            }
            catch (e: Exception){
                Snackbar.make(b.root, "Для сохранения изменений необходимо заполнить все поля!", Snackbar.LENGTH_SHORT).show()

            }
        }

    }

    //fun hp():Int{
    //    if (b.editHP.text.toString().toInt() < Player.getInstance().getActivePlayer().hp){
    //        return b.editHP.text.toString().toInt()
    //    }
    //    else{
    //        return Player.getInstance().getActivePlayer().hp
    //    }
    //}

    //fun fate():Int{
    //    if (b.editFate.text.toString().toInt() < Player.getInstance().getActivePlayer().fate){
    //        return b.editFate.text.toString().toInt()
    //    }
    //    else{
    //        return Player.getInstance().getActivePlayer().fate
    //    }
    //}


}