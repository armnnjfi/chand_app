package com.example.chand

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.chand.databinding.ActivityMainBinding
import com.example.chand.model.Response_Currency_Price
import com.example.chand.server.ApiClient
import com.example.chand.server.ApiServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    //binding
    private lateinit var binding: ActivityMainBinding
    //other
    private lateinit var navController: NavController
//    private lateinit var appActionBar: ActionBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController=findNavController(R.id.mainFragment)
        binding.apply {
            bottomNav.setupWithNavController(navController)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Alert Channel"
            val descriptionText = "Channel for price alerts"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("alert_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    override fun onNavigateUp(): Boolean {
        return navController.navigateUp() || super.onNavigateUp()
    }
}