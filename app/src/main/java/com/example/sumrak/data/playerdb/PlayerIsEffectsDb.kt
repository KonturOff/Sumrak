package com.example.sumrak.data.playerdb


import com.example.sumrak.lists.DataPlayer

data class PlayerIsEffectsDb(
    val id : Int,
    val name_player : String,
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
    val max_hp : Int,
    val max_fate : Int,
    val influence : Int,
    val xp : Int,
    val active_armor : Int,
    val active_arsenal : Int,
    val total_db : Int,
    val total_bb : Int,
    val total_power : Int,
    val total_dexterity : Int,
    val total_volition : Int,
    val total_endurance : Int,
    val total_intelect : Int,
    val total_insihgt : Int,
    val total_observation : Int,
    val total_chsarisma : Int,
    val total_bonus_power : Int,
    val total_bonus_endurance : Int,

) {
    fun toDataPlayer():DataPlayer = DataPlayer(
        id = id,
        namePlayer = name_player,
        db = db + total_db,
        bb = bb + total_bb,
        power = power + total_power,
        dexterity = dexterity + total_dexterity,
        volition = volition + total_volition,
        endurance = endurance + total_endurance,
        intelect = intelect + total_intelect,
        insihgt = insihgt + total_insihgt,
        observation = observation + total_observation,
        chsarisma = chsarisma + total_chsarisma,
        bonusPower = total_bonus_power,
        bonusEndurance = total_bonus_endurance,
        maxHp = max_hp,
        maxFate = max_fate,
        influence = influence,
        xp = xp,
        activeArmor = active_armor,
        activeArsenal = active_arsenal
    )
}