package com.example.chand.ViewModel.alerts

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chand.DataBase.AlertEntity
import com.example.chand.DataBase.watchlist.WatchlistItemEntity
import com.example.chand.model.PriceItem
import com.example.chand.model.Response_Currency_Price
import com.example.chand.server.ApiClient
import com.example.chand.server.ApiServices
import com.example.retrofit_exersice.utils.Constants
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlertsViewModel(private val repository: AlertsRepository) : ViewModel() {

    private val api by lazy { ApiClient().getClient().create<ApiServices>(ApiServices::class.java) }

    val allItems: LiveData<List<WatchlistItemEntity>> = repository.allItems

    fun addToAlertList(item: AlertEntity) {
        viewModelScope.launch {
            repository.insert(item)
        }
    }

    fun removeFromAlertList(id: Int) {
        viewModelScope.launch {
            repository.delete(id)
        }
    }

    fun updateAlertsPrices() {
        viewModelScope.launch {
            val callApi = api.getCurrencyPrice(Constants.API_KEY)
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
                                    val entity = AlertEntity(
                                        symbol = priceItem.symbol!!,
                                        upperLimit = 0.0, // مقدار پیش‌فرض
                                        lowerLimit = 0.0  // مقدار پیش‌فرض
                                    )
//                                    repository.insert(entity) // ذخیره تو AlertEntity

                                }
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<Response_Currency_Price?>, t: Throwable) {
                    Log.e("onFailure", "api Failure: ${t.message}")
                }
            })
        }
    }

}