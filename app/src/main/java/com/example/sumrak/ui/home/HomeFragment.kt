package com.example.sumrak.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sumrak.data.playerdb.PlayerViewModel
import com.example.sumrak.Player
import com.example.sumrak.databinding.FragmentHomeBinding
import com.example.sumrak.ui.inventory.recycler.arsenal.item.ArsenalItem

class HomeFragment : Fragment() {

    private var viewBinding: FragmentHomeBinding? = null
    private var myHomeInterface: Interface? = null
    private var playerViewModel : PlayerViewModel? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myHomeInterface = context as Interface
    }

    interface Interface{
        fun get_result_roll(text: String, player: Int, mode : String, param: Int, change : Int, bonus: Int, position: Int?, weapon: ArsenalItem?)
        fun touch_screen()
    }
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = viewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View? = binding?.root

        playerViewModel = ViewModelProvider(requireActivity()).get(PlayerViewModel::class.java)
        loadPlayer()

        viewBinding?.apply {
            playerViewModel?.playerV?.observe(viewLifecycleOwner) {
                if (it.hp > Player.getInstance().getActivePlayer().maxHp) {
                    tvChangeHp.isVisible = true
                    val hp = it.hp.toString()
                    val change = (it.hp - Player.getInstance().getActivePlayer().maxHp).toString()
                    hpPlayer.text = hp
                    tvChangeHp.text = "+$change"
                } else {
                    tvChangeHp.isVisible = false
                    hpPlayer.text = it.hp.toString()
                }
                hpPlayer.text = it.hp.toString()
                tvLightKarm.text = it.lightKarm.toString()
                tvDarkKarm.text = it.darkKarm.toString()
                setKarmBlago(it.darkKarm, it.lightKarm)
                remFate.text = "Очки судьбы: ${it.fate}"
                remFate.isEnabled = it.fate > 0
                btnRemDark.isEnabled = it.darkKarm > 0
                btnRemLight.isEnabled = it.lightKarm > 0
            }
            addHp.setOnClickListener {
                playerViewModel?.updateHpPlayer(
                    Player.getInstance().getIdActivePlayer(), 1
                )
            }
            remHp.setOnClickListener {
                playerViewModel?.updateHpPlayer(
                    Player.getInstance().getIdActivePlayer(), -1
                )
            }


            btnDb.setOnClickListener {
                myHomeInterface?.get_result_roll(
                    "d20",
                    Player.getInstance().getIdActivePlayer(),
                    "Проверка Дальнего боя",
                    0,
                    0,
                    0,
                    null,
                    null
                )
            }
            btnBb.setOnClickListener {
                myHomeInterface?.get_result_roll(
                    "d20",
                    Player.getInstance().getIdActivePlayer(),
                    "Проверка Ближнего боя",
                    0,
                    0,
                    0,
                    null,
                    null
                )
            }
            btnPower.setOnClickListener {
                myHomeInterface?.get_result_roll(
                    "d20",
                    Player.getInstance().getIdActivePlayer(),
                    "Проверка Силы",
                    0,
                    0,
                    0,
                    null,
                    null
                )
            }
            btnDexterity.setOnClickListener {
                myHomeInterface?.get_result_roll(
                    "d20",
                    Player.getInstance().getIdActivePlayer(),
                    "Проверка Ловкости",
                    0,
                    0,
                    0,
                    null,
                    null
                )
            }
            btnVolition.setOnClickListener {
                myHomeInterface?.get_result_roll(
                    "d20",
                    Player.getInstance().getIdActivePlayer(),
                    "Проверка Воли",
                    0,
                    0,
                    0,
                    null,
                    null
                )
            }
            btnEndurance.setOnClickListener {
                myHomeInterface?.get_result_roll(
                    "d20",
                    Player.getInstance().getIdActivePlayer(),
                    "Проверка Стойкости",
                    0,
                    0,
                    0,
                    null,
                    null
                )
            }
            btnIntelect.setOnClickListener {
                myHomeInterface?.get_result_roll(
                    "d20",
                    Player.getInstance().getIdActivePlayer(),
                    "Проверка Интелекта",
                    0,
                    0,
                    0,
                    null,
                    null
                )
            }
            btnInsihgt.setOnClickListener {
                myHomeInterface?.get_result_roll(
                    "d20",
                    Player.getInstance().getIdActivePlayer(),
                    "Проверка Сообразительности",
                    0,
                    0,
                    0,
                    null,
                    null
                )
            }
            btnObservation.setOnClickListener {
                myHomeInterface?.get_result_roll(
                    "d20",
                    Player.getInstance().getIdActivePlayer(),
                    "Проверка Наблюдательности",
                    0,
                    0,
                    0,
                    null,
                    null
                )
            }
            btnChsarisma.setOnClickListener {
                myHomeInterface?.get_result_roll(
                    "d20",
                    Player.getInstance().getIdActivePlayer(),
                    "Проверка Харизмы",
                    0,
                    0,
                    0,
                    null,
                    null
                )
            }

            remFate.setOnClickListener { remFate() }

            fuss.setOnClickListener {
                myHomeInterface?.get_result_roll(
                    "2d6",
                    Player.getInstance().getIdActivePlayer(),
                    "Проверка Суеты",
                    0,
                    0,
                    0,
                    null,
                    null
                )
            }
            btnAddLight.setOnClickListener {
                playerViewModel?.updateLightKarm(
                    Player.getInstance().getIdActivePlayer(), 1
                )
            }
            btnRemLight.setOnClickListener {
                playerViewModel?.updateLightKarm(
                    Player.getInstance().getIdActivePlayer(), -1
                )
            }
            btnAddDark.setOnClickListener {
                playerViewModel?.updateDarkKarm(
                    Player.getInstance().getIdActivePlayer(), 1
                )
            }
            btnRemDark.setOnClickListener {
                playerViewModel?.updateDarkKarm(
                    Player.getInstance().getIdActivePlayer(), -1
                )
            }

            homeFonClick.setOnClickListener { myHomeInterface?.touch_screen() }
        }
        return root
    }

    private fun setKarmBlago(darkKarm: Int, lightKarm: Int) {
        viewBinding?.apply {
            if (darkKarm >= lightKarm){
                if ((darkKarm - lightKarm <= 15)&&(darkKarm>=80)&&(lightKarm>=80)){
                    tvKarmBlago.text = "Большое Благословление Сумрака"
                }
                else if((darkKarm - lightKarm <= 10)&&(darkKarm>=40)&&(lightKarm>=40)){
                    tvKarmBlago.text = "Среднее Благословление Сумрака"
                }
                else if ((darkKarm - lightKarm <= 5)&&(darkKarm>=20)&&(lightKarm>=20)){
                    tvKarmBlago.text = "Малое Благословление Сумрака"
                }
                else if (darkKarm > 99){
                    tvKarmBlago.text = "Большое Благословление Тьмы"
                }
                else if (darkKarm > 59){
                    tvKarmBlago.text = "Среднее Благословление Тьмы"
                }
                else if (darkKarm > 19){
                    tvKarmBlago.text = "Малое Благословление Тьмы"
                }
                else{
                    tvKarmBlago.text = ""
                }
            } else{
                if ((lightKarm - darkKarm <= 15)&&(darkKarm>=80)&&(lightKarm>=80)){
                    tvKarmBlago.text = "Большое Благословление Сумрака"
                }
                else if((lightKarm - darkKarm <= 10)&&(darkKarm>=40)&&(lightKarm>=40)){
                    tvKarmBlago.text = "Среднее Благословление Сумрака"
                }
                else if ((lightKarm - darkKarm <= 5)&&(darkKarm>=20)&&(lightKarm>=20)){
                    tvKarmBlago.text = "Малое Благословление Сумрака"
                }
                else if (lightKarm > 99){
                    tvKarmBlago.text = "Большое Благословление Света"
                }
                else if (lightKarm > 59){
                    tvKarmBlago.text = "Среднее Благословление Света"
                }
                else if (lightKarm > 19){
                    tvKarmBlago.text = "Малое Благословление Света"
                }
                else{
                    tvKarmBlago.text = ""
                }
            }
        }
    }

    private fun remFate() {
         playerViewModel?.updateFatePlayer(Player.getInstance().getIdActivePlayer())

    }

    private fun loadPlayer() {
        if (Player.getInstance().getPlayerCount()!=0){
            viewBinding?.apply {
                btnDb.text = "Дальний бой " + Player.getInstance().getActivePlayer().db
                btnBb.text = "Ближний бой " + Player.getInstance().getActivePlayer().bb
                btnPower.text = "Сила " + Player.getInstance().getActivePlayer().power
                btnDexterity.text =
                    "Ловкость " + Player.getInstance().getActivePlayer().dexterity
                btnVolition.text =
                    "Воля " + Player.getInstance().getActivePlayer().volition
                btnEndurance.text =
                    "Стойкость " + Player.getInstance().getActivePlayer().endurance
                btnIntelect.text =
                    "Интелект " + Player.getInstance().getActivePlayer().intelect
                btnInsihgt.text =
                    "Сообразительность " + Player.getInstance().getActivePlayer().insihgt
                btnObservation.text =
                    "Наблюдательность " + Player.getInstance().getActivePlayer().observation
                btnChsarisma.text =
                    "Харизма " + Player.getInstance().getActivePlayer().chsarisma
                playerViewModel?.getPlayerVariableToId(Player.getInstance().getIdActivePlayer())
            }
        }

    }

    companion object {
        private val instance = HomeFragment()

        @JvmStatic
        fun getInstance(): HomeFragment {
            return instance
        }
    }
}