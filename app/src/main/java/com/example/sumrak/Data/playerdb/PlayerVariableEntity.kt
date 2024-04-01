package com.example.sumrak.Data.playerdb

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.sumrak.Lists.PlayerVariable


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
    val light_karm : Int,
    val dark_karm : Int,
    val fate : Int,
    val dodge : Int,
    val parrying: Int
) {
    fun toPlayerVariable(): PlayerVariable = PlayerVariable(
        id = id,
        hp = hp,
        light_karm = light_karm,
        dark_karm = dark_karm,
        fate = fate,
        dodge = dodge,
        parrying = parrying
    )
}