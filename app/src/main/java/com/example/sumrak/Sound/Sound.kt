package com.example.sumrak.Sound

import android.content.Context
import android.media.SoundPool
import com.example.sumrak.R
import kotlin.properties.Delegates

class Sound {
    private lateinit var soundPool : SoundPool
    private var soundCube by Delegates.notNull<Int>()
    private var soundStandart1 by Delegates.notNull<Int>()
    private var soundStandart20 by Delegates.notNull<Int>()
    private val leftVolume = 1.0f
    private val rightVolume = 1.0f
    private val priority = 1
    private val loop = 0
    private val rate = 1.0f

    fun loadSound(context: Context){
        soundPool = SoundPool.Builder().setMaxStreams(5).build()
        soundCube = soundPool.load(context, R.raw.cube_roll, 1)
        soundStandart1 = soundPool.load(context, R.raw.standart_1, 1)
        soundStandart20 = soundPool.load(context, R.raw.standart_20, 1)
    }

    fun playSound(cube: String, result: String){
        if (cube == "d20"){
            when(result.toInt()){
                1-> soundPool.play(soundStandart1, leftVolume, rightVolume, priority, loop, rate)
                20-> soundPool.play(soundStandart20, leftVolume, rightVolume, priority, loop, rate)
                else-> soundPool.play(soundCube, leftVolume, rightVolume, priority, loop, rate)
            }
        }
        else{
            soundPool.play(soundCube, leftVolume, rightVolume, priority, loop, rate)
        }


    }

    companion object {
        private val instance = Sound()

        fun getInstance(): Sound {
            return instance
        }
    }

}