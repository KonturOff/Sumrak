package com.example.sumrak.ui.inventory.recycler.equipment.item


class EquipmentItemManager private constructor(){

    private val equipmentItemList = ArrayList<EquipmentItem>()
    private var equipmentItemAdapter : EquipmentItemAdapter? = null

    fun setAdapter(equipmentItemAdapter: EquipmentItemAdapter){
        this.equipmentItemAdapter = equipmentItemAdapter
    }


    fun addItem(equipmentItem: EquipmentItem){
        equipmentItemList.add(equipmentItem)
        equipmentItemAdapter?.notifyItemInserted(equipmentItemList.size)
    }

    // TODO Использование notifyDataSetChanged стоит избегать, так как это затратная для
    //  телефона операция. Также может экран рябить при вызове этой
    //  функции (так как происходит ПОЛНАЯ переррисовка списка). Лучше искать альтернативы
    fun updateItem(equipmentItem: EquipmentItem){
        val pos = getPosItemToId(equipmentItem.id)
        println(pos)
        equipmentItemList[pos] = equipmentItem
        equipmentItemAdapter?.notifyDataSetChanged()
    }

    fun deleteItemToId(id: Int){
        val pos = getPosItemToId(id)
        equipmentItemList.removeAt(pos)
        equipmentItemAdapter?.notifyDataSetChanged()
    }

    fun getItemToId(id: Int): EquipmentItem{
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

    fun getItemToPos(pos : Int): EquipmentItem{
        return equipmentItemList[pos]
    }

    fun getItemCount() : Int{
        return equipmentItemList.size
    }

    fun checkLinkToIdConsum(id: Int): Boolean{
        var result = false
        for (i in 0..<equipmentItemList.size){
            if (equipmentItemList[i].consumablesLink == id){
                result = true
                break
            }
        }
        return result
    }


    companion object{
        private val instance = EquipmentItemManager()

        @JvmStatic
        fun getInstance(): EquipmentItemManager{
            return instance
        }
    }
}