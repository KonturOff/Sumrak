package com.example.sumrak.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sumrak.Data.playerdb.PlayerViewModel
import com.example.sumrak.Player
import com.example.sumrak.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var b: FragmentHomeBinding
    private lateinit var myHomeInterface: Interface
    private lateinit var playerViewModel : PlayerViewModel



    override fun onAttach(context: Context) {
        super.onAttach(context)
        myHomeInterface = context as Interface
    }

    interface Interface{
        fun get_result_roll(text: String, player: Int, mode : String, param: Int, position: Int?)
        fun touch_screen()
    }
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = b

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        b = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        playerViewModel = ViewModelProvider(requireActivity()).get(PlayerViewModel::class.java)
        loadPlayer()


        playerViewModel.playerV.observe(viewLifecycleOwner, Observer {
            if (it.hp > Player.getInstance().getActivePlayer().max_hp){
                b.tvChangeHp.visibility = View.VISIBLE
                val hp = it.hp.toString()
                val change = (it.hp - Player.getInstance().getActivePlayer().max_hp).toString()
                b.hpPlayer.text = hp
                b.tvChangeHp.text = "+$change"
            }
            else{
                b.tvChangeHp.visibility = View.GONE
                b.hpPlayer.text = it.hp.toString()
            }
            b.hpPlayer.text = it.hp.toString()
            b.tvLightKarm.text = it.light_karm.toString()
            b.tvDarkKarm.text = it.dark_karm.toString()
            b.remFate.text = "Очки судьбы: ${it.fate}"
            b.remFate.isEnabled = it.fate > 0
            b.btnRemDark.isEnabled = it.dark_karm > 0
            b.btnRemLight.isEnabled = it.light_karm > 0
        })
        b.addHp.setOnClickListener { playerViewModel.updateHpPlayer(Player.getInstance().getIdActivePlayer(), 1) }
        b.remHp.setOnClickListener { playerViewModel.updateHpPlayer(Player.getInstance().getIdActivePlayer(), -1) }


        b.btnDb.setOnClickListener { myHomeInterface.get_result_roll("d20",Player.getInstance().getIdActivePlayer(),"Проверка Дальнего боя",0, null) }
        b.btnBb.setOnClickListener { myHomeInterface.get_result_roll("d20",Player.getInstance().getIdActivePlayer(),"Проверка Ближнего боя", 0,null)  }
        b.btnPower.setOnClickListener { myHomeInterface.get_result_roll("d20",Player.getInstance().getIdActivePlayer(),"Проверка Силы", 0,null)  }
        b.btnDexterity.setOnClickListener { myHomeInterface.get_result_roll("d20",Player.getInstance().getIdActivePlayer(),"Проверка Ловкости",0, null)  }
        b.btnVolition.setOnClickListener { myHomeInterface.get_result_roll("d20",Player.getInstance().getIdActivePlayer(),"Проверка Воли", 0,null)  }
        b.btnEndurance.setOnClickListener { myHomeInterface.get_result_roll("d20",Player.getInstance().getIdActivePlayer(),"Проверка Стойкости",0, null)  }
        b.btnIntelect.setOnClickListener { myHomeInterface.get_result_roll("d20",Player.getInstance().getIdActivePlayer(),"Проверка Интелекта", 0,null)  }
        b.btnInsihgt.setOnClickListener { myHomeInterface.get_result_roll("d20",Player.getInstance().getIdActivePlayer(),"Проверка Сообразительности", 0,null)  }
        b.btnObservation.setOnClickListener { myHomeInterface.get_result_roll("d20",Player.getInstance().getIdActivePlayer(),"Проверка Наблюдательности", 0,null)  }
        b.btnChsarisma.setOnClickListener { myHomeInterface.get_result_roll("d20",Player.getInstance().getIdActivePlayer(),"Проверка Харизмы", 0,null)  }

        b.remFate.setOnClickListener { remFate() }

        b.fuss.setOnClickListener { myHomeInterface.get_result_roll("2d6", Player.getInstance().getIdActivePlayer(), "Проверка Суеты", 0,null) }
        b.btnAddLight.setOnClickListener { playerViewModel.updateLightKarm(Player.getInstance().getIdActivePlayer(), 1) }
        b.btnRemLight.setOnClickListener { playerViewModel.updateLightKarm(Player.getInstance().getIdActivePlayer(), -1) }
        b.btnAddDark.setOnClickListener {  playerViewModel.updateDarkKarm(Player.getInstance().getIdActivePlayer(), 1) }
        b.btnRemDark.setOnClickListener {  playerViewModel.updateDarkKarm(Player.getInstance().getIdActivePlayer(), -1) }

        b.homeFonClick.setOnClickListener { myHomeInterface.touch_screen() }
        return root
    }



    private fun remFate() {
         playerViewModel.updateFatePlayer(Player.getInstance().getIdActivePlayer())

    }

    private fun loadPlayer() {
        if (Player.getInstance().getPlayerCount()!=0){
            b.btnDb.text = "Дальний бой " + Player.getInstance().getActivePlayer().db
            b.btnBb.text = "Ближний бой " + Player.getInstance().getActivePlayer().bb
            b.btnPower.text = "Сила " + Player.getInstance().getActivePlayer().power
            b.btnDexterity.text = "Ловкость "+ Player.getInstance().getActivePlayer().dexterity
            b.btnVolition.text = "Воля " + Player.getInstance().getActivePlayer().volition
            b.btnEndurance.text = "Стойкость " + Player.getInstance().getActivePlayer().endurance
            b.btnIntelect.text = "Интелект " + Player.getInstance().getActivePlayer().intelect
            b.btnInsihgt.text = "Сообразительность " + Player.getInstance().getActivePlayer().insihgt
            b.btnObservation.text = "Наблюдательность " + Player.getInstance().getActivePlayer().observation
            b.btnChsarisma.text = "Харизма " + Player.getInstance().getActivePlayer().chsarisma
            playerViewModel.getPlayerVariableToId(Player.getInstance().getIdActivePlayer())
        }

    }



    override fun onDestroyView() {
        super.onDestroyView()

    }

    companion object {
        private val instance = HomeFragment()

        @JvmStatic
        fun getInstance(): HomeFragment {
            return instance
        }
    }
}