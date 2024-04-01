package com.example.sumrak.ui.inventory.recycler.equipment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
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

    override fun onCreateViewHolder(parent: ViewGroup): EquipmentAdapter.EquipmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.maket_inventory_equipment, parent, false)
        return EquipmentViewHolder(view, viewModel, lifecycleOwner, context, inventoryViewModel)
    }

    override fun onBindViewHolder(holder: EquipmentAdapter.EquipmentViewHolder, item: Equipment) {
        holder.bind(item, viewModel, lifecycleOwner, context, inventoryViewModel, inventoryFragment)
    }

    class EquipmentViewHolder(
        itemView: View,
        viewModel: EquipmentViewModel,
        lifecycleOwner: LifecycleOwner,
        context: Context,
        inventoryViewModel: InventoryViewModel
    ) : RecyclerView.ViewHolder(itemView) {
        val b = MaketInventoryEquipmentBinding.bind(itemView)

        init {
            viewModel.modeSettings.observe(lifecycleOwner, Observer {
                setMode(it)
            })

            viewModel.equipmentItem.observe(lifecycleOwner, Observer {
                b.editNameEquipment.setText(it.name)
                b.editMaxCharge.setText(it.maxCharge.toString())
                b.editStep.setText(it.step.toString())
                b.cBoxTest.isChecked = it.test
                b.tvNameEqipmentRp.text = it.name
                b.tvRepairValueEquipment.text = it.charge.toString()
                b.pbRepairEquipment.max = it.maxCharge
                b.pbRepairEquipment.setProgress(it.charge, true)
                b.btnAddRepairEquipment.isEnabled = it.charge < it.maxCharge
                b.btnUseEquipment.isEnabled = it.charge > 0
                b.btnReplaceEquipment.isEnabled = it.charge < it.maxCharge
            })

            inventoryViewModel.equipmentVisibility.observe(lifecycleOwner, Observer {
                if (it){
                    b.equipmentVisible.visibility = View.VISIBLE
                }
                else b.equipmentVisible.visibility = View.GONE
            })
        }

        fun bind(
            item: Equipment,
            viewModel: EquipmentViewModel,
            lifecycleOwner: LifecycleOwner,
            context: Context,
            inventoryViewModel: InventoryViewModel,
            inventoryFragment: InventoryFragment
        ) {
            inventoryViewModel.getVisibleView(item.name)

            b.equipmentRecView.layoutManager = LinearLayoutManager(context)
            b.equipmentRecView.adapter = EquipmentItemAdapter(viewModel)

            b.tvEquipment.setOnClickListener { inventoryViewModel.updateVisbleView(item.name) }

            b.btnAddEquipment.setOnClickListener { viewModel.setMode(1, 0) }

            b.btnEsc.setOnClickListener { viewModel.setMode(0, 0) }
            b.btnSaveEquipment.setOnClickListener { saveEquipment(viewModel)}
            b.btnDeleteEquipment.setOnClickListener { viewModel.deleteEquipmentItem(viewModel.activeIdItemSettings) }
            b.btnAddRepairEquipment.setOnClickListener { viewModel.updateChargeEquipment(viewModel.activeIdItemRepair, 1) }
            b.btnRemRepairEquipment.setOnClickListener { viewModel.updateChargeEquipment(viewModel.activeIdItemRepair, -1) }
            b.btnReplaceEquipment.setOnClickListener { viewModel.replaceChangeEquipment(viewModel.activeIdItemRepair) }
            b.btnUseEquipment.setOnClickListener { useEquipment(viewModel, inventoryFragment)}
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
                val name = b.editNameEquipment.text.toString()
                val charge = b.editMaxCharge.text.toString().toInt()
                val step = b.editStep.text.toString().toInt()
                val test = b.cBoxTest.isChecked

                if (viewModel.activeIdItemSettings==0){
                    viewModel.addEquipmentItem(name, charge, step, test)
                }
                else{
                    viewModel.updateEquipmentItem(EquipmentItem(
                        id = viewModel.activeIdItemSettings,
                        idPlayer = Player.getInstance().getIdActivePlayer(),
                        name = name,
                        charge = charge,
                        maxCharge = charge,
                        step = step,
                        test = test
                    ))
                }

                viewModel.setMode(0, 0)
            }
            catch (e: Exception) {
                Snackbar.make(
                    b.root,
                    "Не заполнены значения Заряда и/или Степень убывания!",
                    Snackbar.LENGTH_SHORT
                ).show()
            }

        }


        private fun setMode(mode: Int){
            when(mode){
                4 -> {
                    b.tvSettingsEquipment.text = "Управление Зарядом"
                    b.settingsEquipment.visibility = View.GONE
                    b.repairEquipment.visibility = View.VISIBLE
                    b.addModeEquipment.visibility = View.VISIBLE
                    b.btnAddEquipment.visibility = View.GONE
                }
                3 -> {
                    b.tvSettingsEquipment.text = "Редактировать Снаряжение"
                    b.settingsEquipment.visibility = View.VISIBLE
                    b.repairEquipment.visibility = View.GONE
                    b.addModeEquipment.visibility = View.VISIBLE
                    b.btnAddEquipment.visibility = View.GONE
                    b.btnDeleteEquipment.visibility = View.VISIBLE

                }
                1 -> {
                    b.tvSettingsEquipment.text = "Добавить снаряжение"
                    b.repairEquipment.visibility = View.GONE
                    b.addModeEquipment.visibility = View.VISIBLE
                    b.settingsEquipment.visibility = View.VISIBLE
                    b.btnAddEquipment.visibility = View.GONE
                    b.btnDeleteEquipment.visibility = View.GONE
                }
                else -> {
                    b.repairEquipment.visibility = View.GONE
                    b.addModeEquipment.visibility = View.GONE
                    b.btnAddEquipment.visibility = View.VISIBLE
                }
            }
        }


    }
}