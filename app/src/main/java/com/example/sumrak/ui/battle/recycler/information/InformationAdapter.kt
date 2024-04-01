package com.example.sumrak.ui.battle.recycler.information

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
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
    private val context: Context
) : DelegateAdapterB<Information, InformationViewHolder, BattleViewModel>() {


    override fun onCreateViewHolder(parent: ViewGroup): InformationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.maket_battle_information, parent, false)
        return InformationViewHolder(view, battleViewModel, lifecycleOwner)
    }

    override fun onBindViewHolder(holder: InformationViewHolder, item: Information) {
        holder.bind(item, battleViewModel, lifecycleOwner, context)
    }

    class InformationViewHolder(
        itemView: View,
        viewModel: BattleViewModel,
        lifecycleOwner: LifecycleOwner
    ) : RecyclerView.ViewHolder(itemView){
        val b = MaketBattleInformationBinding.bind(itemView)

        init {
            b.tvBonusEndur.text = Player.getInstance().getBonusEndurance().toString()
            b.tvBonusPow.text = Player.getInstance().getBonusPower().toString()

            viewModel.playerV.observe(lifecycleOwner, Observer {
                b.tvHpPlayer.text = it.hp.toString()
                b.pbHpPlayer.max = Player.getInstance().getActivePlayer().max_hp
                b.pbHpPlayer.setProgress(it.hp, true)
            })

            viewModel.armorV.observe(lifecycleOwner, Observer {
                if (it.name==""){
                    b.tvNameArmor.text = "Ну что, ты, название поленился записать?"
                }
                else b.tvNameArmor.text = it.name
                if (it.id!=0){
                    b.pbArmorPlayer.visibility = View.VISIBLE
                    b.armorVisibile.visibility = View.VISIBLE
                    b.tvClassArmor.text = it.classArmor
                    b.tvParamsArmor.text = "${it.params} (${it.endurance}/${it.enduranceMax})"
                    b.tvFeaturesArmor.text = it.features
                    b.pbArmorPlayer.max = it.enduranceMax
                    b.pbArmorPlayer.setProgress(it.endurance, true)
                }
                else{
                    b.pbArmorPlayer.visibility = View.GONE
                    b.armorVisibile.visibility = View.GONE
                }

            })
        }


        fun bind(
            information: Information,
            viewModel: BattleViewModel,
            lifecycleOwner: LifecycleOwner,
            context: Context
        ) {

        }



    }
}