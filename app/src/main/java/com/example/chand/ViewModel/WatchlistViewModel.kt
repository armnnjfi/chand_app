
package com.example.chand.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chand.DataBase.WatchlistItemEntity
import com.example.chand.DataBase.toEntity
import com.example.chand.model.PriceItem
import com.example.chand.model.Response_Currency_Price
import com.example.chand.server.ApiClient
import com.example.chand.server.ApiServices
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WatchlistViewModel(private val repository: WatchlistRepository) : ViewModel() {

    private val api by lazy { ApiClient().getClient().create<ApiServices>(ApiServices::class.java) }

    val allItems: LiveData<List<WatchlistItemEntity>> = repository.allItems

    fun addToWatchlist(item: WatchlistItemEntity) {
        viewModelScope.launch {
            repository.insert(item)
        }
    }

    fun removeFromWatchlist(item: WatchlistItemEntity) {
        viewModelScope.launch {
            repository.delete(item)
        }
    }
    fun savePrices(prices: List<PriceItem>) {
        viewModelScope.launch {
            repository.insertAll(prices) // باید در Repository تابع insertAll بسازی
        }
    }

    fun getSavedPrices(): LiveData<List<PriceItem>> {
        return repository.getAllPrices()
    }

    fun updateWatchlistPrices() {
        viewModelScope.launch {
            val callApi = api.getCurrencyPrice("FreejCpsMTnCQC5VM5mod6U35aNqCq5c")
            callApi.enqueue(object : Callback<Response_Currency_Price> {
                override fun onResponse(
                    call: Call<Response_Currency_Price?>,
                    response: Response<Response_Currency_Price?>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { itBody ->
                            val priceItems = mutableListOf<PriceItem>()
                            itBody.currency?.filterNotNull()?.forEach { currency ->
                                priceItems.add(PriceItem.CurrencyItem(currency))
                            }
                            itBody.gold?.filterNotNull()?.forEach { gold ->
                                priceItems.add(PriceItem.GoldItem(gold))
                            }
                            itBody.cryptocurrency?.filterNotNull()?.forEach { crypto ->
                                priceItems.add(PriceItem.CryptocurrencyItem(crypto))
                            }

                            viewModelScope.launch {
                                priceItems.forEach { priceItem ->
                                    val entity = priceItem.toEntity()
                                    repository.updatePriceAndChangePercent(
                                        entity.symbol,
                                        entity.price,
                                        entity.changePercent
                                    )
                                }
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<Response_Currency_Price?>, t: Throwable) {
                    Log.e("onFailure","api Failure")
                }
            })
        }
    }
}