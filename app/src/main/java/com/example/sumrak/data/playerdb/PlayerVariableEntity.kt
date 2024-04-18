package com.example.sumrak.data.playerdb

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.sumrak.lists.PlayerVariable


@Entity(tableName = "PlayerVariable",
    foreignKeys = [ForeignKey(
    entity = PlayerDbEntity::class,
    parentColumns = ["id"],
    childColumns = ["id"],
    onDelete = ForeignKey.CASCADE)
])
data class PlayerVariableEntity(
    @PrimaryKey val id : Int,
    val hp : Int,
    val maxHp: Int,
    val light_karm : Int,
    val dark_karm : Int,
    val fate : Int,
    val maxFate : Int,
    val dodge : Int,
    val parrying: Int
) {
    fun toPlayerVariable(): PlayerVariable = PlayerVariable(
        id = id,
        hp = hp,
        maxHp = maxHp,
        lightKarm = light_karm,
        darkKarm = dark_karm,
        fate = fate,
        maxFate = maxFate,
        dodge = dodge,
        parrying = parrying
    )
}