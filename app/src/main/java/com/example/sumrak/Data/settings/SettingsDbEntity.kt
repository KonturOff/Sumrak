package com.example.sumrak.Data.settings

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "Settings"
)
data class SettingsDbEntity(
    @PrimaryKey(autoGenerate = true) val id : Int? = null,
    val name_settings : String,
    val value : Int)
