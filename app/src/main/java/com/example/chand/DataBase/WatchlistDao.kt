
package com.example.chand.DataBase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.retrofit_exersice.utils.Constants

@Dao
interface WatchlistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: WatchlistItemEntity)

    @Delete
    suspend fun delete(item: WatchlistItemEntity)

    @Query("SELECT * FROM ${Constants.TABLE_NAME}")
    fun getAll(): LiveData<List<WatchlistItemEntity>>

    @Query("UPDATE ${Constants.TABLE_NAME} SET price = :price, changePercent = :changePercent WHERE symbol = :symbol")
    suspend fun updatePriceAndChangePercent(symbol: String, price: String?, changePercent: Double?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(prices: List<WatchlistItemEntity>)

    @Query("SELECT * FROM ${Constants.TABLE_NAME}")
    fun getAllPrices(): LiveData<List<WatchlistItemEntity>>
}