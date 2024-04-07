package com.example.sumrak.ui.battle.recycler.equipment.item

import com.example.sumrak.ui.inventory.recycler.equipment.item.EquipmentItem
import com.example.sumrak.ui.inventory.recycler.equipment.item.EquipmentItemAdapter

class EquipmentBItemManager {

    private val equipmentItemList = ArrayList<EquipmentItem>()
    private var equipmentBItemAdapter: EquipmentBItemAdapter? = null


    fun setBattleAdapter(equipmentBItemAdapter: EquipmentBItemAdapter){
        this.equipmentBItemAdapter = equipmentBItemAdapter
        println("адаптер установлен")
    }

    fun addItem(equipmentItem: EquipmentItem){
        equipmentItemList.add(equipmentItem)
        equipmentBItemAdapter?.notifyItemInserted(equipmentItemList.size)
    }


    // TODO Использование notifyDataSetChanged стоит избегать, так как это затратная для
    //  телефона операция. Также может экран рябить при вызове этой
    //  функции (так как происходит ПОЛНАЯ переррисовка списка). Лучше искать альтернативы
    fun updateItem(equipmentItem: EquipmentItem){
        val pos = getPosItemToId(equipmentItem.id)
        println(pos)
        equipmentItemList[pos] = equipmentItem
        equipmentBItemAdapter?.notifyDataSetChanged()
    }


    fun getItemToId(id: Int): EquipmentItem {
        var result = EquipmentItem(0,0,"",0,0,0,false, 0)
        for (i in 0..<equipmentItemList.size){
            if(id == equipmentItemList[i].id){
                result = equipmentItemList[i]
                break
            }
        }
        return result
    }

    private fun getPosItemToId(id: Int) : Int{
        var result = 0
        for (i in 0..<equipmentItemList.size){
            if (id == equipmentItemList[i].id){
                result = i
                break
            }
        }
        return result
    }

    fun clearList(){
        equipmentItemList.clear()
    }

    fun getItemToPos(pos : Int): EquipmentItem {
        return equipmentItemList[pos]
    }

    fun getItemCount() : Int{
        return equipmentItemList.size
    }


    companion object{
        private val instance = EquipmentBItemManager()

        @JvmStatic
        fun getInstance(): EquipmentBItemManager {
            return instance
        }
    }
}
