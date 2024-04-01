package com.example.sumrak.ui.inventory.recycler.consumables.item


class ConsumablesItemManager private constructor() {


    private val consumblesItemList = ArrayList<ConsumblesItem>()

    private var consumblesItemAdapter : ConsumblesItemAdapter? = null

    fun setAdapter(consumblesItemAdapter: ConsumblesItemAdapter) {
        this.consumblesItemAdapter = consumblesItemAdapter
    }
    fun addItem(newItem: ConsumblesItem) {
        consumblesItemList.add( newItem)
        consumblesItemAdapter?.let {
            it.notifyItemInserted(consumblesItemList.size)
        }
    }

    fun loadItemToPlayer(newItem: ConsumblesItem){
        consumblesItemList.add(newItem)
    }

    fun deleteItem(id: Int){
        val position = getPositionToId(id)
        consumblesItemList.removeAt(position)
        consumblesItemAdapter?.notifyItemRemoved(position)

    }

    fun updateItem(consumblesItem: ConsumblesItem, id: Int){
        val position = getPositionToId(id)
        consumblesItemList[position] = consumblesItem
    }

    fun getPositionToId(id:Int) :Int{
        var result = -1
        for (i in 0..<consumblesItemList.size){
            if (id == consumblesItemList[i].id){
                result = i
            }
        }
        return result
    }

    fun getItemToId(id:Int) : ConsumblesItem{
        val position = getPositionToId(id)
        return getItem(position)
    }

    fun getItem(position: Int): ConsumblesItem {
        return consumblesItemList[position]
    }

    fun getItemCount(): Int {
        return consumblesItemList.size
    }

    fun clearConsumablesList(){
        consumblesItemList.clear()
    }




    companion object {
        private val instance = ConsumablesItemManager()

        @JvmStatic
        fun getInstance(): ConsumablesItemManager {
            return instance
        }
    }

}