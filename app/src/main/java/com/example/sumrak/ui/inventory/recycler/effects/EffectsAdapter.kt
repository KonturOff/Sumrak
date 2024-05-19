package com.example.sumrak.ui.inventory.recycler.effects

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.R
import com.example.sumrak.databinding.MaketInventoryEffectsBinding
import com.example.sumrak.ui.inventory.InventoryViewModel
import com.example.sumrak.ui.inventory.recycler.DelegateAdapter
import com.example.sumrak.ui.inventory.recycler.effects.item.EffectsItemAdapter


class EffectsAdapter(
    private val viewModel: EffectsViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val context: Context,
    private val inventoryViewModel: InventoryViewModel
): DelegateAdapter<Effects, EffectsAdapter.EffectsViewHolder, EffectsViewModel>() {

    override fun onCreateViewHolder(parent: ViewGroup): EffectsViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.maket_inventory_effects, parent,false)
        return EffectsViewHolder(view, viewModel, lifecycleOwner, context, inventoryViewModel)
    }

    override fun onBindViewHolder(holder: EffectsViewHolder, item: Effects) {
        holder.bind(
            item,
            viewModel,
//            lifecycleOwner,
            context,
            inventoryViewModel
        )
    }

    class EffectsViewHolder(
        itemView: View,
        viewModel: EffectsViewModel,
        lifecycleOwner: LifecycleOwner,
        context: Context,
        inventoryViewModel: InventoryViewModel
    ) : RecyclerView.ViewHolder(itemView){
        val viewBinding = MaketInventoryEffectsBinding.bind(itemView)

        init {
            viewBinding.apply {
                val tvValue = mutableListOf(
                    tvDb,
                    tvBb,
                    tvPower,
                    tvDexterity,
                    tvVolition,
                    tvEndurance,
                    tvIntelect,
                    tvInsihgt,
                    tvObservation,
                    tvChsarisma,
                    tvBonusPower,
                    tvBonusEndurance,
                    tvBonusInititive,
                    tvBonusReaction
                )

                viewModel.modeSettings.observe(lifecycleOwner) {
                    if (it > 0) {
                        modeAdd(viewModel, tvValue)
                        if (it == 2) {
                            modeSettings(viewModel)//, tvValue)
                        }
                    } else {
                        modeEsc(viewModel, tvValue, context)
                    }
                }

                viewModel.indexTvValue.observe(lifecycleOwner) {
                    if (it != 20) {
                        tvValueParamEffects.text = tvValue[it].text
                    } else tvValueParamEffects.text = ""

                }

                inventoryViewModel.effectsVisibility.observe(lifecycleOwner) {
                    effectsVisible.isVisible = it
                }
            }
        }

        fun bind(
            item: Effects,
            viewModel: EffectsViewModel,
//            lifecycleOwner: LifecycleOwner,
            context: Context,
            inventoryViewModel: InventoryViewModel
        ) {
            viewBinding.apply {
                val radioButtons = mutableListOf(
                    rBtnDB,
                    rBtnBB,
                    rBtnPower,
                    rBtnDexterity,
                    rBtnVolition,
                    rBtnEndurance,
                    rBtnIntelect,
                    rBtnInsihgt,
                    rBtnObservation,
                    rBtnChsarisma,
                    rBtnAllParams,
                    rBtnBonusPower,
                    rBtnBonusEndurance,
                    rBtnBonusInitiative,
                    rBtnBonusReaction
                )

                val tvValue = mutableListOf(
                    tvDb,
                    tvBb,
                    tvPower,
                    tvDexterity,
                    tvVolition,
                    tvEndurance,
                    tvIntelect,
                    tvInsihgt,
                    tvObservation,
                    tvChsarisma,
                    tvBonusPower,
                    tvBonusEndurance,
                    tvBonusInititive,
                    tvBonusReaction
                )

                for (radioButton in radioButtons) {
                    radioButton.setOnClickListener {
                        for (button in radioButtons) {
                            if (button == radioButton) {
                                button.isChecked = true
                                viewModel.observationRadioButton(button.text.toString())
                            } else {
                                button.isChecked = false
                            }
                        }
                    }
                }
                inventoryViewModel.getVisibleView(item.name)

                tvEffects.setOnClickListener { inventoryViewModel.updateVisibleView(item.name) }


                effectsRecView.layoutManager = LinearLayoutManager(context)
                effectsRecView.adapter = EffectsItemAdapter(viewModel)

                addBtnEffects.setOnClickListener { viewModel.modeSettings(true) }
                btnEscSettingsEffects.setOnClickListener { viewModel.modeSettings(false) }

                btnRemValueEffects.setOnClickListener {
                    updateTvViewSettings(-1, viewModel, tvValue)
                }
                btnAddValueEffects.setOnClickListener {
                    updateTvViewSettings(1, viewModel, tvValue)
                }

                btnSaveEffects.setOnClickListener {
                    if (viewModel.modeSettings.value == 1) {
                        viewModel.addEffect(editNameEffects.text.toString())
                    } else viewModel.updateEffect(editNameEffects.text.toString())
                    viewModel.modeSettings(false)
                }
            }
        }

        private fun updateTvViewSettings(
            t: Int,
            viewModel: EffectsViewModel,
            tvValue: MutableList<TextView>
        ){
            val list  = viewModel.changeValueSettings(t)
            for (i in 0..< tvValue.size) {
                tvValue[i].text = list[i].toString()
            }
            if (viewModel.index == -1) {
                viewBinding.tvValueParamEffects.text = ""
            }
            else if (viewModel.index != 20) {
                viewBinding.tvValueParamEffects.text = list[viewModel.index].toString()
            }


        }
        private fun modeAdd(viewModel: EffectsViewModel, tvValue: MutableList<TextView>) {
            viewBinding.tvSettingsEffects.text = "Добавить эффект"
            val list = viewModel.effectConstructor
            for (i in 0..<tvValue.size){
                tvValue[i].text = list[i].toString()
            }
            viewBinding.apply {
                addBtnEffects.isVisible = false
                EffectsSetingsVisibile.isVisible = true
            }
        }
        private fun modeSettings(
            viewModel: EffectsViewModel,
//            tvValue: MutableList<TextView>
        ) {
            viewBinding.apply {
                tvSettingsEffects.text = "Редактировать эффект"
                editNameEffects.setText(viewModel.nameEff)
            }
        }
        private fun modeEsc(viewModel: EffectsViewModel, tvValue: MutableList<TextView>, context: Context) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm.isAcceptingText) {
                // Закрываем клавиатуру
                imm.hideSoftInputFromWindow(itemView.windowToken, 0)
            }
            viewBinding.apply {
                addBtnEffects.isVisible = true
                EffectsSetingsVisibile.isVisible = false
            }
            clearSettingsValue(tvValue, viewModel)
        }

        private fun clearSettingsValue(tvValue: MutableList<TextView>, viewModel: EffectsViewModel) {
            viewBinding.apply {
                editNameEffects.text.clear()
                rBtnGroup2.clearCheck()
                rBtnGroup1.clearCheck()
            }
            updateTvViewSettings(0, viewModel, tvValue)
        }
    }
}