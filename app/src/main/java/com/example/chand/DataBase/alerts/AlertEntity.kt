package com.example.chand.DataBase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alerts")
data class AlertEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val symbol: String, // نماد ارز (مثل USD, EUR)
    val upperLimit: Double, // سقف قیمت
    val lowerLimit: Double, // کف قیمت
    val isActive: Boolean = true // وضعیت فعال/غیرفعال
)