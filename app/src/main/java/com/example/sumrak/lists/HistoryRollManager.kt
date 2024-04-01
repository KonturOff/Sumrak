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

    fun getLastRollIniToIdPlayer(idPlayer: Int): HistoryRoll{
        var result = HistoryRoll("d20","0","","",idPlayer,"не найдено", 0, 0, 0)
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