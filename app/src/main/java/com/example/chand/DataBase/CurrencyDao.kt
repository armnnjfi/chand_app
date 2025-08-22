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
    // متدهای Watchlist
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchlistItem(item: WatchlistItemEntity)

    @Delete
    suspend fun deleteWatchlistItem(item: WatchlistItemEntity)

    @Query("SELECT * FROM ${Constants.TABLE_NAME}")
    fun getAllWatchlistItems(): LiveData<List<WatchlistItemEntity>>

    @Query("UPDATE ${Constants.TABLE_NAME} SET price = :price, changePercent = :changePercent WHERE symbol = :symbol")
    suspend fun updateWatchlistPriceAndChangePercent(symbol: String, price: String?, changePercent: Double?)

    // متدهای Converter
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConverterPrices(prices: List<ConverterPriceEntity>)

    @Query("SELECT * FROM converter_prices")
    suspend fun getAllConverterPrices(): List<ConverterPriceEntity>
}