package com.example.chand

import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.chand.DataBase.ChandDatabase
import com.example.chand.model.Response_Currency_Price
import com.example.chand.server.ApiClient
import com.example.chand.server.ApiServices
import com.example.retrofit_exersice.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriceCheckWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    companion object {
        private var notificationId = 1
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val dao = ChandDatabase.getDatabase(applicationContext).dao()
        val alerts = dao.getAllAlertsList()

        Log.d("PriceCheckWorker", "Alerts size: ${alerts.size}, Alerts: $alerts")
        if (alerts.isEmpty()) {
            Log.d("PriceCheckWorker", "No alerts to check")
            return@withContext Result.success()
        }

        val api = ApiClient().getClient().create<ApiServices>(ApiServices::class.java)
        val callApi = api.getCurrencyPrice(Constants.API_KEY)
        callApi.enqueue(object : Callback<Response_Currency_Price> {
            override fun onResponse(
                call: Call<Response_Currency_Price>,
                response: Response<Response_Currency_Price>
            ) {
                Log.d("PriceCheckWorker", "API Response: ${response.code()}")
                if (response.isSuccessful) {
                    response.body()?.let { priceResponse ->
                        Log.d("PriceCheckWorker", "Price Response: $priceResponse")
                        alerts.forEach { alert ->
                            priceResponse.currency?.find { it?.symbol == alert.symbol }
                                ?.let { currency ->
                                    val price = currency.price?.toDoubleOrNull() ?: 0.0
                                    Log.d(
                                        "PriceCheckWorker",
                                        "Checking ${alert.symbol}: Price=$price, Upper=${alert.upperLimit}, Lower=${alert.lowerLimit}"
                                    )
                                    if (price >= alert.upperLimit || price <= alert.lowerLimit) {
                                        sendNotification(applicationContext, alert.symbol, price)
                                        Log.d(
                                            "PriceCheckWorker",
                                            "Notification sent for ${alert.symbol}"
                                        )
                                    }
                                } ?: Log.d(
                                "PriceCheckWorker",
                                "No currency found for ${alert.symbol}"
                            )
                        }
                    } ?: Log.d("PriceCheckWorker", "Response body is null")
                } else {
                    Log.e("PriceCheckWorker", "API Response not successful: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Response_Currency_Price>, t: Throwable) {
                Log.e("PriceCheckWorker", "API Failure: ${t.message}")
            }
        })

        return@withContext Result.success()
    }

    private fun sendNotification(context: Context, symbol: String, price: Double) {
        val builder = NotificationCompat.Builder(context, "alert_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Price Alert")
            .setContentText("$symbol reached $price")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId++, builder.build()) // استفاده از یک ID متغیر
            if (notificationId > 1000) notificationId = 1 // جلوگیری از overflow
        }
    }
}