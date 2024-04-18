package com.example.sumrak.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.sumrak.data.playerdb.PlayerDbEntity
import com.example.sumrak.data.playerdb.PlayerViewModel
import com.example.sumrak.Player
import com.example.sumrak.R
import com.example.sumrak.data.playerdb.PlayerVariableEntity
import com.example.sumrak.databinding.FragmentUpdatePlayerBinding
import com.google.android.material.snackbar.Snackbar

class UpdatePlayer : Fragment() {

    private var viewBinding: FragmentUpdatePlayerBinding? = null
    private var navController: NavController? = null
    private var playerViewModel : PlayerViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentUpdatePlayerBinding.inflate(layoutInflater)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        playerViewModel = ViewModelProvider(this).get(PlayerViewModel::class.java)
        playerViewModel?.getPlayerVariableToId(Player.getInstance().getIdActivePlayer())


        viewBinding?.apply {
            btnBack.setOnClickListener { navController?.navigate(R.id.action_updatePlayer_to_navigation_profile) }

            val player = playerViewModel?.getPlayerToId(Player.getInstance().getIdActivePlayer())
            editNamePlayer.setText(player?.namePlayer)
            editDb.setText(player?.db.toString())
            editBb.setText(player?.bb.toString())
            editPower.setText(player?.power.toString())
            editDexterity.setText(player?.dexterity.toString())
            editVolition.setText(player?.volition.toString())
            editEndurance.setText(player?.endurance.toString())
            editIntelect.setText(player?.intelect.toString())
            editInsihgt.setText(player?.insihgt.toString())
            editObservation.setText(player?.observation.toString())
            editChsarisma.setText(player?.chsarisma.toString())
            editHP.setText(player?.maxHp.toString())
            editXP.setText(player?.xp.toString())
            editInfluence.setText(player?.influence.toString())
            editFate.setText(player?.maxFate.toString())

            saveStats.setOnClickListener {
                try {
                    val playerV = playerViewModel?.playerVariable
                    val player = PlayerDbEntity(
                        Player.getInstance().getActivePlayer().id,
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
                        Player.getInstance().getActivePlayer().activeArmor,
                        Player.getInstance().getActivePlayer().activeArsenal,
                        Player.getInstance().getActivePlayer().classPers,
                        Player.getInstance().getActivePlayer().rank,
                        Player.getInstance().getActivePlayer().bitchPlace,
                        Player.getInstance().getActivePlayer().markFate,
                        Player.getInstance().getActivePlayer().skills,
                        Player.getInstance().getActivePlayer().profile,
                        Player.getInstance().getActivePlayer().sound
                    )

                    playerViewModel?.updatePlayerVariable(
                        PlayerVariableEntity(
                            id = playerV!!.id,
                            hp = editHP.text.toString().toInt(),
                            maxHp = editHP.text.toString().toInt(),
                            light_karm = playerV.lightKarm,
                            dark_karm = playerV.darkKarm,
                            fate = editFate.text.toString().toInt(),
                            maxFate = editFate.text.toString().toInt(),
                            dodge = playerV.dodge,
                            parrying = playerV.parrying
                        )
                    )
                    playerViewModel?.updatePlayer(player)
                    playerViewModel?.setIdActivePlayer(Player.getInstance().getActivePlayer().id!!)
                    navController?.navigate(R.id.navigation_home)
                } catch (e: Exception) {
                    viewBinding?.let { view ->
                        Snackbar
                            .make(
                                view.root,
                                "Для сохранения изменений необходимо заполнить все поля!",
                                Snackbar.LENGTH_SHORT
                            ).show()
                    }

                }
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