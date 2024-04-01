package com.example.sumrak.Data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sumrak.Data.inventory.armor.ArmorDbEntity
import com.example.sumrak.Data.inventory.armor.DaoArmorDb
import com.example.sumrak.Data.inventory.consumables.ConsumablesDbEntity
import com.example.sumrak.Data.inventory.consumables.DaoConsumablesDb
import com.example.sumrak.Data.inventory.effects.DaoEffectsDb
import com.example.sumrak.Data.inventory.effects.EffectsDbEntity
import com.example.sumrak.Data.inventory.equipment.DaoEquipmentDb
import com.example.sumrak.Data.inventory.equipment.EquipmentDbEntity
import com.example.sumrak.Data.inventory.inventoryItem.DaoInventoryItem
import com.example.sumrak.Data.inventory.inventoryItem.InventoryItemEntity
import com.example.sumrak.Data.playerdb.DaoPlayerDb
import com.example.sumrak.Data.playerdb.PlayerDbEntity
import com.example.sumrak.Data.playerdb.PlayerVariableEntity
import com.example.sumrak.Data.settings.SettingsDbEntity


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
    abstract fun getConsumamblesDao(): DaoConsumablesDb
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