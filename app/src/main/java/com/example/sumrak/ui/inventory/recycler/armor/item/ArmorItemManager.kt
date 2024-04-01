package com.example.sumrak.ui.inventory.recycler.armor.item

class ArmorItemManager private constructor(){
    private val armorItemList = ArrayList<ArmorItem>()
    private var armorItemAdapter : ArmorItemAdapter? = null

    fun setAdepter(armorItemAdapter: ArmorItemAdapter) {
        this.armorItemAdapter = armorItemAdapter
    }

    fun addItem(armorItem: ArmorItem){
        armorItemList.add(armorItem)
        armorItemAdapter?.notifyItemInserted(armorItemList.size)
    }

    // TODO использование notifyDataSetChanged стоит избегать, так как это
    //  затратная для телефона операция. Также может экран рябить при вызове этой
    //  функции (так как происходит ПОЛНАЯ переррисовка списка). Лучше искать альтернативы
    fun deleteItem(id: Int){
        val position = getPosItemToId(id)
        armorItemList.removeAt(position)
        armorItemAdapter?.notifyDataSetChanged()
    }

    fun loarArmorToPlayer(armorItem: ArmorItem){
        armorItemList.add(armorItem)
    }

    fun updateArmor(armorItem: ArmorItem, id: Int){
        val position = getPosItemToId(id)
        armorItemList[position] = armorItem
        armorItemAdapter?.notifyDataSetChanged()
    }

    fun updateEnduranceArmor(endurance : Int, id: Int){
        val position = getPosItemToId(id)
        armorItemList[position].endurance = endurance
        armorItemAdapter?.notifyDataSetChanged()
    }

    fun clearArmorList(){
        armorItemList.clear()
    }

    fun getPosItemToId(id :Int): Int{
        var result = 0
        for (i in 0..<armorItemList.size){
            if (id == armorItemList[i].id){
                result = i
            }
        }
        return result
    }

    fun getItem(position : Int): ArmorItem{
        return armorItemList[position]
    }

    fun getItemToId ( id: Int) : ArmorItem{
        val position = getPosItemToId(id)
        return getItem(position)
    }

    fun getItemCount():Int{
        return armorItemList.size
    }




    companion object{
        private val instance = ArmorItemManager()

        @JvmStatic
        fun getInstance() : ArmorItemManager{
            return instance
        }
    }
}