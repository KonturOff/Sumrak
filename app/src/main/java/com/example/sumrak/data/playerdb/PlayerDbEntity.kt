package com.example.sumrak.data.playerdb

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.sumrak.lists.DataPlayer

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
    val active_arsenal : Int,
    val classPers: String,
    val rank : String,
    val bitchPlace : String,
    val markFate : String,
    val skills : String,
    val profile : String,
    val sound : String
) {
    fun toDataPlayer() : DataPlayer = DataPlayer(
        id = id,
        namePlayer = name_player,
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
        maxHp = max_hp,
        maxFate = max_fate,
        influence = influence,
        xp = xp,
        activeArmor = active_armor,
        activeArsenal = active_arsenal,
        classPers = classPers,
        rank = rank,
        bitchPlace = bitchPlace,
        markFate = markFate,
        skills = skills,
        profile = profile,
        sound = sound
    )

    companion object{
        fun fromDataPlayer(dataPlayer: DataPlayer):PlayerDbEntity = PlayerDbEntity(
            id = 0,
            name_player = dataPlayer.namePlayer,
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
            max_hp = dataPlayer.maxHp,
            max_fate = dataPlayer.maxFate,
            influence = dataPlayer.influence,
            xp = dataPlayer.xp,
            active_armor = dataPlayer.activeArmor,
            active_arsenal = dataPlayer.activeArsenal,
            classPers = dataPlayer.classPers,
            rank = dataPlayer.rank,
            bitchPlace = dataPlayer.bitchPlace,
            markFate = dataPlayer.markFate,
            skills = dataPlayer.skills,
            profile = dataPlayer.profile,
            sound = dataPlayer.sound
        )
    }
}