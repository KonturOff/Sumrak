package com.example.sumrak.ui.inventory.recycler.armor.item

import com.example.sumrak.data.inventory.armor.ArmorDbEntity

data class ArmorItem(
    var id : Int,
    var idPlayer: Int,
    var classArmor: String,
    var name : String,
    var params : Int,
    var endurance : Int,
    var enduranceMax: Int,
    var features: String
) {
    fun toArmorDbEntity(): ArmorDbEntity = ArmorDbEntity(
        id = id,
        idPlayer = idPlayer,
        classArmor = classArmor,
        name = name,
        params = params,
        endurance = endurance,
        enduranceMax = enduranceMax,
        features = features
    )
}