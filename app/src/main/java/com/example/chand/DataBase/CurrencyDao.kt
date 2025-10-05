package com.example.chand.DataBase

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.chand.DataBase.watchlist.WatchlistItemEntity
import com.example.retrofit_exersice.utils.Constants

@Dao
interface CurrencyDao {

    // ===== Watchlist =====
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchlistItem(item: WatchlistItemEntity)

    @Delete
    suspend fun deleteWatchlistItem(item: WatchlistItemEntity)

    @Query("SELECT * FROM ${Constants.TABLE_NAME}")
    fun getAllWatchlistItems(): LiveData<List<WatchlistItemEntity>>

    @Query("UPDATE ${Constants.TABLE_NAME} SET price = :price, changePercent = :changePercent WHERE symbol = :symbol")
    suspend fun updateWatchlistPriceAndChangePercent(symbol: String, price: String?, changePercent: Double?)

    // ===== Converter =====
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConverterPrices(prices: List<ConverterPriceEntity>)

    @Query("SELECT * FROM ${Constants.TABLE_CONVERTER}")
    suspend fun getAllConverterPrices(): List<ConverterPriceEntity>

    // ===== Alerts =====
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: AlertEntity)

    @Query("SELECT * FROM ${Constants.TABLE_ALERT}")
    suspend fun getAllAlertsList(): List<AlertEntity>

    @Query("SELECT * FROM ${Constants.TABLE_ALERT}")
    fun getAllAlertsLiveData(): LiveData<List<AlertEntity>>

    @Query("UPDATE ${Constants.TABLE_ALERT} SET isActive = :isActive WHERE id = :id")
    suspend fun updateAlertStatus(id: Int, isActive: Boolean)

    @Query("DELETE FROM ${Constants.TABLE_ALERT} WHERE id = :id")
    suspend fun deleteAlert(id: Int)
}
