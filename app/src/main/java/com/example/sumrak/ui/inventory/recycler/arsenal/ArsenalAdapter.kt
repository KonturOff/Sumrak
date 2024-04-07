package com.example.sumrak.ui.inventory.recycler.arsenal

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
import com.example.sumrak.databinding.MaketInventoryArsenalBinding
import com.example.sumrak.ui.inventory.InventoryViewModel
import com.example.sumrak.ui.inventory.recycler.DelegateAdapter
import com.example.sumrak.ui.inventory.recycler.arsenal.item.ArsenalItem
import com.example.sumrak.ui.inventory.recycler.arsenal.item.ArsenalItemAdapter
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception

class ArsenalAdapter(
    private val viewModel: ArsenalViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val context: Context,
    private val inventoryViewModel: InventoryViewModel
) : DelegateAdapter<Arsenal, ArsenalAdapter.ArsenalViewHolder, ArsenalViewModel>() {


    override fun onCreateViewHolder(parent: ViewGroup): ArsenalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.maket_inventory_arsenal, parent, false)
        return ArsenalViewHolder(view, viewModel, lifecycleOwner, context, inventoryViewModel)
    }

    override fun onBindViewHolder(holder: ArsenalViewHolder, item: Arsenal) {
        holder.bind(item, viewModel, lifecycleOwner, context, inventoryViewModel)
    }
    class ArsenalViewHolder(
        itemView: View,
        viewModel: ArsenalViewModel,
        lifecycleOwner: LifecycleOwner,
        context: Context,
        inventoryViewModel: InventoryViewModel
    ) : RecyclerView.ViewHolder(itemView) {
        val viewBinding = MaketInventoryArsenalBinding.bind(itemView)
        private var classArsenal : Int = 0
        private var itemId : Int = 0
        private var clips : MutableList<Int> = mutableListOf(0, 0, 0, 0)
        private var activeClips = 0

        init {
            inventoryViewModel.arsenalVisibility.observe(lifecycleOwner){
                viewBinding.arsenalVisibility.isVisible = it
            }

            viewModel.modeSettings.observe(lifecycleOwner){
                setMode(it)
            }

            viewModel.arsenalItem.observe(lifecycleOwner){
                viewBinding.apply {
                    when(it.classArsenal){
                        1 -> rBtnBBType.isChecked = true
                        2 -> rBtnDBType.isChecked = true
                        0 -> {
                            rBtnBBType.isChecked = false
                            rBtnDBType.isChecked = false
                        }
                    }
                    editNameArsenal.setText(it.name)
                    editDamageX.setText(it.damageX.toString())
                    editDamageY.setText(it.damageY.toString())
                    editDamageZ.setText(it.damageZ.toString())
                    editPenetrationArsenal.setText(it.penetration.toString())
                    editStepArsenal.setText(it.step.toString())
                    editChangeArsenal.setText(it.change.toString())
                    editPatronsArsenal.setText(it.maxPatrons.toString())
                    editDistanseArsenal.setText(it.distanse.toString())
                    tvValueTestArsenal.text = it.valueTest.toString()
                    cBoxChekBPowerArsenal.isChecked = it.bonusPower
                    cBoxPairedArsenal.isChecked = it.paired
                    editFeaturesArsenal.setText(it.features)
                    clips = mutableListOf(it.maxPatrons, it.clip1, it.clip2, it.clip3)
                    setModeRepair(activeClips)
                    setTypeSettings(it.classArsenal)
                    itemId = it.id
                }
            }

            viewModel.activeClip.observe(lifecycleOwner){
                setModeRepair(it)
            }


        }



        fun bind(
            arsenal: Arsenal,
            viewModel: ArsenalViewModel,
            lifecycleOwner: LifecycleOwner,
            context: Context,
            inventoryViewModel: InventoryViewModel
        ) {
            inventoryViewModel.getVisibleView(arsenal.name)

            viewBinding.apply {
                arsenalRecView.layoutManager = LinearLayoutManager(context)
                arsenalRecView.adapter = ArsenalItemAdapter(viewModel, lifecycleOwner)

                tvArsenal.setOnClickListener { inventoryViewModel.updateVisibleView(arsenal.name) }

                btnAddArsenal.setOnClickListener { viewModel.setMode(1,0) }
                btnEscSettingsArsenal.setOnClickListener { viewModel.setMode(0,0) }

                rBtnGTypeArsenal.setOnCheckedChangeListener { group, checkedId ->
                    when(checkedId){
                        rBtnBBType.id -> setTypeSettings(1)
                        rBtnDBType.id -> setTypeSettings(2)
                    }
                }

                rBtnGClips.setOnCheckedChangeListener { clips, checkedId ->
                    when(checkedId){
                        rBtnClip1.id -> viewModel.setActiveClip(1)
                        rBtnClip2.id -> viewModel.setActiveClip(2)
                        rBtnClip3.id -> viewModel.setActiveClip(3)
                    }
                }

                btnAddValueTest.setOnClickListener { changeValueTest(tvValueTestArsenal.text.toString().toInt(), 1) }
                btnRemValueTest.setOnClickListener { changeValueTest(tvValueTestArsenal.text.toString().toInt(), -1) }

                btnSaveArsenal.setOnClickListener { saveArsenal(viewModel) }

                btnDeleteArsenal.setOnClickListener {
                    viewModel.deleteArsenalItem(itemId)
                    viewModel.setMode(0, 0)
                }

                btnRavageActiveClip.setOnClickListener { ravageArsenalClips(activeClips, viewModel) }
                btnRefresh.setOnClickListener { refreshArsenalClips(viewModel) }
                btnReplenishActiveClip.setOnClickListener { replenishArsenalClips(activeClips, viewModel) }
                btnReplenishAll.setOnClickListener { replenishAllArsenalClips(viewModel) }

            }

        }

        private fun saveArsenal(viewModel: ArsenalViewModel){

            if (classArsenal !=0){
                try {
                    viewBinding.apply {

                        if (itemId > 0){
                            val item = ArsenalItem(
                                id = itemId,
                                idPlayer = Player.getInstance().getIdActivePlayer(),
                                classArsenal = classArsenal,
                                name = editNameArsenal.text.toString(),
                                damageX = editDamageX.text.toString().toInt(),
                                damageY = editDamageY.text.toString().toInt(),
                                damageZ = editDamageZ.text.toString().toInt(),
                                penetration = editPenetrationArsenal.text.toString().toInt(),
                                step = editStepArsenal.text.toString().toInt(),
                                change = editChangeArsenal.text.toString().toInt(),
                                maxPatrons = editPatronsArsenal.text.toString().toInt(),
                                clip1 = editPatronsArsenal.text.toString().toInt(),
                                clip2 = editPatronsArsenal.text.toString().toInt(),
                                clip3 = editPatronsArsenal.text.toString().toInt(),
                                distanse = editDistanseArsenal.text.toString().toInt(),
                                valueTest = tvValueTestArsenal.text.toString().toInt(),
                                bonusPower = cBoxChekBPowerArsenal.isChecked,
                                paired = cBoxPairedArsenal.isChecked,
                                features = editFeaturesArsenal.text.toString()
                            )
                            viewModel.updateArsenalItem(item)
                        }
                        else{
                            val item = ArsenalItem(
                                id = 0,
                                idPlayer = Player.getInstance().getIdActivePlayer(),
                                classArsenal = classArsenal,
                                name = editNameArsenal.text.toString(),
                                damageX = editDamageX.text.toString().toInt(),
                                damageY = editDamageY.text.toString().toInt(),
                                damageZ = editDamageZ.text.toString().toInt(),
                                penetration = editPenetrationArsenal.text.toString().toInt(),
                                step = editStepArsenal.text.toString().toInt(),
                                change = editChangeArsenal.text.toString().toInt(),
                                maxPatrons = editPatronsArsenal.text.toString().toInt(),
                                clip1 = editPatronsArsenal.text.toString().toInt(),
                                clip2 = editPatronsArsenal.text.toString().toInt(),
                                clip3 = editPatronsArsenal.text.toString().toInt(),
                                distanse = editDistanseArsenal.text.toString().toInt(),
                                valueTest = tvValueTestArsenal.text.toString().toInt(),
                                bonusPower = cBoxChekBPowerArsenal.isChecked,
                                paired = cBoxPairedArsenal.isChecked,
                                features = editFeaturesArsenal.text.toString()
                            )
                            viewModel.addArsenalItem(item)
                        }

                        viewModel.setMode(0, 0)
                    }

                }
                catch (e: Exception){
                    Snackbar.make(viewBinding.root, "Не все поля заполнены!", Snackbar.LENGTH_SHORT).show()
                }
            }
            else {
                Snackbar.make(viewBinding.root, "Не выбран Класс оружия!", Snackbar.LENGTH_SHORT).show()
            }
        }

        private fun setMode(mode: Int){
            viewBinding.apply {
                when(mode){
                    0 -> {
                        addArsenalMode.isVisible = false
                        btnAddArsenal.isVisible = true
                    }
                    1 -> {
                        tvModeSettingsArsenal.text = "Добавить Оружие"
                        addArsenalMode.isVisible = true
                        btnAddArsenal.isVisible = false
                        btnDeleteArsenal.isVisible = false
                        rBtnDBType.isVisible = true
                        rBtnBBType.isVisible = true
                        repairArsenal.isVisible = false
                        settingsArsenal.isVisible = true
                    }
                    2 -> {
                        tvModeSettingsArsenal.text = "Редактировать Оружие"
                        addArsenalMode.isVisible = true
                        btnAddArsenal.isVisible = false
                        settingsArsenal.isVisible = true
                        repairArsenal.isVisible = false
                        btnDeleteArsenal.isVisible = true
                        if (classArsenal==1){
                            rBtnDBType.isVisible = false
                            rBtnBBType.isVisible = true
                        }
                        else{
                            rBtnDBType.isVisible = true
                            rBtnBBType.isVisible = false
                        }
                    }
                    3 -> {
                        tvModeSettingsArsenal.text = "Менеджер Патронов"
                        addArsenalMode.isVisible = true
                        btnAddArsenal.isVisible = false
                        settingsArsenal.isVisible = false
                        repairArsenal.isVisible = true
                    }
                }
            }

        }

        private fun changeValueTest(value : Int, step : Int){
            var newValue = value + step
            if (newValue == 0) {
                newValue = 1
            }
            viewBinding.tvValueTestArsenal.text = newValue.toString()
        }
        private fun setModeRepair(activeClip: Int) {
            activeClips = activeClip
            viewBinding.apply {
                pBarPatrons.max = clips[0]

                val patronsText = "${clips[activeClip]}/${clips[0]}"
                when (activeClip) {
                    1 -> rBtnClip1.isChecked = true
                    2 -> rBtnClip2.isChecked = true
                    3 -> rBtnClip3.isChecked = true
                }

                pBarPatrons.progress = clips[activeClip]
                tvPatronsActiveClip.text = patronsText
            }
        }

        private fun ravageArsenalClips(active: Int, viewModel: ArsenalViewModel) {
            clips[active] = 0
            viewModel.ravageClipsToArsenal(itemId, clips[1], clips[2], clips[3])
        }

        private fun replenishArsenalClips(active: Int, viewModel: ArsenalViewModel){
            clips[active] = clips[0]
            viewModel.ravageClipsToArsenal(itemId, clips[1], clips[2], clips[3])
        }

        private fun replenishAllArsenalClips(viewModel: ArsenalViewModel){
            val maxPatrons = clips[0]
            clips = mutableListOf(maxPatrons, maxPatrons, maxPatrons, maxPatrons)
            viewModel.ravageClipsToArsenal(itemId, clips[1], clips[2], clips[3])
        }

        private fun refreshArsenalClips(viewModel: ArsenalViewModel){
            clips.sortDescending()
            viewModel.ravageClipsToArsenal(itemId, clips[1], clips[2], clips[3])
        }

        private fun setTypeSettings(type: Int){
            classArsenal = type
            viewBinding.apply {
                when(type){
                    0-> {
                        settingsDBArsenal.isVisible = false
                        cBoxChekBPowerArsenal.isVisible = false
                        cBoxChekBPowerArsenal.isChecked = false
                    }
                    1-> {
                        settingsDBArsenal.isVisible = false
                        editPatronsArsenal.setText("0")
                        editDistanseArsenal.setText("0")
                        cBoxChekBPowerArsenal.isVisible = true
                        cBoxChekBPowerArsenal.isChecked = true
                    }
                    2->{
                        settingsDBArsenal.isVisible = true
                        cBoxChekBPowerArsenal.isVisible = false
                        cBoxChekBPowerArsenal.isChecked = false
                    }
                }
            }
        }
    }
}
