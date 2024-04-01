package com.example.sumrak.ui.battle



class BattleManager(
    val items: MutableList<BattleItem> = mutableListOf(), // Список элементов Боя
    private val expandedStateMap: MutableMap<Int, Boolean> = mutableMapOf() // Соответствие id элемента и его состояния (раскрыт или свернут),
) {

    // Метод для добавления нового элемента в инвентарь
    fun addItem(item: BattleItem) {
        items.add(item)
        expandedStateMap[item.id] = item.isExpanded
    }

    fun updatePosition(){
        for (i in items.indices){
            items[i].position = i
        }
    }

    // Метод для переключения состояния элемента (раскрыть/свернуть)
    fun toggleItemState(name: String) {
        val itemId = getIdToName(name)
        val currentState = expandedStateMap[itemId] ?: false
        expandedStateMap[itemId] = !currentState
        items.find { it.id == itemId }?.isExpanded = !currentState
    }

    fun getVisibleToName(name:String): Boolean{
        val pos = getPosToName(name)
        return items[pos].isExpanded
    }

    private fun getPosToName(name: String): Int{
        var pos = -1
        for (i in items.indices){
            if (items[i].type == name){
                pos = i
                break
            }
        }
        return pos
    }

    private fun getIdToName(name: String): Int{
        var id = -1
        for (i in items.indices){
            if (items[i].type == name){
                id = items[i].id
                break
            }
        }
        return id
    }

    fun getItemToName(name: String): BattleItem {
        val pos = getPosToName(name)
        return items[pos]
    }


    fun getAllItems(): MutableList<BattleItem> {
        return items
    }

    fun clearList(){
        items.clear()
    }

    fun getItem(position : Int) : BattleItem {
        return items[position]
    }

    fun getCountItem():Int{
        return items.size
    }




    companion object{
        private val instance = BattleManager()

        @JvmStatic
        fun getInstance(): BattleManager{
            return instance
        }
    }
}