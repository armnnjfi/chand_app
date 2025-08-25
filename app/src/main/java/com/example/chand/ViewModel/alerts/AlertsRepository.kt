package com.example.chand.ViewModel.alerts

import androidx.lifecycle.LiveData
import com.example.chand.DataBase.AlertEntity
import com.example.chand.DataBase.CurrencyDao
import com.example.chand.DataBase.watchlist.WatchlistItemEntity

class AlertsRepository(private val dao: CurrencyDao) {

    val allItems: LiveData<List<WatchlistItemEntity>> = dao.getAllWatchlistItems()

    suspend fun insert(item: AlertEntity) {
        dao.insertAlert(item)
    }

    suspend fun delete(id:Int) {
        dao.deleteAlert(id)
    }

    suspend fun updatePriceAndChangePercent(symbol: String, price: String?, changePercent: Double?) {
        dao.updateWatchlistPriceAndChangePercent(symbol, price, changePercent)
    }
}