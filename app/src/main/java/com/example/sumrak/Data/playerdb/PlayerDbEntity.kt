package com.example.sumrak.Data.playerdb

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.sumrak.Lists.DataPlayer

@Entity(
    tableName = "PlayersTab"
)
data class PlayerDbEntity(
    @PrimaryKey(autoGenerate = true) val id : Int? = null,
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
    val active_arsenal : Int
) {
    fun toDataPlayer() : DataPlayer = DataPlayer(
        id = id,
        name_player = name_player,
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
        bonusPower = 0,
        bonusEndurance = 0,
        max_hp = max_hp,
        max_fate = max_fate,
        influence = influence,
        xp = xp,
        active_armor = active_armor,
        active_arsenal = active_arsenal
    )

    companion object{
        fun fromDataPlayer(dataPlayer: DataPlayer):PlayerDbEntity = PlayerDbEntity(
            id = 0,
            name_player = dataPlayer.name_player,
            db = dataPlayer.db,
            bb = dataPlayer.bb,
            power = dataPlayer.power,
            dexterity = dataPlayer.dexterity,
            volition = dataPlayer.volition,
            endurance = dataPlayer.endurance,
            intelect = dataPlayer.intelect,
            insihgt = dataPlayer.insihgt,
            observation = dataPlayer.observation,
            chsarisma = dataPlayer.chsarisma,
            max_hp = dataPlayer.max_hp,
            max_fate = dataPlayer.max_fate,
            influence = dataPlayer.influence,
            xp = dataPlayer.xp,
            active_armor = dataPlayer.active_armor,
            active_arsenal = dataPlayer.active_arsenal
        )
    }
}