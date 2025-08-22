package com.example.chand.ViewModel

import androidx.lifecycle.LiveData
import com.example.chand.DataBase.CurrencyDao
import com.example.chand.DataBase.watchlist.WatchlistItemEntity

class WatchlistRepository(private val dao: CurrencyDao) { // نام DAO به CurrencyDao تغییر کرد

    val allItems: LiveData<List<WatchlistItemEntity>> = dao.getAllWatchlistItems()

    suspend fun insert(item: WatchlistItemEntity) {
        dao.insertWatchlistItem(item)
    }

    suspend fun delete(item: WatchlistItemEntity) {
        dao.deleteWatchlistItem(item)
    }

    suspend fun updatePriceAndChangePercent(symbol: String, price: String?, changePercent: Double?) {
        dao.updateWatchlistPriceAndChangePercent(symbol, price, changePercent)
    }
}