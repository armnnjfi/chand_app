package com.example.chand.DataBase

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.retrofit_exersice.utils.Constants

@Entity(tableName = "watchlist")
data class WatchlistItemEntity(
    @PrimaryKey val symbol: String,
    val name: String?,
    val nameEn: String?,
    val price: String?,
    val changePercent: Double?,
    val unit: String?,
    val date: String?,
    val time: String?,
    val type: String   // "currency", "gold", "crypto"
)