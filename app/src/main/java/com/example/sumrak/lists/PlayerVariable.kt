package com.example.sumrak.lists

import com.example.sumrak.data.playerdb.PlayerVariableEntity

data class PlayerVariable(
    val id : Int,
    var hp : Int,
    val maxHp : Int,
    var lightKarm : Int,
    var darkKarm : Int,
    var fate : Int,
    val maxFate : Int,
    var dodge : Int,
    var parrying: Int
) {

    fun toPlayerVariableEntity() : PlayerVariableEntity = PlayerVariableEntity(
        id = id,
        hp = hp,
        maxHp = maxHp,
        light_karm = lightKarm,
        dark_karm = darkKarm,
        fate = fate,
        maxFate = maxFate,
        dodge = dodge,
        parrying = parrying
    )
}