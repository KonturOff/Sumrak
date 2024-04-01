package com.example.sumrak.ui.inventory.recycler.effects

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
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
): DelegateAdapter<Effects, EffectsAdapter.EffectsViewHolder, EffectsViewModel>()
{


    override fun onCreateViewHolder(parent: ViewGroup): EffectsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.maket_inventory_effects, parent,false)
        return EffectsViewHolder(view, viewModel, lifecycleOwner, context, inventoryViewModel)
    }

    override fun onBindViewHolder(holder: EffectsViewHolder, item: Effects) {
        holder.bind(item, viewModel, lifecycleOwner, context, inventoryViewModel)
    }

    class EffectsViewHolder(
        itemView: View,
        viewModel: EffectsViewModel,
        lifecycleOwner: LifecycleOwner,
        context: Context,
        inventoryViewModel: InventoryViewModel
    ) : RecyclerView.ViewHolder(itemView){
        val b = MaketInventoryEffectsBinding.bind(itemView)


        init {
            val tvValue = mutableListOf(b.tvDb, b.tvBb, b.tvPower, b.tvDexterity,
                b.tvVolition, b.tvEndurance, b.tvIntelect, b.tvInsihgt, b.tvObservation,
                b.tvChsarisma, b.tvBonusPower, b.tvBonusEndurance)

            viewModel.modeSettings.observe(lifecycleOwner) {
                if (it > 0) {
                    modeAdd(viewModel, tvValue)
                    if (it == 2) {
                        modeSettings(viewModel, tvValue)
                    }
                } else {
                    modeEsc(viewModel, tvValue, context)
                }
            }

            viewModel.indexTvValue.observe(lifecycleOwner) {
                if (it != 20) {
                    b.tvValueParamEffects.text = tvValue[it].text
                } else b.tvValueParamEffects.text = ""

            }

            inventoryViewModel.effectsVisibility.observe(lifecycleOwner) {
                if (it) {
                    b.effectsVisible.visibility = View.VISIBLE
                } else b.effectsVisible.visibility = View.GONE
            }
        }



        fun bind(
            item: Effects,
            viewModel: EffectsViewModel,
            lifecycleOwner: LifecycleOwner,
            context: Context,
            inventoryViewModel: InventoryViewModel
        ) {


            val radioButtons = mutableListOf(b.rBtnDB, b.rBtnBB, b.rBtnPower,
                b.rBtnDexterity, b.rBtnVolition, b.rBtnEndurance, b.rBtnIntelect,
                b.rBtnInsihgt, b.rBtnObservation, b.rBtnChsarisma, b.rBtnAllParams,
                b.rBtnBonusPower, b.rBtnBonusEndurance)

            val tvValue = mutableListOf(b.tvDb, b.tvBb, b.tvPower, b.tvDexterity,
                b.tvVolition, b.tvEndurance, b.tvIntelect, b.tvInsihgt, b.tvObservation,
                b.tvChsarisma, b.tvBonusPower, b.tvBonusEndurance)



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

            b.tvEffects.setOnClickListener { inventoryViewModel.updateVisbleView(item.name) }


            b.effectsRecView.layoutManager = LinearLayoutManager(context)
            b.effectsRecView.adapter = EffectsItemAdapter(viewModel)

            b.addBtnEffects.setOnClickListener { viewModel.modeSettings(true) }
            b.btnEscSettingsEffects.setOnClickListener { viewModel.modeSettings(false) }

            b.btnRemValueEffects.setOnClickListener {
                updateTvViewSettngs(-1, viewModel, tvValue)
            }
            b.btnAddValueEffects.setOnClickListener {
                updateTvViewSettngs(1, viewModel, tvValue)
            }

            b.btnSaveEffects.setOnClickListener {
                if (viewModel.modeSettings.value==1){
                    viewModel.addEffect(b.editNameEffects.text.toString())
                }
                else viewModel.updateEffect(b.editNameEffects.text.toString())
                viewModel.modeSettings(false)
            }



        }

        private fun updateTvViewSettngs(
            t: Int,
            viewModel: EffectsViewModel,
            tvValue: MutableList<TextView>
        ){
            val list  = viewModel.changeValueSettings(t)
            for (i in 0..<tvValue.size){
                tvValue[i].text = list[i].toString()
            }
            if (viewModel.index==-1){
                b.tvValueParamEffects.text = ""
            }
            else if (viewModel.index!=20){
                b.tvValueParamEffects.text = list[viewModel.index].toString()
            }


        }
        private fun modeAdd(viewModel: EffectsViewModel, tvValue: MutableList<TextView>) {
            b.tvSettingsEffects.text = "Добавить эффект"
            val list = viewModel.effectConstructor
            for (i in 0..<tvValue.size){
                tvValue[i].text = list[i].toString()
            }
            b.addBtnEffects.visibility = View.GONE
            b.EffectsSetingsVisibile.visibility = View.VISIBLE
        }
        private fun modeSettings(viewModel: EffectsViewModel, tvValue: MutableList<TextView>) {
            b.tvSettingsEffects.text = "Редактировать эффект"
            b.editNameEffects.setText(viewModel.nameEff)
        }
        private fun modeEsc(viewModel: EffectsViewModel, tvValue: MutableList<TextView>, context: Context) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm.isAcceptingText) {
                // Закрываем клавиатуру
                imm.hideSoftInputFromWindow(itemView.windowToken, 0)
            }
            b.addBtnEffects.visibility = View.VISIBLE
            b.EffectsSetingsVisibile.visibility = View.GONE
            clearSettingsValue(tvValue, viewModel)
        }

        private fun clearSettingsValue(tvValue: MutableList<TextView>, viewModel: EffectsViewModel) {
            b.editNameEffects.text.clear()
            b.rBtnGroup2.clearCheck()
            b.rBtnGroup1.clearCheck()
            updateTvViewSettngs(0, viewModel, tvValue)
        }
    }
}