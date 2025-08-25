package com.example.chand

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.activityViewModels
import com.example.chand.DataBase.AlertEntity
import com.example.chand.DataBase.ChandDatabase
import com.example.chand.DataBase.toPriceItem
import com.example.chand.ViewModel.alerts.AlertsRepository
import com.example.chand.ViewModel.alerts.AlertsViewModel
import com.example.chand.ViewModel.alerts.AlertsViewModelFactory
import com.example.chand.databinding.FragmentAddAlertBinding
import com.example.chand.server.ApiClient
import com.example.chand.server.ApiServices
import com.example.retrofit_exersice.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log

class AddAlertFragment : Fragment() {

    private var _binding: FragmentAddAlertBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AlertsViewModel by activityViewModels {
        AlertsViewModelFactory(
            AlertsRepository(ChandDatabase.getDatabase(requireActivity().application).dao())
        )
    }
    private val api by lazy { ApiClient().getClient().create<ApiServices>(ApiServices::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddAlertBinding.inflate(inflater, container, false)
        val view = binding.root

        // لود داده‌ها از ViewModel با observe
        viewModel.updateAlertsPrices()
        viewModel.allItems.observe(viewLifecycleOwner) { alerts ->
            Log.d("AddAlertFragment", "Alerts size: ${alerts.size}")
            val priceItems = alerts.map { it.toPriceItem() }
            val symbols = priceItems.mapNotNull { it.symbol }
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, symbols)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.symbolSpinner.adapter = adapter
        }

        // تنظیم دکمه ذخیره
        binding.apply {
            saveButton.setOnClickListener {
                val selectedSymbol = symbolSpinner.selectedItem
                val symbol = if (selectedSymbol != null) selectedSymbol.toString() else {
                    Toast.makeText(requireContext(), "Please select a symbol", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
                val upperLimit = upperLimitEdit.text.toString().toDoubleOrNull() ?: 0.0
                val lowerLimit = lowerLimitEdit.text.toString().toDoubleOrNull() ?: 0.0

                val alert = AlertEntity(
                    symbol = symbol,
                    upperLimit = upperLimit,
                    lowerLimit = lowerLimit
                )
                CoroutineScope(Dispatchers.IO).launch {
                    ChandDatabase.getDatabase(requireContext()).dao().insertAlert(alert)
                    checkPriceAlerts()
                }
                requireActivity().onBackPressed()
            }
        }

        return view
    }

    private fun checkPriceAlerts() {
        CoroutineScope(Dispatchers.IO).launch {
            val callApi = api.getCurrencyPrice(Constants.API_KEY)
            callApi.enqueue(object : Callback<com.example.chand.model.Response_Currency_Price> {
                override fun onResponse(
                    call: Call<com.example.chand.model.Response_Currency_Price>,
                    response: Response<com.example.chand.model.Response_Currency_Price>
                ) {
                    if (!isAdded) {
                        return
                    }
                    if (response.isSuccessful) {
                        response.body()?.let { priceResponse ->
                            val alerts = ChandDatabase.getDatabase(requireContext()).dao()
                                .getAllAlerts().value
                            alerts?.forEach { alert ->
                                priceResponse.currency?.find { it?.symbol == alert.symbol }
                                    ?.let { currency ->
                                        val price = currency.price?.toDoubleOrNull() ?: 0.0
                                        if (price >= alert.upperLimit || price <= alert.lowerLimit) {
                                            sendNotification(alert.symbol, price)
                                        }
                                    }
                            }
                        }
                    }
                }

                override fun onFailure(
                    call: Call<com.example.chand.model.Response_Currency_Price>,
                    t: Throwable
                ) {
                    // مدیریت خطا
                }
            })
        }
    }

    private fun sendNotification(symbol: String, price: Double) {
        val builder = NotificationCompat.Builder(requireContext(), "alert_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Price Alert")
            .setContentText("$symbol reached $price")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(requireContext())) {
            notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}