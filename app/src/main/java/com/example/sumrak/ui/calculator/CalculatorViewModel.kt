package com.example.sumrak.ui.calculator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class CalculatorViewModel : ViewModel() {

    val lv = MutableLiveData<String>()

    private val rl = MutableLiveData<Int?>()

    var result: String =""


    fun addCube(cube: String) {
        //Если строка не пустая
        if (result != "") {
            //если в конце строки стоит оператор добавляем куб
            if (result.endsWith("+") || result.endsWith("-")){
                result += cube
            }
            //если в конце строки не оператор
            else {
                //получем индексы последних символов +, -, d в строке
                val lastMinusIndex = result.indexOf("-", result.length-4)
                val lastPlusIndex = result.indexOf("+", result.length-4)
                val lastCubeIndex = result.indexOf("d", result.length-4)
                //если d присутсвует и он правее чем индексы
                if (lastCubeIndex!=-1 && lastCubeIndex>lastMinusIndex && lastCubeIndex>lastPlusIndex){
                    val lastcube = result.substring(lastCubeIndex)
                    //если выбраный куб равен последнему кубу в строке
                    if (lastcube == cube) {
                        //разделяем строку на массивы по оператору -
                        val anonimTextMinus = result.split("-").toMutableList()
                        //разделяем последний массив anonim_text_minus по оператору +
                        val anonimTextPlus =anonimTextMinus[anonimTextMinus.size-1].split("+").toMutableList()
                        // разделяем последний массив anonim_text_plus по d
                        val anonimTextD = anonimTextPlus[anonimTextPlus.size-1].split("d").toMutableList()
                        //если перед d ничего не стоит добавляем 2
                        if (anonimTextD[0] == ""){
                            anonimTextD[0] = "2"
                        }
                        //если перед d уже есть множитель увеличиваем его на 1
                        else anonimTextD[0] = (anonimTextD[0].toInt() + 1).toString()

                        //собираем конструкцию в обратном порядке

                        //собираем 2 элемента массива  (их всегда 2) anonim_text_d в одну строку ставим между ними d
                        //помещаем ее в последний элемент массива anonim_text_plus
                        anonimTextPlus[anonimTextPlus.size-1] = anonimTextD[0] + "d" + anonimTextD[1]
                        //собираем ве элементы массива anonim_text_plus в одну строку между ними помещая +
                        //кладем в последний элемент массива  anonim_text_minus
                        for (i in 0..<anonimTextPlus.size){
                            if (i == 0) {
                                anonimTextMinus[anonimTextMinus.size-1] = anonimTextPlus[i]
                            }
                            else anonimTextMinus[anonimTextMinus.size-1] = anonimTextMinus[anonimTextMinus.size-1] + "+" + anonimTextPlus[i]
                        }
                        //собираем ве элементы массива anonim_text_minus в одну строку между ними помещая -
                        //помещаем в строку result
                        for (i in 0..<anonimTextMinus.size){
                            result = if (i == 0) {
                                anonimTextMinus[i]
                            } else result + "-" + anonimTextMinus[i]
                        }
                    }
                    //если выбранный куб не равен последнему кубу добавляем с оператором +
                    else result = "$result+$cube"
                }
                //если d отсутствует или находится левее операторов
                else result += cube
            }
        }
        //если строка пустая добавляем в строку куб
        else result = cube

        lv.postValue(result)
    }

    fun addSymbol(symbol: String){
        //если строка не пустая
        if (result != ""){
            //если в конце строки стоит оператор
            if (result.endsWith("+") || result.endsWith("-")){
                //если вводимый символ это оператор заменяем последний символ строки на вводимый оператор
                if (symbol == "+" || symbol == "-") {
                    result = result.dropLast(1)+symbol
                }
                //если вводимый символ не 0 добавляем символ в конец строки
                else if (symbol != "0") {
                    result += symbol
                }
            }
            //если d стоит в последних 4 символах
            else if (result.indexOf("d", result.length-4)!= -1){
                // если оператор находится правее куба то добавляем символ без оператора +
                if (result.indexOf("+", result.length-4) > result.indexOf("d",result.length-4)
                    || result.indexOf("-", result.length-4) > result.indexOf("d", result.length-4)){
                    result = "$result$symbol"
                }
                // если после куба нет оператора
                else{
                    //если вводим оператор
                    if (symbol == "+" || symbol == "-"){
                        result += symbol
                    }
                    //если вводим числа кроме 0 устанавливаем его добавляя оператор +
                    else if (symbol != "0"){
                        result = "$result+$symbol"
                    }
                }
            }
            //иначе устанавливаем вводимый символ в конец строки
            else result += symbol

        }
        //если строка пустая
        else{
            //если вводимый символ не +, -, 0 добавляем введеный символ
            if (symbol != "-" && symbol != "+" && symbol!="0"){
                result += symbol
            }
        }
        lv.postValue(result)
    }

    fun enableRollIfCub(cub : String) : Boolean{
        val splitPlus = cub.split("+").toMutableList()
        for (i in 0..< splitPlus.size) {
            val splitMinus = splitPlus[i].split("-").toMutableList()
            for (j in 0..< splitMinus.size) {
                val splitD = splitMinus[j].split("d").toMutableList()
                if (splitD[0].length > 3) {
                    return false
                }
            }
        }
        return true
    }

    fun clear(){
        //очищаем строку
        result = ""
        lv.postValue(result)
    }

    fun deleteSymdol(){
        //если строка не пустая
        if (result != ""){
            // если в последних 4 смволах строки стоит куб
            if (result.indexOf("d", result.length-4) != -1) {
                // если оператор находится правее куба удаляем последний символ
                result = if (result.indexOf("+", result.length-4) > result.indexOf("d", result.length-4)
                    || result.indexOf("-", result.length-4) > result.indexOf("d", result.length-4)){
                    result.dropLast(1)
                }
                //значит в конце стоит куб, его необходимо удалить целиком
                else{
                    //определяем индекс начала куба, удаяем его
                    result.dropLast(result.length - result.indexOf("d", result.length-4))
                }
            }
            //если в последних 4 символах строки  не находится куб удаляем последний символ
            else {
                result = result.dropLast(1)
            }
        }
        lv.postValue(result)
    }

    companion object {
        @Volatile
        private var instance: CalculatorViewModel? = null

        fun getInstance(): CalculatorViewModel {
            return instance ?: synchronized(this) {
                instance ?: CalculatorViewModel().also { instance = it }
            }
        }
    }

   //override fun onRecyclerViewItemClick(view: View, position: Int) {
   //    System.out.println("вызвали функицию onRecyclerViewItemClick во ViewModel")
   //    val position = position
   //    rl.postValue(position)
   //}

   //override fun getInfoHistoryCard(view: View, position: Int) {
   //    System.out.println("position " + position)
   //    System.out.println("info " + HistoryRollManager.getInstance().getItem(position))
   //}

    fun fragmentStop(){
        rl.postValue(null)
    }
}