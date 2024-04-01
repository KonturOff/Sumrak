package com.example.sumrak.ui.inventory.recycler.effects.item




class EffectsItemManager private constructor(){

    private val effectsItemList = ArrayList<EffectsItem>()

    private var effectsItemAdapter : EffectsItemAdapter? = null

    fun setAdapter(effectsItemAdapter: EffectsItemAdapter) {
        this.effectsItemAdapter = effectsItemAdapter
    }

    fun addItem(effectsItem: EffectsItem){
        effectsItemList.add(effectsItem)
        effectsItemAdapter?.notifyItemInserted(effectsItemList.size)
    }

    fun deleteitem(id: Int){
        val position = getItemPositionToId(id)
        if (position >= 0 && position < effectsItemList.size) {
            effectsItemList.removeAt(position)
            effectsItemAdapter?.notifyItemRemoved(position)
        }
    }

    fun updateItem(id: Int, effectsItem: EffectsItem){
        val position = getItemPositionToId(id)
        effectsItemList[position] = effectsItem
        effectsItemAdapter?.notifyItemChanged(position)
    }

    fun updateChekItemToPosition(id: Int, check : Int){
        val position = getItemPositionToId(id)
        effectsItemList[position].isActive = check
    }

    private fun getItemPositionToId(id : Int): Int {
        var result = 0
        for (i in 0..<effectsItemList.size){
            if (id == effectsItemList[i].id){
                result = i
                break
            }
        }
        return result
    }

    fun clearEffectsList(){
        effectsItemList.clear()
    }

    fun loadEffectsToPlayer(effectsItem: EffectsItem){
        effectsItemList.add(effectsItem)
    }

    fun getItemToId(id: Int): EffectsItem{
        val position = getItemPositionToId(id)
        return effectsItemList[position]
    }

    fun getItem(position : Int): EffectsItem {
        return effectsItemList[position]
    }

    fun getItemCount():Int{
        return effectsItemList.size
    }



    companion object {
        private val instance = EffectsItemManager()

        @JvmStatic
        fun getInstance(): EffectsItemManager {
            return instance
        }
    }
}