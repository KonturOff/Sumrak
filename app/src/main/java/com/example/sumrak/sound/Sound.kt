package com.example.sumrak.sound

import android.content.Context
import android.media.SoundPool
import com.example.sumrak.Player
import com.example.sumrak.R
import com.example.sumrak.lists.HistoryRollManager
import kotlin.properties.Delegates
import kotlin.random.Random

class Sound {
    private lateinit var soundPool : SoundPool
    private var soundCube by Delegates.notNull<Int>()
    private var soundStandart1 by Delegates.notNull<Int>()
    private var soundStandart20 by Delegates.notNull<Int>()
    private var tinkoff_0hp by Delegates.notNull<Int>()
    private var tinkoff_1 by Delegates.notNull<Int>()
    private var tinkoff_1_1 by Delegates.notNull<Int>()
    private var tinkoff_20 by Delegates.notNull<Int>()
    private var tinkoff_20_1 by Delegates.notNull<Int>()
    private var tinkoff_confirmed_1 by Delegates.notNull<Int>()
    private var tinkoff_sueta_1 by Delegates.notNull<Int>()
    private var tinkoff_sueta_2 by Delegates.notNull<Int>()
    private var tinkoff_sueta_3 by Delegates.notNull<Int>()
    private var tinkoff_sueta_4 by Delegates.notNull<Int>()
    private val leftVolume = 1.0f
    private val rightVolume = 1.0f
    private val priority = 1
    private val loop = 0
    private val rate = 1.0f
    private val historyRoll = HistoryRollManager.getInstance()

    fun loadSound(context: Context){
        soundPool = SoundPool.Builder().setMaxStreams(5).build()
        soundCube = soundPool.load(context, R.raw.cube_roll, 1)
        soundStandart1 = soundPool.load(context, R.raw.standart_1, 1)
        soundStandart20 = soundPool.load(context, R.raw.standart_20, 1)
        tinkoff_0hp = soundPool.load(context, R.raw.tinkoff_0hp, 1)
        tinkoff_1 = soundPool.load(context, R.raw.tinkoff_1, 1)
        tinkoff_1_1 = soundPool.load(context, R.raw.tinkoff_1_1, 1)
        tinkoff_20 = soundPool.load(context, R.raw.tinkoff_20, 1)
        tinkoff_20_1 = soundPool.load(context, R.raw.tinkoff_20_1, 1)
        tinkoff_confirmed_1 = soundPool.load(context, R.raw.tinkoff_confirmed_1, 1)
        tinkoff_sueta_1 = soundPool.load(context, R.raw.tinkoff_sueta_1, 1)
        tinkoff_sueta_2 = soundPool.load(context, R.raw.tinkoff_sueta_2, 1)
        tinkoff_sueta_3 = soundPool.load(context, R.raw.tinkoff_sueta_3, 1)
        tinkoff_sueta_4 = soundPool.load(context, R.raw.tinkoff_sueta_4, 1)

    }
    fun playSoundT(cube: String, result: String) {
        val soundToPlay = when (cube) {
            "d20" -> when (result) {
                "[1]" -> if (Random.nextBoolean()) tinkoff_1 else tinkoff_1_1
                "[20]" -> if (Random.nextBoolean()) tinkoff_20 else tinkoff_20_1
                else -> soundCube
            }
            "2d6" -> when (result) {
                "[1, 1]", "[2, 2]", "[3, 3]", "[4, 4]", "[5, 5]", "[6, 6]" -> {
                    playSoundSuetaTinkoff()
                    return
                }
                else -> soundCube
            }
            else -> soundCube
        }

        soundPool.play(soundToPlay, leftVolume, rightVolume, priority, loop, rate)
    }

    fun playSound(pos : Int){
        val idPlayer = historyRoll.getItem(pos).player
        val sound = Player.getInstance().getPlayerToId(idPlayer).sound
        when(sound){
            "Стандартные Кубы" -> standartSoud(pos)
            "Олег Т" -> tinkoffSound(pos)
        }
    }

    fun playSound0Hp(idPlayer: Int){
        if (Player.getInstance().getPlayerToId(idPlayer).sound == "Олег Т"){
            soundPool.play(tinkoff_0hp, leftVolume, rightVolume, priority, loop, rate)
        }

    }

    private fun tinkoffSound(pos: Int) {
        val item = historyRoll.getItem(pos)
        val cube = item.cube
        val resultRoll = item.textRoll
        val soundToPlay = when (cube) {
            "d20" -> when (resultRoll) {
                "[1]" -> {
                    if (historyRoll.getConfirmedToPos(pos)){
                        tinkoff_confirmed_1
                    }
                    else{
                        if (Random.nextBoolean()) tinkoff_1 else tinkoff_1_1
                    }
                }
                "[20]" -> if (Random.nextBoolean()) tinkoff_20 else tinkoff_20_1
                else -> soundCube
            }
            "2d6" -> when (resultRoll) {
                "[1, 1]", "[2, 2]", "[3, 3]", "[4, 4]", "[5, 5]", "[6, 6]" -> {
                    playSoundSuetaTinkoff()
                    return
                }
                else -> soundCube
            }
            else -> soundCube
        }

        soundPool.play(soundToPlay, leftVolume, rightVolume, priority, loop, rate)
    }

    private fun standartSoud(pos: Int) {
        val item = historyRoll.getItem(pos)
        val cube = item.cube
        val resultRoll = item.resultRoll
        if (cube == "d20"){
            when(resultRoll.toInt()){
                1-> soundPool.play(soundStandart1, leftVolume, rightVolume, priority, loop, rate)
                20-> soundPool.play(soundStandart20, leftVolume, rightVolume, priority, loop, rate)
                else-> soundPool.play(soundCube, leftVolume, rightVolume, priority, loop, rate)
            }
        }
        else{
            soundPool.play(soundCube, leftVolume, rightVolume, priority, loop, rate)
        }
    }

    fun playSoundSuetaTinkoff(){
        val t = Random.nextInt(4)
        when(t){
            0-> soundPool.play(tinkoff_sueta_1, leftVolume, rightVolume, priority, loop, rate)
            1-> soundPool.play(tinkoff_sueta_2, leftVolume, rightVolume, priority, loop, rate)
            2-> soundPool.play(tinkoff_sueta_3, leftVolume, rightVolume, priority, loop, rate)
            3-> soundPool.play(tinkoff_sueta_4, leftVolume, rightVolume, priority, loop, rate)
        }
    }

    companion object {
        private val instance = Sound()

        fun getInstance(): Sound {
            return instance
        }
    }

}