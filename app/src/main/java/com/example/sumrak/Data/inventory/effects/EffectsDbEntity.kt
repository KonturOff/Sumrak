package com.example.sumrak.Data.inventory.effects

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.sumrak.Data.playerdb.PlayerDbEntity
import com.example.sumrak.ui.inventory.recycler.effects.item.EffectsItem


@Entity(tableName = "Effects",
    foreignKeys = [ForeignKey(
        entity = PlayerDbEntity::class,
        parentColumns = ["id"],
        childColumns = ["idPlayer"],
        onDelete = ForeignKey.CASCADE)
    ]
)
data class EffectsDbEntity(
    @PrimaryKey(autoGenerate = true) val id : Int,
    val idPlayer: Int,
    val name: String,
    val isActive: Int,
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

    fun toEffectsItem() : EffectsItem = EffectsItem(
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
        insihgt =  insihgt,
        observation = observation,
        chsarisma = chsarisma,
        bonusPower = bonusPower,
        bonusEndurance = bonusEndurance
    )
}