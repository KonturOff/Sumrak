package com.example.sumrak.lists

import com.example.sumrak.ui.calculator.historyRoll.HistoryAdapter

class HistoryRollManager private constructor() {

    private val historyRollList = ArrayList<HistoryRoll>()
    private var historyAdapter: HistoryAdapter? = null

    fun setAdapter(historyAdapter: HistoryAdapter) {
        this.historyAdapter = historyAdapter
    }

    fun addItem(historyRoll: HistoryRoll) {
        historyRollList.add(0,historyRoll)
        historyAdapter?.notifyItemInserted(0)
    }

    fun getLastRollHitToIdPlayer(idPlayer: Int):HistoryRoll?{
        var result :HistoryRoll? = null
        for (i in historyRollList.indices){
            if (historyRollList[i].mode == "Проверка Попадания" && historyRollList[i].player == idPlayer){
                result = historyRollList[i]
                break
            }
        }
        return result
    }

    private fun getPositionLastHitToIdPlayer(idPlayer: Int): Int {
        var position = 0
        for (i in historyRollList.indices){
            if (historyRollList[i].mode == "Проверка Попадания" && historyRollList[i].player == idPlayer){
                position = i
                break
            }
        }
        return position
    }
    fun updateValueHitToIdPlayer(idPlayer: Int, value : Int){
        val position = getPositionLastHitToIdPlayer(idPlayer)
        historyRollList[position].value = value
        historyAdapter?.notifyItemChanged(position)
    }


    fun getLastRollIniToIdPlayer(idPlayer: Int): HistoryRoll{
        var result = HistoryRoll("d20","0","","",idPlayer,"не найдено", 0, 0,0, 0, null)
        for (i in historyRollList.indices){
            if (historyRollList[i].mode == "Проверка Инициативы" && historyRollList[i].player == idPlayer){
                result = historyRollList[i]
                break
            }
        }
        return result
    }
    fun getPositionLastIniToIdPlayer(idPlayer: Int): Int {
        var position = 0
        for (i in historyRollList.indices){
            if (historyRollList[i].mode == "Проверка Инициативы" && historyRollList[i].player == idPlayer){
                position = i
                break
            }
        }
        return position
    }

    fun updateStepIniToPos(position: Int, value : Int){
        historyRollList[position].value = value
        historyAdapter?.notifyItemChanged(position)
    }



    fun removeItem(historyRoll: HistoryRoll) {
        val index = historyRollList.indexOf(historyRoll)
        if (index >= 0) {
            historyRollList.removeAt(index)
            historyAdapter?.notifyItemRemoved(index)
        }
    }

    fun removeItemToIdPlayer(idPlayer: Int) {
        for (i in historyRollList.size-1 downTo 0){
            if (historyRollList[i].player == idPlayer){
                historyRollList.removeAt(i)
                historyAdapter?.notifyItemRemoved(i)
            }
        }
    }

    fun updateItem(index: Int, historyRoll: HistoryRoll) {
        historyRollList[index] = historyRoll
        historyAdapter?.notifyItemChanged(index)
    }

    fun getItem(position: Int): HistoryRoll {
        return historyRollList[position]
    }

    fun getItemCount(): Int {
        return historyRollList.size
    }

    //Проверка 2х единиц рядом
    fun getConfirmedToPos(position: Int): Boolean{
        if (position < 0 || position >= historyRollList.size) {
            return false
        }

        val currentRoll = historyRollList[position]

        // Проверяем соседние экземпляры
        if (position > 0) {
            val previousRoll = historyRollList[position - 1]
            if (previousRoll.cube == currentRoll.cube && previousRoll.player == currentRoll.player && previousRoll.resultRoll == "1") {
                return true
            }
        }

        if (position < historyRollList.size - 1) {
            val nextRoll = historyRollList[position + 1]
            if (nextRoll.cube == currentRoll.cube && nextRoll.player == currentRoll.player && nextRoll.resultRoll == "1") {
                return true
            }
        }

        return false
    }

    companion object {
        private val instance = HistoryRollManager()

        @JvmStatic
        fun getInstance(): HistoryRollManager {
            return instance
        }
    }
}

//Шпаргалка

// Добавление нового элемента в список
//HistoryRollManager.getInstance().addItem(newHistoryRoll)

// Удаление элемента из списка
//HistoryRollManager.getInstance().removeItem(historyRoll)

// Обновление элемента в списке
//HistoryRollManager.getInstance().updateItem(index, updatedHistoryRoll)

// Получение списка элементов
//val historyRollList = HistoryRollManager.getInstance().getHistoryRollList()