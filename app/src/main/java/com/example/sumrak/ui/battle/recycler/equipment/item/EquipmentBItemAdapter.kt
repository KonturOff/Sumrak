package com.example.sumrak.ui.battle.recycler.equipment.item

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.sumrak.Player
import com.example.sumrak.R
import com.example.sumrak.databinding.MaketBattleEquipmentRecBinding
import com.example.sumrak.ui.battle.BattleFragment
import com.example.sumrak.ui.inventory.recycler.equipment.EquipmentViewModel
import com.example.sumrak.ui.inventory.recycler.equipment.item.EquipmentItem
import com.example.sumrak.ui.inventory.recycler.equipment.item.EquipmentItemManager


class EquipmentBItemAdapter(
    private val equipmentViewModel: EquipmentViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val battleFragment: equipmentRollClick
) : RecyclerView.Adapter<EquipmentBItemAdapter.EquipmentBItemViewHolder>() {

    private val equipmentItemManager = EquipmentItemManager.getInstance()

    interface equipmentRollClick{
        fun rollEquipmentTest(mode: String, param : Int)
    }

    init {
        equipmentViewModel.getEquipmentToIdPlayer(Player.getInstance().getIdActivePlayer())
        equipmentItemManager.setBattleAdapter(this)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EquipmentBItemAdapter.EquipmentBItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.maket_battle_equipment_rec, parent, false)
        return EquipmentBItemViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: EquipmentBItemAdapter.EquipmentBItemViewHolder,
        position: Int
    ) {
        val item = equipmentItemManager.getItemToPos(position)
        holder.bind(item, equipmentViewModel, lifecycleOwner, battleFragment)
    }

    override fun getItemCount(): Int {
        return equipmentItemManager.getItemCount()
    }

    class EquipmentBItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val b = MaketBattleEquipmentRecBinding.bind(itemView)
        fun bind(
            item: EquipmentItem,
            equipmentViewModel: EquipmentViewModel,
            lifecycleOwner: LifecycleOwner,
            battleFragment: equipmentRollClick
        ) {

            if (item.charge == 0){
                b.btnUseEquipmentB.isEnabled = false
            }
            b.tvNameEquipmentB.text = item.name
            b.tvChangeEquipmentB.text = "(${item.charge}/${item.maxCharge}) : ${item.step}"
            b.colorTestB.setBackgroundResource(getColor(item.test))

            b.btnReplaceEquipmentB.setOnClickListener { equipmentViewModel.replaceChangeEquipment(item.id) }
            b.btnUseEquipmentB.setOnClickListener {
                if (item.test){
                    battleFragment.rollEquipmentTest("Убывающий Тест ${item.name}", item.charge)
                }
                equipmentViewModel.useEquipmentChange(item.id)
            }


        }

        private fun getColor(test : Boolean) : Int{
            return if (test){
                R.color.d20
            }
            else{
                R.color.sumrak_grey50
            }
        }


    }
}