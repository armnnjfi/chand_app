package com.example.chand

import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.chand.DataBase.ChandDatabase
import com.example.chand.server.ApiClient
import com.example.chand.server.ApiServices
import com.example.retrofit_exersice.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class PriceCheckWorker(appContext: Context, workerParams: WorkerParameters)
    : CoroutineWorker(appContext, workerParams) {

    companion object {
        private var notificationId = 1
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            // 🔸 بررسی فعال بودن هشدارها از Settings
            val sharedPref = applicationContext.getSharedPreferences("settingsAlert", Context.MODE_PRIVATE)
            val isAlertsOn = sharedPref.getBoolean("alerts_enabled", true)

            if (!isAlertsOn) {
                Log.d("PriceCheckWorker", "Global alerts disabled")
                return@withContext Result.success()
            }

            val dao = ChandDatabase.getDatabase(applicationContext).dao()
            val alerts = dao.getAllAlertsList().filter { it.isActive } // فقط فعال‌ها

            if (alerts.isEmpty()) {
                Log.d("PriceCheckWorker", "No enabled alerts to check")
                return@withContext Result.success()
            }

            val api = ApiClient().getClient().create(ApiServices::class.java)
            val response = api.getCurrencyPrice(Constants.API_KEY).awaitResponse()

            if (response.isSuccessful) {
                response.body()?.let { priceResponse ->
                    alerts.forEach { alert ->
                        priceResponse.currency?.find { it?.symbol == alert.symbol }?.let { currency ->
                            val price = currency.price?.toDoubleOrNull() ?: 0.0

                            if (true) {
                                sendNotification(applicationContext, alert.symbol, price)
                                Log.d("PriceCheckWorker", "Notification sent for ${alert.symbol}")
                            }
                        }
                    }
                }
            }
            Result.success()
        } catch (e: Exception) {
            Log.e("PriceCheckWorker", "Error: ${e.message}", e)
            Result.failure()
        }
    }

    private fun sendNotification(context: Context, symbol: String, price: Double) {
        val builder = NotificationCompat.Builder(context, "alert_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Price Alert")
            .setContentText("$symbol reached $price")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId++, builder.build())
            if (notificationId > 1000) notificationId = 1
        }
    }
}
