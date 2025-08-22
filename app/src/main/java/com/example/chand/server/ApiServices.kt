package com.example.chand.server

import com.example.chand.model.Response_Currency_Price
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    @GET("Market/Gold_Currency.php")
    fun getCurrencyPrice(@Query("key") apiKey: String): Call<Response_Currency_Price>
}