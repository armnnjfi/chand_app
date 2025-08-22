package com.example.chand

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

    private val api by lazy { ApiClient().getClient().create<ApiServices>(ApiServices::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController=findNavController(R.id.mainFragment)
        binding.apply {
            bottomNav.setupWithNavController(navController)
        }
    }


    override fun onNavigateUp(): Boolean {
        return navController.navigateUp() || super.onNavigateUp()
    }
}