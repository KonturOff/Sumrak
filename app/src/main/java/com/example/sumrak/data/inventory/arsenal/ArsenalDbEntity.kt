package com.example.sumrak.data.inventory.arsenal

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.sumrak.data.playerdb.PlayerDbEntity
import com.example.sumrak.ui.inventory.recycler.arsenal.item.ArsenalItem

@Entity(
    tableName = "Arsenal",
    foreignKeys = [ForeignKey(
        entity = PlayerDbEntity::class,
        parentColumns = ["id"],
        childColumns = ["idPlayer"],
        onDelete = ForeignKey.CASCADE
    )
    ]
)
data class ArsenalDbEntity(
    @PrimaryKey(autoGenerate = true) val id :Int,
    val idPlayer: Int,
    val classArsenal : Int,
    val name : String,
    val damageX: Int,
    val damageY : Int,
    val damageZ : Int,
    val penetration : Int,
    val step : Int,
    val change: Int,
    val maxPatrons: Int,
    val clip1 : Int,
    val clip2 : Int,
    val clip3 : Int,
    val distanse: Int,
    val valueTest : Int,
    val bonusPower: Boolean,
    val paired : Boolean
)

{
    fun toArsenalItem() : ArsenalItem = ArsenalItem(
        id = id,
        idPlayer = idPlayer,
        classArsenal = classArsenal,
        name = name,
        damageX = damageX,
        damageY = damageY,
        damageZ = damageZ,
        penetration = penetration,
        step = step,
        change = change,
        maxPatrons = maxPatrons,
        clip1 = clip1,
        clip2 = clip2,
        clip3 = clip3,
        distanse = distanse,
        valueTest = valueTest,
        bonusPower = bonusPower,
        paired = paired
    )
}