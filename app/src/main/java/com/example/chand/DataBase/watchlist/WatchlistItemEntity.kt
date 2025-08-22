package com.example.chand.DataBase.watchlist

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.retrofit_exersice.utils.Constants

@Entity(tableName = Constants.TABLE_NAME)
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