package com.example.sumrak

import com.example.sumrak.Data.playerdb.DaoPlayerDb
import com.example.sumrak.Data.playerdb.PlayerViewModel
import com.example.sumrak.Lists.DataPlayer
import com.example.sumrak.Lists.PlayerVariable
import com.example.sumrak.ui.home.HomeFragment
import com.example.sumrak.ui.home.HomeViewModel
import kotlin.collections.ArrayList
import kotlin.random.Random


class Player private constructor(){

    lateinit var playerEntity : PlayerVariable
    private val playerList = ArrayList<DataPlayer>()
    var activPosPlayer = 0

    lateinit var random : Random


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
    fun getPlayer(position: Int): DataPlayer {
        return playerList[position]
    }
    //Получаем экземпляр персонажа по id
    fun getPlayerToId(id : Int): DataPlayer{
        return getPlayer(getPosPlayer(id))
    }

    //Получаем экземпляр Активного персонажа
    fun getActivePlayer(): DataPlayer{
        return playerList[activPosPlayer]
    }

    //Получаем о количестве перснажей в листе
    fun getPlayerCount(): Int {
        return playerList.size
    }

    // Получить Имя активного персонажа
    fun getNamePlayer() : String{
        return playerList[activPosPlayer].name_player
    }

    //получаем id активного игрока по Позиции
    fun getIdActivePlayer() : Int{
        if (playerList[activPosPlayer].id == null){
            return 0
        }
        else{
            return playerList[activPosPlayer].id!!
        }

    }

    // получем позицию игрока по Id 
    fun getPosPlayer(id : Int): Int {
        var t : Int = 0
        for (i in 0..playerList.size-1){
            if (playerList[i].id == id){
                t = i
                return t
            }
        }
        return t
    }

    fun getIdPlayerToPlayerList(): ArrayList<Int> {
        var idList = ArrayList<Int>()
        for (i in playerList.indices){
            idList.add(playerList[i].id!!)
        }
        return idList
    }

    fun getRandomText() : String{
        val list = listOf<String>("Кубы правду знают", "Удача отвернулась от тебя" , "Хрюкни, если хочешь единицу",
            "Тебя фантом искал, обосрался?", "Кубодроч не создавай", "Ну сейчас точно единица!", "Критический кто?", "Важно не то, как часто ты кидаешь единицы, а то как долго ты выдерживаешь двадцатки",
            "Рерола не будет", "Кидай инициативу, сука!", "Не взывай, так не суетим будешь")
        return list[Random.nextInt(0,list.size)]
    }

    fun getPlayerParametr(id: Int, mode: String): Int{
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


        fun roll_cube(cube: String): ArrayList<String> {
            // инициализируем перменные в которых будем считать результат и записывать расчеты
            var result_roll = ""
            var text_roll = ""
            var max_cube = 0

            // делим строку cube на массив строк по разделителю "+"
            var split_plus = cube.split("+").toMutableList()
            for (i in 0..split_plus.size - 1) {
                // инициализируем переменную для внесения записей при переборе массива split_plus
                var text_roll_minus = ""
                // делим строку split_plus[i] на массив строк по разделителю "-"
                var split_minus = split_plus[i].split("-").toMutableList()
                for (j in 0..split_minus.size - 1) {
                    //если в строке split_minus[j] присутствует d делим на массив с разделителем d
                    if (split_minus[j].contains("d")) {
                        // инициализируем временную переменную для записей результата броска кубиков
                        // в общей сложности получится [n, n+1, n+2,...] где n результат броска
                        var text_roll_d = "["
                        var n_roll = 0
                        var split_d = split_minus[j].split("d").toMutableList()
                        if (split_d[0] == "") {
                            split_d[0] = "1"
                        }
                        // проверяем какой индекс куба записан в переменную max_cube
                        // если текущий куб больше, записываем его
                        if (split_d[1].toInt() > max_cube) {
                            max_cube = split_d[1].toInt()
                        }
                        for (k in 0..split_d[0].toInt() - 1) {
                            var cube_roll = Random.nextInt(split_d[1].toInt()) + 1
                            n_roll = n_roll + cube_roll
                            if (k != split_d[0].toInt() - 1) {
                                text_roll_d = "$text_roll_d$cube_roll, "
                            } else text_roll_d = "$text_roll_d$cube_roll]"
                        }
                        split_minus[j] = n_roll.toString()
                        if (j == 0) {
                            split_plus[i] = split_minus[j]
                            text_roll_minus = text_roll_d
                        } else {
                            split_plus[i] =
                                (split_plus[i].toInt() - split_minus[j].toInt()).toString()
                            text_roll_minus = text_roll_minus + "-" + text_roll_d
                        }
                    } else {
                        if (j == 0) {
                            split_plus[i] = split_minus[j]
                            text_roll_minus = split_minus[j]
                        } else {
                            split_plus[i] =
                                (split_plus[i].toInt() - split_minus[j].toInt()).toString()
                            text_roll_minus = text_roll_minus + "-" + split_minus[j]
                        }
                    }
                }
                if (i == 0) {
                    result_roll = split_plus[i]
                    text_roll = text_roll_minus
                } else {
                    result_roll = (result_roll.toInt() + split_plus[i].toInt()).toString()
                    text_roll = text_roll + "+" + text_roll_minus
                }
            }

            return arrayListOf<String>(cube, result_roll, text_roll, max_cube.toString())
        }


    }

}