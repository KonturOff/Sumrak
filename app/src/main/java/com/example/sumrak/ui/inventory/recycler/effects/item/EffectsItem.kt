package com.example.sumrak.ui.inventory.recycler.effects.item

import com.example.sumrak.Data.inventory.effects.EffectsDbEntity


data class EffectsItem(
    var id : Int,
    val idPlayer: Int,
    val name: String,
    var isActive: Int,
    val db : Int,
    val bb : Int,
    val power : Int,
    val dexterity : Int,
    val volition : Int,
    val endurance : Int,
    val intelect : Int,
    val insihgt : Int,
    val observation : Int,
    val chsarisma : Int,
    val bonusPower : Int,
    val bonusEndurance : Int
    ) {

    fun toEffectsDbEntity() : EffectsDbEntity = EffectsDbEntity(
        id = id,
        idPlayer = idPlayer,
        name = name,
        isActive = isActive,
        db = db,
        bb = bb,
        power = power,
        dexterity = dexterity,
        volition = volition,
        endurance = endurance,
        intelect = intelect,
        insihgt = insihgt,
        observation = observation,
        chsarisma = chsarisma,
        bonusPower = bonusPower,
        bonusEndurance = bonusEndurance
    )
}