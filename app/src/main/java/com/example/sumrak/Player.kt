package com.example.sumrak

import android.content.res.Resources
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
            "Проверка Влияния" -> player.influence
            else -> -1
        }
    }

    fun getSuccessfulHit(summRoll : Int, paramPers: Int, bonus: Int,  change: Int, valueTest: Int, parrying: Boolean): Int {
        var stepen = paramPers + bonus - summRoll
        var successfulHit = 0
        if (summRoll!=1){
            while(stepen>=0){
                successfulHit++
                stepen-=valueTest
            }
            if (summRoll == 2){
                successfulHit += 2
            }
            if (successfulHit > change){
                successfulHit = change
            }
        }
        else{
            successfulHit = change
        }
        if (parrying){
            successfulHit *= 2
        }
        return successfulHit
    }

    fun getParamPlayerToClassArsenal(classArsenal: Int, idPlayer: Int): Int {
        val player = getPlayerToId(idPlayer)
        if (classArsenal==1){
            return player.bb
        }
        else{
            return player.db
        }
    }

    fun getRandomText() : String{

        val list = listOf("Кубодроч не создавай", "Ну сейчас точно единица!", "Критический кто?",
            "Важно не то, как часто ты кидаешь единицы, а то как долго ты выдерживаешь двадцатки",
            "Рерола не будет", "Кидай инициативу, сука", "Не взывай, так не суетим будешь", "Кубы правду знают",
            "Удача отвернулась от тебя", "Хрюкни, если хочешь единицу", "Тебя фантом искал, обосрался?",
            "Была бы граната, а дальний бой нареролим", "Мужики не реролят", "Судьба определяется действием, а не броском",
            "Удача улыбнулась тебе", "Бросай меня, бросай", "Оставь надежду, всяк сюда входящий",
            "Если и реролить, то только с улыбкой", "Семь раз подумай, один раз взывай", "Фантом не волк, в лес не убежит",
            "Не трать очко судьбы, пригодится, в пелене не убиться", "Плохому паломкику и суета мешает",
            "Что за день без рерола?", "Тут без рерола не разобраться...", "Кто здесь?", "Без сучка, без реролинки",
            "Без рерола не вывезешь и миссию в пелене", "За рерол и меч отдам", "Кто реролит, тот не пьет шампанское",
            "Что у ГМа на уме, то у игрока на кубе", "Кубы не мать, можно и бросать", "Сколько куб не перебрасывай, все равно на 20ку смотрит",
            "Че электронные Кубы включил? Играть бросаешь?", "Суета не приходит одна", "В кубы верь, а сам не плошай",
            "Плох тот рерол, что 20ку выдаёт", "Не зная лора, не суйся в воду", "Сколько рейдера ни корми, все равно в пелену смотрит",
            "Рерол костей не ломит", "Не имей 100 рублей, а имей 100 кубов (зачем)", "Кубы как девушки, хер угадаешь",
            "Инициатива тебя не забудет", "Не хватает рерола и чипсов", "Видишь крит? И я не вижу, а он есть",
            "Не по вкусу вкусно, но по сути вкусно", "Чтобы жиром не заплыть, надо жукесов убить", "Силы, карма, два куба (суета)",
            "Сколько фантома ни корми, всё равно тебя сожрёт", "Глаза боятся, а руки кидают суету", "Не говори гоп, пока не перебросишь",
            "На гены надейся, а сам не мутируй", "Уклоняйся, да не зауклонёвывайся", "Школа - второй дом, а ГМ - второй бог",
            "Инициативу по ловкости считают", "Взывание без кармы - суета на ветер", "Хочешь жить, умей Очки судьбы сжигать",
            "Как кубы лягут, так и будет", "Ну ты это, рероль если че", "А мы тут критами балуемся",
            "Волк не тот, кто куб кидает, а тот, кто результат принимает", "Что ГМу радость, то игроку пиздец",
            "Как куб кинешь, так по миссии и поплывёшь", "Не плюй в кубы - пригодится, на 20ке не убиться",
            "Забирай свои единицы и проваливай", "Выйди и зайди нормально", "Чтобы спереди прокинуть, нужно сзади пореролить",
            "В каком же ты отчаянии, раз обратился ко мне...", "Сколько крит не подтверждай, все равно волю не повысишь",
            "Товарищ оперативник, проснитесь, вы обосрались", "Все реролы ведут в крит", "Кто старое рерольнет, тому Очко судьбы вон",
            "Подними свой клинок! Ну или чем ты там сражаешься", "Большое взывание - это большая ответственность",
            "Будешь много взывать, писька на суете отвалится", "Приходи, когда подрастёшь, штаб-сержант", "Д20 от Д100 недалеко падает",
            "Где суетил, там Очко судьбы оставил", "Как это суету не хочешь? Все хотят. Часики то тикают", "Криты - это цветы жизни",
            "Самый сладкая единица бывает только после двадцатки", "Горячее огня только плазма, слаще насвая только единица",
            "Может врезать ему как следует?", "А бросок то с преимуществом", "Ты еще на трусы себе силовое поле прикрути",
            "Выключатель: *висит в ахуе*", "Будь проще и единицы к тебе потянутся", "Дверь закрыта, что будете делать?",
            "Миша, давай по новой, всё хуйня", "Лор не хуй, сиди читай", "Подписывайся на Дзен \"Сумрак 2096\" ",
            "Ты быканул или мне показалось?", "Степени успеха со штрафами считают", "Малиновский без прикола, как куб без рерола",
            "Пелена скрывает истину", "Фантом не нападёт, если ты сдох", "Сумрак это не просто игра - это ролевая игра",
            "За двумя фантомами погонишься, от обоих пизды отхватишь", "Не всё то кислое, что говорит",
            "У семи оперативников запросы на 3 часа", "Что пореролишь, то и пройдёшь (но это не точно)",
            "Куй реролы, пока не умер от двадцатки", "В здоровом рероле - здоровый куб", "Век живи - век рероль",
            "Фантомов бояться - в пелену не ходить", "Готовь броню перед боем, а очко в бою", "Эфемерность фантома бережет",
            "Кто рано ходит, тому бонус к инициативе дали", "Забудь все, чему тебя учили в школе",
            "Потому что только смелый сам полезет в пелену", "Лучше 10 в руках, чем 1 в небе",
            "Любишь взывать - люби и суету вывозить", "Не броня красит человека, а человек броню",
            "Не стыдно провалить, стыдно не реролить", "Один в пелене не два", "Откладывай страх, но не откладывай в штаны",
            "Поспешишь - ГМа насмешишь", "С фантомом рай и в пелене", "Фантом - не воробей, прилетил - не застрелишь",
            "Иерихон и труд все перетрут", "Реролят все, а кто говорит, что не реролит, реролит еще больше", "Курва кубик!",
            "Ебаный рот этого рерола", "Учишься взывать, Поттер?", "За рульбук и лор стреляю в упор",
            "Чем больше мутант, тем громче падает", "С вас 5 тыщ", "Чтобы взывать волшебная палочка не нужна, а вот карма пригодится")
        return list[Random.nextInt(0,list.size)]
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