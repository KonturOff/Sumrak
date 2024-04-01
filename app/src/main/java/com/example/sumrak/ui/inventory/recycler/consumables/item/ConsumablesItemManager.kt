package com.example.sumrak.ui.inventory.recycler.consumables.item


class ConsumablesItemManager private constructor() {


    private val consumablesItemList = ArrayList<ConsumablesItem>()

    private var consumablesItemAdapter : ConsumablesItemAdapter? = null

    fun setAdapter(consumablesItemAdapter: ConsumablesItemAdapter) {
        this.consumablesItemAdapter = consumablesItemAdapter
    }
    fun addItem(newItem: ConsumablesItem) {
        consumablesItemList.add( newItem)
        consumablesItemAdapter?.notifyItemInserted(consumablesItemList.size)
    }

    fun loadItemToPlayer(newItem: ConsumablesItem){
        consumablesItemList.add(newItem)
    }

    fun deleteItem(id: Int){
        val position = getPositionToId(id)
        consumablesItemList.removeAt(position)
        consumablesItemAdapter?.notifyItemRemoved(position)

    }

    fun updateItem(consumablesItem: ConsumablesItem, id: Int){
        val position = getPositionToId(id)
        consumablesItemList[position] = consumablesItem
    }

    private fun getPositionToId(id:Int) :Int{
        var result = -1
        for (i in 0..< consumablesItemList.size){
            if (id == consumablesItemList[i].id){
                result = i
            }
        }
        return result
    }

    fun getItemToId(id:Int) : ConsumablesItem{
        val position = getPositionToId(id)
        return getItem(position)
    }

    fun getItem(position: Int): ConsumablesItem {
        return consumablesItemList[position]
    }

    fun getItemCount(): Int {
        return consumablesItemList.size
    }

    fun clearConsumablesList(){
        consumablesItemList.clear()
    }

    companion object {
        private val instance = ConsumablesItemManager()

        @JvmStatic
        fun getInstance(): ConsumablesItemManager {
            return instance
        }
    }

}