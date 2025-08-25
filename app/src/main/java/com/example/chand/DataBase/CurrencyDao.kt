package com.example.chand.DataBase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.chand.DataBase.watchlist.WatchlistItemEntity
import com.example.retrofit_exersice.utils.Constants

@Dao
interface CurrencyDao {
    // Watchlist
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchlistItem(item: WatchlistItemEntity)

    @Delete
    suspend fun deleteWatchlistItem(item: WatchlistItemEntity)

    @Query("SELECT * FROM ${Constants.TABLE_NAME}")
    fun getAllWatchlistItems(): LiveData<List<WatchlistItemEntity>>

    @Query("UPDATE ${Constants.TABLE_NAME} SET price = :price, changePercent = :changePercent WHERE symbol = :symbol")
    suspend fun updateWatchlistPriceAndChangePercent(symbol: String, price: String?, changePercent: Double?)

    // Converter
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConverterPrices(prices: List<ConverterPriceEntity>)

    @Query("SELECT * FROM ${Constants.TABLE_CONVERTER}")
    suspend fun getAllConverterPrices(): List<ConverterPriceEntity>

    // متدهای Alerts
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: AlertEntity)

    @Query("SELECT * FROM ${Constants.TABLE_ALERT}")
    fun getAllAlerts(): LiveData<List<AlertEntity>>

    @Query("UPDATE ${Constants.TABLE_ALERT} SET isActive = :isActive WHERE id = :id")
    suspend fun updateAlertStatus(id: Int, isActive: Boolean)

    @Query("Delete From ${Constants.TABLE_ALERT} WHERE id = :id")
    suspend fun deleteAlert(id: Int)
}