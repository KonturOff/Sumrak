package com.example.sumrak.ui.inventory.recycler.armor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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
        private val b = MaketInventoryArmorBinding.bind(itemView)

        init {
            inventoryViewModel.armorVisibility.observe(lifecycleOwner) {
                if (it) {
                    b.armorVisible.visibility = View.VISIBLE
                } else b.armorVisible.visibility = View.GONE
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
                b.progressEnduranceArmor.setProgress(it, true)
                b.tvEnduranceArmor.text = it.toString()
            }
        }

        fun bind(
            armor: Armor,
            viewModel: ArmorViewModel,
            lifecycleOwner: LifecycleOwner,
            context: Context,
            inventoryViewModel: InventoryViewModel
        ){
            inventoryViewModel.getVisibleView(armor.name)

            b.armorRecView.layoutManager = LinearLayoutManager(context)
            b.armorRecView.adapter = ArmorItemAdapter(viewModel, lifecycleOwner)

            b.tvArmors.setOnClickListener { inventoryViewModel.updateVisbleView(armor.name) }
            b.addBtnArmor.setOnClickListener { viewModel.modeAddArmor(true) }
            b.btnEcsSettingsArmor.setOnClickListener { viewModel.modeAddArmor(false) }
            b.btnSaveArmor.setOnClickListener { saveArmor(viewModel) }

            b.btnAddRepairArmor.setOnClickListener { viewModel.updateArmorToRepair(1) }
            b.btnRemRepairArmor.setOnClickListener { viewModel.updateArmorToRepair(-1) }

            b.rgBtnGroup.setOnCheckedChangeListener { _, checkedId ->
                when(checkedId){
                    b.rBtnLight.id -> viewModel.setClassArmor("Легкая")
                    b.rBtnMedium.id -> viewModel.setClassArmor("Средняя")
                    b.rBtnHard.id -> viewModel.setClassArmor("Тяжелая")
                }
            }

            b.btnDeleteArmor.setOnClickListener { viewModel.deleteArmor()}

            }

        private fun saveArmor(viewModel: ArmorViewModel){
            if (viewModel.armorItem.classArmor != ""){
                try {
                    val name = b.editNameArmor.text.toString()
                    val params = b.editParamsArmor.text.toString().toInt()
                    val endurance = b.editEnduranceArmor.text.toString().toInt()
                    val features = b.editFeaturesArmor.text.toString()
                    if (viewModel.armorItem.id == 0){
                        viewModel.addArmor(name, params, endurance, features)
                    }
                    else{
                        viewModel.updateArmor(name, params, endurance, features)
                    }
                }
                catch (e: Exception){
                    Snackbar.make(b.root, "Не заполнены значения Параметра и/или Прочности брони!", Snackbar.LENGTH_SHORT).show()
                }

            }
            else Snackbar.make(b.root, "Не выбран класс брони!", Snackbar.LENGTH_SHORT).show()
        }

        private fun modeRepair(viewModel: ArmorViewModel){
            b.tvModeSettingsArmor.text = "Ремонт брони"
            b.addBtnArmor.visibility = View.GONE
            b.addModeArmor.visibility = View.VISIBLE
            b.btnDeleteArmor.visibility = View.GONE
            b.repairModeArmor.visibility = View.VISIBLE
            b.settingsMode.visibility = View.GONE
            val item = viewModel.armorItem
            b.tvRepairClassArmor.text = item.classArmor
            b.tvRepairNameArmor.text = item.name
            b.tvEnduranceArmor.text = item.endurance.toString()
            b.progressEnduranceArmor.max = item.enduranceMax
        }

        private fun modeAdd(){
            b.tvModeSettingsArmor.text = "Добавить броню"
            b.addBtnArmor.visibility = View.GONE
            b.addModeArmor.visibility = View.VISIBLE
            b.btnDeleteArmor.visibility = View.GONE
            b.repairModeArmor.visibility = View.GONE
            b.settingsMode.visibility = View.VISIBLE
        }
        private fun modeSettings(viewModel: ArmorViewModel) {
            b.tvModeSettingsArmor.text = "Редактировать броню"
            b.addBtnArmor.visibility = View.GONE
            b.repairModeArmor.visibility = View.GONE
            b.addModeArmor.visibility = View.VISIBLE
            b.btnDeleteArmor.visibility = View.VISIBLE
            b.settingsMode.visibility = View.VISIBLE
            val item = viewModel.armorItem
            b.editNameArmor.setText(item.name)
            b.editEnduranceArmor.setText(item.enduranceMax.toString())
            b.editFeaturesArmor.setText(item.features)
            b.editParamsArmor.setText(item.params.toString())
            when(item.classArmor){
                "Легкая"-> b.rBtnLight.isChecked = true
                "Средняя"-> b.rBtnMedium.isChecked = true
                "Тяжелая" -> b.rBtnHard.isChecked = true
            }

        }
        private fun modeEsc(context: Context) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm.isAcceptingText) {
                // Закрываем клавиатуру
                imm.hideSoftInputFromWindow(itemView.windowToken, 0)
            }
            b.addBtnArmor.visibility = View.VISIBLE
            b.addModeArmor.visibility = View.GONE
            b.editEnduranceArmor.text.clear()
            b.editNameArmor.text.clear()
            b.editFeaturesArmor.text.clear()
            b.editParamsArmor.text.clear()
            b.rgBtnGroup.clearCheck()
        }

    }
}
