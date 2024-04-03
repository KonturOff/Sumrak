package com.example.sumrak.data.inventory.armor

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.sumrak.data.playerdb.PlayerDbEntity
import com.example.sumrak.ui.inventory.recycler.armor.item.ArmorItem

@Entity(tableName = "Armor",
    foreignKeys = [ForeignKey(
        entity = PlayerDbEntity::class,
        parentColumns = ["id"],
        childColumns = ["idPlayer"],
        onDelete = ForeignKey.CASCADE
    )
    ]
)
data class ArmorDbEntity(
    @PrimaryKey(autoGenerate = true) val id :Int,
    val idPlayer: Int,
    var classArmor: String,
    val name : String,
    val params : Int,
    var endurance : Int,
    val enduranceMax: Int,
    val features: String
) {

    fun toArmorItem(): ArmorItem = ArmorItem(
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