package com.example.sumrak.ui.battle.recycler.atack

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.Player
import com.example.sumrak.R
import com.example.sumrak.databinding.MaketBattleArsenalBinding
import com.example.sumrak.ui.battle.BattleViewModel
import com.example.sumrak.ui.battle.recycler.DelegateAdapterB
import com.example.sumrak.ui.inventory.recycler.arsenal.item.ArsenalItem

class AttackAdapter(
    private val battleViewModel: BattleViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val context: Context,
    private val battleFragment: attackClick,
    private val viewModel: AttackViewModel
) : DelegateAdapterB<Attack, AttackAdapter.AttackViewHolder, BattleViewModel>() {

    interface attackClick{
        fun rollHit(change: Int, bonusHit: Int, weapon : ArsenalItem)
        fun rollDamage(cube : String, weapon: ArsenalItem)
    }


    override fun onCreateViewHolder(parent: ViewGroup): AttackAdapter.AttackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.maket_battle_arsenal, parent, false)
        return AttackViewHolder(view, context, viewModel, lifecycleOwner, battleViewModel)
    }

    override fun onBindViewHolder(
        holder: AttackViewHolder,
        item: Attack,
        attackViewModel: AttackViewModel
    ) {
        holder.bind(item, battleViewModel, context, battleFragment, attackViewModel)
    }

    class AttackViewHolder(
        itemView: View,
        context: Context,
        viewModel: AttackViewModel,
        lifecycleOwner: LifecycleOwner,
        battleViewModel: BattleViewModel
    ) :RecyclerView.ViewHolder(itemView) {
        val viewBinding = MaketBattleArsenalBinding.bind(itemView)
        private var damage = ""
        private var clips : MutableList<Int> = mutableListOf(0, 0, 0, 0)
        private var change = 0
        private var step = 0
        private var bonusHit = 0
        private var bonusDamage = 0

        init {
            battleViewModel.getVisibleView("Атака")

            battleViewModel.atackVisible.observe(lifecycleOwner){
                viewBinding.atackVisibility.isVisible = it
            }

            battleViewModel.getNumberHitsPlayer(Player.getInstance().getIdActivePlayer())
            battleViewModel.hitsVisible.observe(lifecycleOwner){
                viewBinding.hitsVisible.isVisible = it
            }
            battleViewModel.hitsV.observe(lifecycleOwner){
                viewBinding.tvHit.text = it.toString()
            }

            viewModel.activeWeaponLiveData.observe(lifecycleOwner){
                viewBinding.apply {
                    tvNameArsenalB.text = it.name
                    if (it.paired){
                        tvNameArsenalB.text = it.name +" X2"
                    }
                    tvStepArsenalB.text = it.step.toString()
                    tvDistanseArsenalB.text = "${it.distanse}м"
                    tvPatronsArsenalB.text = "${it.clip1}/${it.maxPatrons}"
                    if (it.features == ""){
                        featuresArsenalVisible.isVisible = false
                    }
                    else{
                        featuresArsenalVisible.isVisible = true
                        tvFeaturesArsenalB.text = it.features
                    }
                    clips = mutableListOf(it.maxPatrons, it.clip1, it.clip2, it.clip3)
                    step = it.step

                    setClassArsenal(it.classArsenal, it.change)
                    tvDamageArsenalB.text = getDamageArsenal(it.damageX, it.damageY, it.damageZ, it.bonusPower, it.penetration)
                }

            }
        }
        fun bind(
            item: Attack,
            battleViewModel: BattleViewModel,
            context: Context,
            battleFragment: attackClick,
            viewModel: AttackViewModel
        ) {
            viewBinding.apply {

                tvAtack.setOnClickListener { battleViewModel.updateVisibleView(item.name) }

                tvBonusDamage.text = bonusDamage.toString()

                btnRefreshArsenalB.setOnClickListener { refreshPatrons(viewModel, battleViewModel) }

                btnRemBonusDamage.setOnClickListener {
                    changeBonusDamage(-1)
                }
                btnAddBonusDamage.setOnClickListener {
                    changeBonusDamage(1)
                }
                btnRemHitBonus.setOnClickListener { changeBonusHit(-1) }
                btnAddHitBonus.setOnClickListener { changeBonusHit(1) }

                btnAddHit.setOnClickListener { battleViewModel.updateValueHitToidPlayer(1) }
                btnRemHit.setOnClickListener { battleViewModel.updateValueHitToidPlayer(-1) }

                btnOneHitRoll.setOnClickListener {
                    hitRollArsenal(false, viewModel, battleViewModel, battleFragment)
                }
                btnChangeHitRoll.setOnClickListener {
                    hitRollArsenal(true, viewModel, battleViewModel, battleFragment)
                }

                btnDamageRoll.setOnClickListener { damageRoll(battleViewModel, battleFragment, viewModel) }
            }


        }
        private fun setClassArsenal(classArsenal: Int, change: Int){
            viewBinding.apply {
                this@AttackViewHolder.change = change
                if (classArsenal == 1) {
                    tvTypeArsenalB.text = "Ближ. Бой"
                    btnRefreshArsenalB.isVisible = false
                    btnChangeHitRoll.isVisible = change > 1
                    distanceVisible.isVisible = false
                    patronsVisible.isVisible = false
                    btnOneHitRoll.isEnabled = true
                    btnChangeHitRoll.isEnabled = true
                    if (change > 1) {
                        btnChangeHitRoll.text = "Вихрь ударов ($change)"
                    } else {
                        btnChangeHitRoll.isVisible = false
                    }
                } else {
                    tvTypeArsenalB.text = "Дальн. Бой"
                    btnRefreshArsenalB.isVisible = true
                    btnChangeHitRoll.isVisible = change > 1
                    distanceVisible.isVisible = true
                    patronsVisible.isVisible = true
                    if (change > 1) {
                        btnChangeHitRoll.text = "Очередь ($change)"
                    } else {
                        btnChangeHitRoll.isVisible = false
                    }
                    if (clips[1] < 1){
                        btnOneHitRoll.isEnabled = false
                        btnChangeHitRoll.isEnabled = false
                    }
                    else{
                        btnOneHitRoll.isEnabled = true
                        btnChangeHitRoll.isEnabled = true
                    }
                }
            }

        }

        private fun hitRollArsenal(
            changeHit: Boolean,
            viewModel: AttackViewModel,
            battleViewModel: BattleViewModel,
            battleFragment: attackClick
        ){
            val weapon = viewModel.activeWeaponLiveData.value!!
            if (changeHit){
                if (weapon.classArsenal == 1){
                    viewModel.hitArsenal(change)
                    battleFragment.rollHit(change, bonusHit, weapon)
                }
                else{
                    if (clips[1] < change){
                        viewModel.hitArsenal(clips[1])
                        battleFragment.rollHit(clips[1], bonusHit, weapon)
                    }
                    else{
                        viewModel.hitArsenal(change)
                        battleFragment.rollHit(change, bonusHit, weapon)
                    }
                }
            }
            else{
                viewModel.hitArsenal(1)
                battleFragment.rollHit(1,  bonusHit, weapon)
            }
            battleViewModel.updateStepInitiative(-step)
            battleViewModel.getNumberHitsPlayer(Player.getInstance().getIdActivePlayer())
        }

        private fun damageRoll(
            battleViewModel: BattleViewModel,
            battleFragment: attackClick,
            viewModel: AttackViewModel
        ){
            val weapon = viewModel.activeWeaponLiveData.value!!
            var resultDamage = ""
            if (bonusDamage > 0){
                resultDamage = "$damage+$bonusDamage"
            }
            else if (bonusDamage<0){
                resultDamage = damage+bonusDamage
            }
            else{
                resultDamage = damage
            }
            battleFragment.rollDamage(resultDamage, weapon)
            battleViewModel.updateValueHitToidPlayer(-1)
        }
        private fun getDamageArsenal(
            damageX: Int,
            damageY: Int,
            damageZ: Int,
            bonusPower: Boolean,
            penetration: Int
        ): String {
            viewBinding.apply {
                var damageCube = ""
                if (damageX>1){
                    damageCube = "${damageX}d$damageY"
                    damage = "${damageX}d$damageY"
                }
                else{
                    damageCube = "d$damageY"
                    damage = "d$damageY"
                }
                if (damageZ > 0){
                    damageCube += " + $damageZ"
                    damage += "+$damageZ"
                }
                if (bonusPower){
                    damageCube = damageCube + " + " + Player.getInstance().getBonusPower() + "(БС)"
                    damage += "+${Player.getInstance().getBonusPower()}"
                }
                if (penetration>0){
                    penetrationVisible.isVisible = true
                    tvPenetration.text = penetration.toString()
                }
                else{
                    penetrationVisible.isVisible = false
                }
                return damageCube
            }
        }

        private fun changeBonusDamage(change: Int){
            bonusDamage+= change
            viewBinding.tvBonusDamage.text = bonusDamage.toString()
        }

        private fun refreshPatrons(viewModel: AttackViewModel, battleViewModel: BattleViewModel){
            clips.sortDescending()
            viewModel.refreshPatrons(viewModel.activeWeaponLiveData.value!!.id, clips[1], clips[2],clips[3])
            battleViewModel.updateStepInitiative(-2)
        }

        private fun changeBonusHit(change: Int){
            bonusHit+= change
            viewBinding.tvHitBonusArsenalB.text = bonusHit.toString()
        }

    }
}