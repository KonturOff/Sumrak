package com.example.sumrak.ui.inventory.recycler.arsenal.item

class ArsenalItemManager private constructor() {
    private val arsenalItemList = ArrayList<ArsenalItem>()
    private var arsenalItemAdapter : ArsenalItemAdapter? = null

    fun setAdapter(arsenalItemAdapter: ArsenalItemAdapter){
        this.arsenalItemAdapter = arsenalItemAdapter
    }

    fun addItem(arsenalItem: ArsenalItem){
        arsenalItemList.add(arsenalItem)
        arsenalItemAdapter?.notifyItemInserted(arsenalItemList.size)
    }

    fun updateItem(arsenalItem: ArsenalItem, id: Int){
        val pos = getPosItemToId(id)
        arsenalItemList[pos] = arsenalItem
        arsenalItemAdapter?.notifyDataSetChanged()
    }

    fun updatePatronsItemToId(id: Int, clip1: Int, clip2: Int, clip3: Int){
        val pos = getPosItemToId(id)
        arsenalItemList[pos].clip1 = clip1
        arsenalItemList[pos].clip2 = clip2
        arsenalItemList[pos].clip3 = clip3
        arsenalItemAdapter?.notifyDataSetChanged()
    }


    fun deleteItem(id: Int){
        val pos = getPosItemToId(id)
        arsenalItemList.removeAt(pos)
        arsenalItemAdapter?.notifyDataSetChanged()
    }
    fun getPosItemToId(id : Int) : Int{
        var result = 0
        for (i in 0..<arsenalItemList.size){
            if (arsenalItemList[i].id == id){
                result = i
                break
            }
        }
        return result
    }

    fun getItemToId(id: Int) : ArsenalItem{
        val pos = getPosItemToId(id)
        return getItem(pos)
    }
    fun getItem(pos : Int): ArsenalItem {
        return arsenalItemList[pos]
    }

    fun getItemCount() : Int{
        return arsenalItemList.size
    }

    fun clearList(){
        arsenalItemList.clear()
    }
    companion object{
        private val instance = ArsenalItemManager()

        @JvmStatic
        fun getInstance() :ArsenalItemManager{
            return instance
        }
    }


}