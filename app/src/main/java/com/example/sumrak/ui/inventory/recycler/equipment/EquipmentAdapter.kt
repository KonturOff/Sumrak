package com.example.sumrak.ui.inventory.recycler.equipment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.Player
import com.example.sumrak.R
import com.example.sumrak.databinding.MaketInventoryEquipmentBinding
import com.example.sumrak.ui.inventory.InventoryFragment
import com.example.sumrak.ui.inventory.InventoryViewModel
import com.example.sumrak.ui.inventory.recycler.DelegateAdapter
import com.example.sumrak.ui.inventory.recycler.equipment.item.EquipmentItem
import com.example.sumrak.ui.inventory.recycler.equipment.item.EquipmentItemAdapter
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception


class EquipmentAdapter(
    private val viewModel: EquipmentViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val context: Context,
    private val inventoryViewModel: InventoryViewModel,
    private val inventoryFragment: InventoryFragment
) : DelegateAdapter<Equipment, EquipmentAdapter.EquipmentViewHolder, EquipmentViewModel>() {
    lateinit var item: EquipmentItem

    override fun onCreateViewHolder(parent: ViewGroup): EquipmentViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.maket_inventory_equipment, parent, false)
        return EquipmentViewHolder(
            view, 
            viewModel, 
            lifecycleOwner, 
//            context, 
            inventoryViewModel
        )
    }

    override fun onBindViewHolder(holder: EquipmentViewHolder, item: Equipment) {
        holder.bind(
            item, 
            viewModel, 
//            lifecycleOwner, 
            context, 
            inventoryViewModel, 
            inventoryFragment
        )
    }

    class EquipmentViewHolder(
        itemView: View,
        viewModel: EquipmentViewModel,
        lifecycleOwner: LifecycleOwner,
//        context: Context,
        inventoryViewModel: InventoryViewModel
    ) : RecyclerView.ViewHolder(itemView) {
        private val viewBinding = MaketInventoryEquipmentBinding.bind(itemView)

        init {
            viewModel.modeSettings.observe(lifecycleOwner) {
                setMode(it)
            }

            viewModel.equipmentItem.observe(lifecycleOwner) {
                viewBinding.apply {
                    editNameEquipment.setText(it.name)
                    editMaxCharge.setText(it.maxCharge.toString())
                    editStep.setText(it.step.toString())
                    cBoxTest.isChecked = it.test
                    tvNameEqipmentRp.text = it.name
                    tvRepairValueEquipment.text = it.charge.toString()
                    pbRepairEquipment.max = it.maxCharge
                    pbRepairEquipment.setProgress(it.charge, true)
                    btnAddRepairEquipment.isEnabled = it.charge < it.maxCharge
                    btnUseEquipment.isEnabled = it.charge > 0
                    btnReplaceEquipment.isEnabled = it.charge < it.maxCharge
                }
            }

            inventoryViewModel.equipmentVisibility.observe(lifecycleOwner) {
                viewBinding.equipmentVisible.isVisible = it
            }
        }

        fun bind(
            item: Equipment,
            viewModel: EquipmentViewModel,
//            lifecycleOwner: LifecycleOwner,
            context: Context,
            inventoryViewModel: InventoryViewModel,
            inventoryFragment: InventoryFragment
        ) {
            inventoryViewModel.getVisibleView(item.name)
            viewBinding.apply {
                equipmentRecView.layoutManager = LinearLayoutManager(context)
                equipmentRecView.adapter = EquipmentItemAdapter(viewModel)

                tvEquipment.setOnClickListener {
                    inventoryViewModel.updateVisibleView(
                        item.name
                    )
                }

                btnAddEquipment.setOnClickListener { viewModel.setMode(1, 0) }

                btnEsc.setOnClickListener { viewModel.setMode(0, 0) }
                btnSaveEquipment.setOnClickListener { saveEquipment(viewModel) }
                btnDeleteEquipment.setOnClickListener {
                    viewModel.deleteEquipmentItem(
                        viewModel.activeIdItemSettings
                    )
                }
                btnAddRepairEquipment.setOnClickListener {
                    viewModel.updateChargeEquipment(
                        viewModel.activeIdItemRepair,
                        1
                    )
                }
                btnRemRepairEquipment.setOnClickListener {
                    viewModel.updateChargeEquipment(
                        viewModel.activeIdItemRepair,
                        -1
                    )
                }
                btnReplaceEquipment.setOnClickListener {
                    viewModel.replaceChangeEquipment(
                        viewModel.activeIdItemRepair
                    )
                }
                btnUseEquipment.setOnClickListener {
                    useEquipment(
                        viewModel,
                        inventoryFragment
                    )
                }
            }
        }

        private fun useEquipment(
            viewModel: EquipmentViewModel,
            inventoryFragment: InventoryFragment
        ) {
            val item = viewModel.getActiveRepairItem()
            if (item.test){
                inventoryFragment.useEquipment("Убывающий Тест ${item.name}", item.charge)
            }
            viewModel.useEquipmentChange(viewModel.activeIdItemRepair)
        }

        private fun saveEquipment(viewModel: EquipmentViewModel) {
            try {
                viewBinding.apply {
                    val name = editNameEquipment.text.toString()
                    val charge = editMaxCharge.text.toString().toInt()
                    val step = editStep.text.toString().toInt()
                    val test = cBoxTest.isChecked

                    if (viewModel.activeIdItemSettings == 0) {
                        viewModel.addEquipmentItem(name, charge, step, test)
                    } else {
                        viewModel.updateEquipmentItem(
                            EquipmentItem(
                                id = viewModel.activeIdItemSettings,
                                idPlayer = Player.getInstance().getIdActivePlayer(),
                                name = name,
                                charge = charge,
                                maxCharge = charge,
                                step = step,
                                test = test
                            )
                        )
                    }

                    viewModel.setMode(0, 0)
                }
            }
            catch (e: Exception) {
                Snackbar.make(
                    viewBinding.root,
                    "Не заполнены значения Заряда и/или Степень убывания!",
                    Snackbar.LENGTH_SHORT
                ).show()
            }

        }

        private fun setMode(mode: Int) {
            viewBinding.apply {
                when (mode) {
                    4 -> {
                        tvSettingsEquipment.text = "Управление Зарядом"
                        settingsEquipment.isVisible = false
                        repairEquipment.isVisible = true
                        addModeEquipment.isVisible = true
                        btnAddEquipment.isVisible = false
                    }

                    3 -> {
                        tvSettingsEquipment.text = "Редактировать Снаряжение"
                        settingsEquipment.isVisible = true
                        repairEquipment.isVisible = false
                        addModeEquipment.isVisible = true
                        btnAddEquipment.isVisible = false
                        btnDeleteEquipment.isVisible = true
                    }

                    1 -> {
                        tvSettingsEquipment.text = "Добавить снаряжение"
                        repairEquipment.isVisible = false
                        addModeEquipment.isVisible = true
                        settingsEquipment.isVisible = true
                        btnAddEquipment.isVisible = false
                        btnDeleteEquipment.isVisible = false
                    }

                    else -> {
                        repairEquipment.isVisible = false
                        addModeEquipment.isVisible = false
                        btnAddEquipment.isVisible = true
                    }
                }
            }
        }
    }
}