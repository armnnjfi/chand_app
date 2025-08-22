package com.example.chand

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.chand.databinding.FragmentConverterBinding
import com.example.chand.model.PriceItem
import com.example.chand.model.Response_Currency_Price
import com.example.chand.server.ApiClient
import com.example.chand.server.ApiServices
import com.example.chand.DataBase.ChandDatabase
import com.example.chand.DataBase.toConverterEntity
import com.example.chand.DataBase.toPriceItem
import com.example.chand.ViewModel.WatchlistRepository
import com.example.chand.ViewModel.WatchlistViewModel
import com.example.chand.ViewModel.WatchlistViewModelFactory
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConverterFragment : Fragment() {

    private lateinit var binding: FragmentConverterBinding
    private val viewModel: WatchlistViewModel by activityViewModels {
        WatchlistViewModelFactory(
            WatchlistRepository(ChandDatabase.getDatabase(requireContext()).dao())
        )
    }
    private val api by lazy { ApiClient().getClient().create<ApiServices>(ApiServices::class.java) }
    private var priceItems: List<PriceItem> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConverterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.arrowBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.swapButton.setOnClickListener {
            swapCurrencies()
        }

        loadCurrencyData()

        setupConversionListeners()
    }

    private fun loadCurrencyData() {
        val callApi = api.getCurrencyPrice("FreejCpsMTnCQC5VM5mod6U35aNqCq5c")
        callApi.enqueue(object : Callback<Response_Currency_Price> {
            override fun onResponse(
                call: Call<Response_Currency_Price?>,
                response: Response<Response_Currency_Price?>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { itBody ->
                        priceItems = mutableListOf<PriceItem>().apply {
                            itBody.currency?.filterNotNull()?.forEach { add(PriceItem.CurrencyItem(it)) }
                            itBody.gold?.filterNotNull()?.forEach { add(PriceItem.GoldItem(it)) }
                            itBody.cryptocurrency?.filterNotNull()?.forEach { add(PriceItem.CryptocurrencyItem(it)) }
                            add(PriceItem.TomanItem)
                        }

                        // ذخیره در دیتابیس برای آفلاین
                        lifecycleScope.launch {
                            val converterEntities = priceItems.map { it.toConverterEntity() }
                            ChandDatabase.getDatabase(requireContext()).dao().insertConverterPrices(converterEntities)
                        }

                        updateSpinners()
                    }
                } else {
                    loadOfflineData()
                }
            }

            override fun onFailure(call: Call<Response_Currency_Price?>, t: Throwable) {
                loadOfflineData()
                android.widget.Toast.makeText(
                    requireContext(),
                    "خطا در لود داده‌ها: ${t.message} - استفاده از داده‌های آفلاین",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun loadOfflineData() {
        lifecycleScope.launch {
            val converterPrices = ChandDatabase.getDatabase(requireContext()).dao().getAllConverterPrices()
            priceItems = converterPrices.map { it.toPriceItem() }

            // مطمئن شو که تومن در لیست باشه (در صورتی که در دیتابیس نیست)
            if (priceItems.none { it is PriceItem.TomanItem }) {
                priceItems = priceItems + PriceItem.TomanItem
            }

            if (priceItems.isNotEmpty()) {
                updateSpinners()
                android.widget.Toast.makeText(
                    requireContext(),
                    "داده‌های آفلاین لود شد",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            } else {
                android.widget.Toast.makeText(
                    requireContext(),
                    "هیچ داده آفلاینی موجود نیست",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun updateSpinners() {
        val currencyNames = priceItems.map { it.name ?: it.nameEn ?: it.symbol ?: "Unknown" }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            currencyNames
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.fromCurrencySpinner.adapter = adapter
        binding.toCurrencySpinner.adapter = adapter
    }

    private fun setupConversionListeners() {
        binding.fromAmount.addTextChangedListener { text ->
            convertCurrency()
        }

        binding.fromCurrencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                convertCurrency()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        binding.toCurrencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                convertCurrency()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun swapCurrencies() {
        val fromIndex = binding.fromCurrencySpinner.selectedItemPosition
        val toIndex = binding.toCurrencySpinner.selectedItemPosition
        binding.fromCurrencySpinner.setSelection(toIndex)
        binding.toCurrencySpinner.setSelection(fromIndex)
        convertCurrency()
    }

    private fun convertCurrency() {
        val fromAmountText = binding.fromAmount.text.toString()
        if (fromAmountText.isBlank()) {
            binding.toAmount.setText("")
            return
        }

        val fromAmount = fromAmountText.toDoubleOrNull() ?: return
        val fromCurrencyName = binding.fromCurrencySpinner.selectedItem.toString()
        val toCurrencyName = binding.toCurrencySpinner.selectedItem.toString()

        val fromPriceItem = priceItems.find {
            (it.name ?: it.nameEn ?: it.symbol) == fromCurrencyName
        }
        val toPriceItem = priceItems.find {
            (it.name ?: it.nameEn ?: it.symbol) == toCurrencyName
        }

        if (fromPriceItem != null && toPriceItem != null) {
            val fromPrice = fromPriceItem.price?.toDoubleOrNull() ?: 1.0
            val toPrice = toPriceItem.price?.toDoubleOrNull() ?: 1.0
            val convertedAmount = (fromAmount * fromPrice) / toPrice
            binding.toAmount.setText(String.format("%.2f", convertedAmount))
        } else {
            binding.toAmount.setText("")
        }
    }
}