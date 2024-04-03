package com.example.sumrak

import com.example.sumrak.lists.DataPlayer
import com.example.sumrak.lists.PlayerVariable
import kotlin.collections.ArrayList
import kotlin.random.Random


class Player private constructor(){

    var playerEntity : PlayerVariable? = null
    private val playerList = ArrayList<DataPlayer>()
    var activePosPlayer = 0

    //очищаем список персонажей
    fun clearPlayerList(){
        playerList.clear()
    }

    //Добавляем персонажа в лист
    fun addPlayer(player: DataPlayer) {
        playerList.add(player)
    }

    fun setPlayerVariable(playerVariable: PlayerVariable){
        playerEntity = playerVariable
    }

    fun getBonusEndurance(): Int {
        val endurance = getActivePlayer().endurance
        val bonusEndurance = getActivePlayer().bonusEndurance

        return when {
            endurance <= 3 -> bonusEndurance
            endurance <= 6 -> 1 + bonusEndurance
            endurance <= 9 -> 2 + bonusEndurance
            endurance <= 12 -> 3 + bonusEndurance
            endurance <= 15 -> 4 + bonusEndurance
            endurance <= 18 -> 5 + bonusEndurance
            endurance <= 20 -> 6 + bonusEndurance
            else -> 7 + bonusEndurance
        }
    }

    fun getBonusPower(): Int{
        val power = getActivePlayer().power
        val bonusPower = getActivePlayer().bonusPower
        return kotlin.math.ceil(power.toDouble() / 2).toInt() + bonusPower
    }

    //Получаем экземпляр персонажа
    private fun getPlayer(position: Int): DataPlayer {
        return playerList[position]
    }
    //Получаем экземпляр персонажа по id
    fun getPlayerToId(id : Int): DataPlayer{
        return getPlayer(getPosPlayer(id))
    }

    //Получаем экземпляр Активного персонажа
    fun getActivePlayer(): DataPlayer{
        return playerList[activePosPlayer]
    }

    //Получаем о количестве перснажей в листе
    fun getPlayerCount(): Int {
        return playerList.size
    }

    // Получить Имя активного персонажа
    fun getNamePlayer() : String{
        return playerList[activePosPlayer].namePlayer
    }

    //получаем id активного игрока по Позиции
    fun getIdActivePlayer() : Int{
        return if (playerList[activePosPlayer].id == null){
            0
        } else{
            playerList[activePosPlayer].id!!
        }
    }

    // получем позицию игрока по Id 
    fun getPosPlayer(id : Int): Int {
        var t : Int = 0
        for (i in 0..< playerList.size){
            if (playerList[i].id == id){
                t = i
                return t
            }
        }
        return t
    }

    fun getIdPlayerToPlayerList(): ArrayList<Int> {
        val idList = ArrayList<Int>()
        for (i in playerList.indices){
            idList.add(playerList[i].id!!)
        }
        return idList
    }

    fun getRandomText() : String{
        val list = listOf("Кубы правду знают", "Удача отвернулась от тебя" , "Хрюкни, если хочешь единицу",
            "Тебя фантом искал, обосрался?", "Кубодроч не создавай", "Ну сейчас точно единица!", "Критический кто?", "Важно не то, как часто ты кидаешь единицы, а то как долго ты выдерживаешь двадцатки",
            "Рерола не будет", "Кидай инициативу, сука!", "Не взывай, так не суетим будешь")
        return list[Random.nextInt(0,list.size)]
    }

    fun getPlayerParameter(id: Int, mode: String): Int{
        val player = getPlayerToId(id)
        return when(mode){
            "Проверка Дальнего боя" -> player.db
            "Проверка Ближнего боя" -> player.bb
            "Проверка Силы" -> player.power
            "Проверка Ловкости" -> player.dexterity
            "Проверка Воли" -> player.volition
            "Проверка Стойкости" -> player.endurance
            "Проверка Интелекта" -> player.intelect
            "Проверка Сообразительности" -> player.insihgt
            "Проверка Наблюдательности" -> player.observation
            "Проверка Харизмы" -> player.chsarisma
            "Проверка Инициативы" -> player.dexterity
            else -> -1
        }
    }

    companion object {
        private val instance = Player()

        @JvmStatic
        fun getInstance(): Player {
            return instance
        }

        // TODO какой-то дикий вложенный в 3(!) раза цикл. Таких конструкций нужно избегать.
        fun rollCube(cube: String): ArrayList<String> {
            // инициализируем перменные в которых будем считать результат и записывать расчеты
            var resultRoll = ""
            var textRoll = ""
            var maxCube = 0

            // делим строку cube на массив строк по разделителю "+"
            val splitPlus = cube.split("+").toMutableList()
            for (i in 0..< splitPlus.size) {
                // инициализируем переменную для внесения записей при переборе массива split_plus
                var textRollMinus = ""
                // делим строку split_plus[i] на массив строк по разделителю "-"
                val splitMinus = splitPlus[i].split("-").toMutableList()
                for (j in 0..< splitMinus.size) {
                    //если в строке split_minus[j] присутствует d делим на массив с разделителем d
                    if (splitMinus[j].contains("d")) {
                        // инициализируем временную переменную для записей результата броска кубиков
                        // в общей сложности получится [n, n+1, n+2,...] где n результат броска
                        var textRollD = "["
                        var nRoll = 0
                        val splitD = splitMinus[j].split("d").toMutableList()
                        if (splitD[0] == "") {
                            splitD[0] = "1"
                        }
                        // проверяем какой индекс куба записан в переменную max_cube
                        // если текущий куб больше, записываем его
                        if (splitD[1].toInt() > maxCube) {
                            maxCube = splitD[1].toInt()
                        }
                        for (k in 0..< splitD[0].toInt()) {
                            val cubeRoll = Random.nextInt(splitD[1].toInt()) + 1
                            nRoll += cubeRoll
                            textRollD = if (k != splitD[0].toInt() - 1) {
                                "$textRollD$cubeRoll, "
                            } else "$textRollD$cubeRoll]"
                        }
                        splitMinus[j] = nRoll.toString()
                        if (j == 0) {
                            splitPlus[i] = splitMinus[j]
                            textRollMinus = textRollD
                        } else {
                            splitPlus[i] =
                                (splitPlus[i].toInt() - splitMinus[j].toInt()).toString()
                            textRollMinus = "$textRollMinus-$textRollD"
                        }
                    } else {
                        if (j == 0) {
                            splitPlus[i] = splitMinus[j]
                            textRollMinus = splitMinus[j]
                        } else {
                            splitPlus[i] =
                                (splitPlus[i].toInt() - splitMinus[j].toInt()).toString()
                            textRollMinus = "$textRollMinus-${splitMinus[j]}"
                        }
                    }
                }
                if (i == 0) {
                    resultRoll = splitPlus[i]
                    textRoll = textRollMinus
                } else {
                    resultRoll = (resultRoll.toInt() + splitPlus[i].toInt()).toString()
                    textRoll = "$textRoll+$textRollMinus"
                }
            }

            return arrayListOf(cube, resultRoll, textRoll, maxCube.toString())
        }
    }
}