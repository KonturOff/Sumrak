package com.example.sumrak.ui.inventory.recycler.arsenal.item

import com.example.sumrak.data.inventory.arsenal.ArsenalDbEntity

data class ArsenalItem(
    var id:Int,
    val idPlayer: Int,
    val classArsenal: Int,
    val name: String,
    val damageX: Int,
    val damageY: Int,
    val damageZ: Int,
    val penetration: Int,
    val step: Int,
    val change: Int,
    val maxPatrons: Int,
    var clip1: Int,
    var clip2: Int,
    var clip3: Int,
    val distanse: Int,
    val valueTest: Int,
    val bonusPower: Boolean,
    val paired: Boolean
) {

    fun toArsenalDbEntity() : ArsenalDbEntity = ArsenalDbEntity(
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