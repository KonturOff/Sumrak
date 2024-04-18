package com.example.sumrak.data.battle

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.sumrak.data.playerdb.PlayerDbEntity
import com.example.sumrak.ui.battle.BattleItem
import com.example.sumrak.ui.battle.recycler.atack.Attack
import com.example.sumrak.ui.battle.recycler.damage.Damage
import com.example.sumrak.ui.battle.recycler.equipment.EquipmentB
import com.example.sumrak.ui.battle.recycler.information.Information
import com.example.sumrak.ui.battle.recycler.initiative.Initiative


@Entity(tableName = "Battle_Item",foreignKeys = [ForeignKey(
    entity = PlayerDbEntity::class,
    parentColumns = ["id"],
    childColumns = ["idPlayer"],
    onDelete = ForeignKey.CASCADE
)
]
)
data class BattleItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val idPlayer : Int,
    val type: String,
    val position: Int,
    val isExpanded: Boolean
) {

    fun toBattleItem() : BattleItem = BattleItem(
        id = id,
        idPlayer = idPlayer,
        type = type,
        position = position,
        isExpanded = isExpanded,
        view = when(type){
            "Информация" -> Information()
            "Получение Урона" -> Damage()
            "Инициатива" -> Initiative()
            "Снаряжение" -> EquipmentB()
            "Атака" -> Attack()
            else -> Information()
        }
    )
}