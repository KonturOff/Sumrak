package com.example.sumrak.lists

import com.example.sumrak.data.playerdb.PlayerVariableEntity

data class PlayerVariable(
    val id : Int,
    var hp : Int,
    var lightKarm : Int,
    var darkKarm : Int,
    var fate : Int,
    var dodge : Int,
    var parrying: Int
) {

    fun toPlayerVariableEntity() : PlayerVariableEntity = PlayerVariableEntity(
        id = id,
        hp = hp,
        light_karm = lightKarm,
        dark_karm = darkKarm,
        fate = fate,
        dodge = dodge,
        parrying = parrying
    )
}