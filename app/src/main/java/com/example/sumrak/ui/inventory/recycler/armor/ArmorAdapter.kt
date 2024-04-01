package com.example.sumrak.ui.inventory.recycler.armor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.R
import com.example.sumrak.databinding.MaketInventoryArmorBinding
import com.example.sumrak.ui.inventory.InventoryViewModel

import com.example.sumrak.ui.inventory.recycler.DelegateAdapter
import com.example.sumrak.ui.inventory.recycler.armor.item.ArmorItemAdapter
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception

class ArmorAdapter(
    private val viewModel: ArmorViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val context: Context,
    private val inventoryViewModel: InventoryViewModel
) : DelegateAdapter<Armor, ArmorAdapter.ArmorViewHolder, ArmorViewModel>() {
    override fun onCreateViewHolder(parent: ViewGroup): ArmorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.maket_inventory_armor, parent, false)
        return ArmorViewHolder(view, viewModel, lifecycleOwner, context, inventoryViewModel)
    }

    override fun onBindViewHolder(holder: ArmorViewHolder, item: Armor) {
        holder.bind(item, viewModel, lifecycleOwner, context, inventoryViewModel)
    }

    class ArmorViewHolder(
        itemView: View,
        viewModel: ArmorViewModel,
        lifecycleOwner: LifecycleOwner,
        context: Context,
        inventoryViewModel: InventoryViewModel
    ) : RecyclerView.ViewHolder(itemView) {
        private val viewBinding = MaketInventoryArmorBinding.bind(itemView)

        init {
            inventoryViewModel.armorVisibility.observe(lifecycleOwner) {
                viewBinding.armorVisible.isVisible = it
            }
            viewModel.modeSettings.observe(lifecycleOwner) {
                when(it){
                    0 -> modeEsc(context)
                    1 -> modeAdd()
                    2 -> modeSettings(viewModel)
                    3 -> modeRepair(viewModel)
                }
            }
            viewModel.repairProgress.observe(lifecycleOwner) {
                viewBinding.apply {
                    progressEnduranceArmor.setProgress(it, true)
                    tvEnduranceArmor.text = it.toString()
                }
            }
        }

        fun bind(
            armor: Armor,
            viewModel: ArmorViewModel,
            lifecycleOwner: LifecycleOwner,
            context: Context,
            inventoryViewModel: InventoryViewModel
        ) {
            inventoryViewModel.getVisibleView(armor.name)
            viewBinding.apply {
                armorRecView.layoutManager = LinearLayoutManager(context)
                armorRecView.adapter = ArmorItemAdapter(viewModel, lifecycleOwner)

                tvArmors.setOnClickListener { inventoryViewModel.updateVisibleView(armor.name) }
                addBtnArmor.setOnClickListener { viewModel.modeAddArmor(true) }
                btnEcsSettingsArmor.setOnClickListener { viewModel.modeAddArmor(false) }
                btnSaveArmor.setOnClickListener { saveArmor(viewModel) }

                btnAddRepairArmor.setOnClickListener { viewModel.updateArmorToRepair(1) }
                btnRemRepairArmor.setOnClickListener { viewModel.updateArmorToRepair(-1) }

                rgBtnGroup.setOnCheckedChangeListener { _, checkedId ->
                    when (checkedId) {
                        rBtnLight.id -> viewModel.setClassArmor("Легкая")
                        rBtnMedium.id -> viewModel.setClassArmor("Средняя")
                        rBtnHard.id -> viewModel.setClassArmor("Тяжелая")
                    }
                }

                btnDeleteArmor.setOnClickListener { viewModel.deleteArmor() }
            }

        }

        private fun saveArmor(viewModel: ArmorViewModel){
            if (viewModel.armorItem.classArmor != ""){
                try {
                    viewBinding.apply {
                        val name = editNameArmor.text.toString()
                        val params = editParamsArmor.text.toString().toInt()
                        val endurance = editEnduranceArmor.text.toString().toInt()
                        val features = editFeaturesArmor.text.toString()
                        if (viewModel.armorItem.id == 0) {
                            viewModel.addArmor(name, params, endurance, features)
                        } else {
                            viewModel.updateArmor(name, params, endurance, features)
                        }
                    }
                }
                catch (e: Exception){
                    Snackbar.make(viewBinding.root, "Не заполнены значения Параметра и/или Прочности брони!", Snackbar.LENGTH_SHORT).show()
                }

            }
            else Snackbar.make(viewBinding.root, "Не выбран класс брони!", Snackbar.LENGTH_SHORT).show()
        }

        private fun modeRepair(viewModel: ArmorViewModel) {
            viewBinding.apply {
                tvModeSettingsArmor.text = "Ремонт брони"
                addBtnArmor.isVisible = false
                addModeArmor.isVisible = true
                btnDeleteArmor.isVisible = false
                repairModeArmor.isVisible = true
                settingsMode.isVisible = false
                val item = viewModel.armorItem
                tvRepairClassArmor.text = item.classArmor
                tvRepairNameArmor.text = item.name
                tvEnduranceArmor.text = item.endurance.toString()
                progressEnduranceArmor.max = item.enduranceMax
            }
        }

        private fun modeAdd(){
            viewBinding.apply {
                tvModeSettingsArmor.text = "Добавить броню"
                addBtnArmor.isVisible = false
                addModeArmor.isVisible = true
                btnDeleteArmor.isVisible = false
                repairModeArmor.isVisible = false
                settingsMode.isVisible = true
            }
        }
        private fun modeSettings(viewModel: ArmorViewModel) {
            viewBinding.apply {
                tvModeSettingsArmor.text = "Редактировать броню"
                addBtnArmor.isVisible = false
                repairModeArmor.isVisible = false
                addModeArmor.isVisible = true
                btnDeleteArmor.isVisible = true
                settingsMode.isVisible = true
                val item = viewModel.armorItem
                editNameArmor.setText(item.name)
                editEnduranceArmor.setText(item.enduranceMax.toString())
                editFeaturesArmor.setText(item.features)
                editParamsArmor.setText(item.params.toString())
                when (item.classArmor) {
                    "Легкая" -> rBtnLight.isChecked = true
                    "Средняя" -> rBtnMedium.isChecked = true
                    "Тяжелая" -> rBtnHard.isChecked = true
                }
            }
        }
        private fun modeEsc(context: Context) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm.isAcceptingText) {
                // Закрываем клавиатуру
                imm.hideSoftInputFromWindow(itemView.windowToken, 0)
            }
            viewBinding.apply {
                addBtnArmor.isVisible = true
                addModeArmor.isVisible = false
                editEnduranceArmor.text.clear()
                editNameArmor.text.clear()
                editFeaturesArmor.text.clear()
                editParamsArmor.text.clear()
                rgBtnGroup.clearCheck()
            }
        }

    }
}
