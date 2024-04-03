package com.example.sumrak.ui.battle.recycler.information

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.Player
import com.example.sumrak.R
import com.example.sumrak.databinding.MaketBattleInformationBinding
import com.example.sumrak.ui.battle.BattleViewModel
import com.example.sumrak.ui.battle.recycler.DelegateAdapterB
import com.example.sumrak.ui.battle.recycler.information.InformationAdapter.*


class InformationAdapter(
    private val battleViewModel: BattleViewModel,
    private val lifecycleOwner: LifecycleOwner,
//    private val context: Context
) : DelegateAdapterB<Information, InformationViewHolder, BattleViewModel>() {


    override fun onCreateViewHolder(parent: ViewGroup): InformationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.maket_battle_information, parent, false)
        return InformationViewHolder(view, battleViewModel, lifecycleOwner)
    }

    override fun onBindViewHolder(holder: InformationViewHolder, item: Information) {
        holder.bind(
//            item,
//            battleViewModel,
//            lifecycleOwner,
//            context
        )
    }

    class InformationViewHolder(
        itemView: View,
        viewModel: BattleViewModel,
        lifecycleOwner: LifecycleOwner
    ) : RecyclerView.ViewHolder(itemView){
        private val viewBinding = MaketBattleInformationBinding.bind(itemView)

        init {
            viewBinding.apply {
                tvBonusEndur.text = Player.getInstance().getBonusEndurance().toString()
                tvBonusPow.text = Player.getInstance().getBonusPower().toString()

                viewModel.playerV.observe(lifecycleOwner) {
                    tvHpPlayer.text = it.hp.toString()
                    pbHpPlayer.max = Player.getInstance().getActivePlayer().maxHp
                    pbHpPlayer.setProgress(it.hp, true)
                }

                viewModel.armorV.observe(lifecycleOwner) {
                    if (it.name == "") {
                        tvNameArmor.text = "Ну что, ты, название поленился записать?"
                    } else tvNameArmor.text = it.name
                    if (it.id != 0) {
                        pbArmorPlayer.isVisible = true
                        armorVisibile.isVisible = true
                        tvClassArmor.text = it.classArmor
                        tvParamsArmor.text =
                            "${it.params} (${it.endurance}/${it.enduranceMax})"
                        tvFeaturesArmor.text = it.features
                        pbArmorPlayer.max = it.enduranceMax
                        pbArmorPlayer.setProgress(it.endurance, true)
                    } else {
                        pbArmorPlayer.isVisible = false
                        armorVisibile.isVisible = false
                    }

                }
            }
        }


        // TODO а зачем нужен этот метод?
        fun bind(
//            information: Information,
//            viewModel: BattleViewModel,
//            lifecycleOwner: LifecycleOwner,
//            context: Context
        ) {

        }
    }
}