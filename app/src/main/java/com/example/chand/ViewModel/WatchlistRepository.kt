package com.example.chand.ViewModel

import androidx.lifecycle.LiveData
import com.example.chand.DataBase.WatchlistDao
import com.example.chand.DataBase.WatchlistItemEntity
import com.example.chand.DataBase.toEntity
import com.example.chand.model.PriceItem
import androidx.lifecycle.map
import com.example.chand.DataBase.toPriceItem


class WatchlistRepository(private val dao: WatchlistDao) {

    val allItems: LiveData<List<WatchlistItemEntity>> = dao.getAll()

    suspend fun insert(item: WatchlistItemEntity) {
        dao.insert(item)
    }

    suspend fun delete(item: WatchlistItemEntity) {
        dao.delete(item)
    }

    suspend fun updatePriceAndChangePercent(symbol: String, price: String?, changePercent: Double?) {
        dao.updatePriceAndChangePercent(symbol, price, changePercent)
    }
    suspend fun insertAll(prices: List<PriceItem>) {
        val entities = prices.map { it.toEntity() }
        dao.insertAll(entities)
    }

        fun getAllPrices(): LiveData<List<PriceItem>> {
            return dao.getAll().map { entities ->
                entities.map { it.toPriceItem() }
            }
        }

}
