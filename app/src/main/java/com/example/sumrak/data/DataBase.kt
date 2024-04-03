package com.example.sumrak.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sumrak.data.inventory.armor.ArmorDbEntity
import com.example.sumrak.data.inventory.armor.DaoArmorDb
import com.example.sumrak.data.inventory.consumables.ConsumablesDbEntity
import com.example.sumrak.data.inventory.consumables.DaoConsumablesDb
import com.example.sumrak.data.inventory.effects.DaoEffectsDb
import com.example.sumrak.data.inventory.effects.EffectsDbEntity
import com.example.sumrak.data.inventory.equipment.DaoEquipmentDb
import com.example.sumrak.data.inventory.equipment.EquipmentDbEntity
import com.example.sumrak.data.inventory.inventoryItem.DaoInventoryItem
import com.example.sumrak.data.inventory.inventoryItem.InventoryItemEntity
import com.example.sumrak.data.playerdb.DaoPlayerDb
import com.example.sumrak.data.playerdb.PlayerDbEntity
import com.example.sumrak.data.playerdb.PlayerVariableEntity
import com.example.sumrak.data.settings.SettingsDbEntity


@Database (
    entities = [
        PlayerDbEntity::class,
        PlayerVariableEntity::class,
        SettingsDbEntity::class,
        InventoryItemEntity::class,
        ArmorDbEntity::class,
        ConsumablesDbEntity::class,
        EffectsDbEntity::class,
        EquipmentDbEntity::class
               ],
    version = 1,
    exportSchema = false
)
abstract class DataBase : RoomDatabase() {
    abstract fun getPlayerDao(): DaoPlayerDb
    abstract fun getInventoryItemDao() : DaoInventoryItem
    abstract fun getArmorDao(): DaoArmorDb
    abstract fun getConsumablesDao(): DaoConsumablesDb
    abstract fun getEffectsDao() : DaoEffectsDb
    abstract fun getEquipmentDao() : DaoEquipmentDb

    companion object{
        @Volatile
        private var INSTANCE: DataBase? = null


        fun getDb(context: Context) : DataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DataBase::class.java,
                    "DataBase"
                ).build()
                INSTANCE = instance
                instance
            }

        }
    }
}