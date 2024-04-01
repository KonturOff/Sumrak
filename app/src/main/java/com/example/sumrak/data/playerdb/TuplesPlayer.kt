package com.example.sumrak.data.playerdb

data class UpdateHpPlayerTuples(
    val id : Int,
    val hp : Int
)

data class UpdateFatePlayerTuples(
    val id : Int,
    val fate : Int
)

data class UpdateLightKarmTuples(
    val id: Int,
    val light_karm : Int
)

data class UpdateDarkKarmTuples(
    val id: Int,
    val dark_karm : Int
)

data class UpdateArmorIdPlayerTuples(
    val id: Int,
    val active_armor : Int
)

data class UpdateDodgeParryingTuples(
    val id: Int,
    val dodge : Int,
    val parrying: Int
)