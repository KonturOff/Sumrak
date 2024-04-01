package com.example.sumrak.Lists

import com.example.sumrak.Data.playerdb.PlayerVariableEntity

data class PlayerVariable(
    val id : Int,
    var hp : Int,
    var light_karm : Int,
    var dark_karm : Int,
    var fate : Int,
    var dodge : Int,
    var parrying: Int
) {

    fun toPlayerVariableEntity() : PlayerVariableEntity = PlayerVariableEntity(
        id = id,
        hp = hp,
        light_karm = light_karm,
        dark_karm = dark_karm,
        fate = fate,
        dodge = dodge,
        parrying = parrying
    )
}