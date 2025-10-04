package com.example.chand

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chand.adapters.AddCurrencyListAdapter
import com.example.chand.databinding.FragmentAddCurrencyListBinding
import com.example.chand.model.PriceItem
import com.example.chand.model.Response_Currency_Price
import com.example.chand.server.ApiClient
import com.example.chand.server.ApiServices
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.fragment.app.activityViewModels
import com.example.chand.DataBase.ChandDatabase
import com.example.chand.ViewModel.watchlist.WatchlistRepository
import com.example.chand.ViewModel.watchlist.WatchlistViewModel
import com.example.chand.ViewModel.watchlist.WatchlistViewModelFactory
import com.example.chand.DataBase.toEntity
import com.example.retrofit_exersice.utils.Constants

class BottomSheetCurrencyListFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddCurrencyListBinding

    // API
    private val api by lazy { ApiClient().getClient().create<ApiServices>(ApiServices::class.java) }

    // ViewModel
    private val viewModel: WatchlistViewModel by activityViewModels {
        WatchlistViewModelFactory(
            WatchlistRepository(ChandDatabase.getDatabase(requireContext()).dao())
        )
    }

    // Adapter
    private val addCurrencyAdapter by lazy {
        AddCurrencyListAdapter { priceItem ->
            val entity = priceItem.toEntity()
            viewModel.addToWatchlist(entity)
            dismiss()
        }
    }

    private var fullList: List<PriceItem> = emptyList() // برای سرچ لحظه‌ای

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddCurrencyListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            addCurrencyList.adapter = addCurrencyAdapter
            addCurrencyList.layoutManager = LinearLayoutManager(requireContext())

            // فراخوانی API
            api.getCurrencyPrice(Constants.API_KEY).enqueue(object : Callback<Response_Currency_Price> {
                override fun onResponse(
                    call: Call<Response_Currency_Price>,
                    response: Response<Response_Currency_Price>
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

                            fullList = priceItems.toList() // ذخیره لیست کامل برای سرچ
                            addCurrencyAdapter.differ.submitList(fullList)
                        }
                    }
                }

                override fun onFailure(call: Call<Response_Currency_Price>, t: Throwable) {
                    Log.e("onFailure", "Err : ${t.message}")
                }
            })

            // سرچ لحظه‌ای
            searchEdt.addTextChangedListener { text ->
                val query = text?.trim()?.toString() ?: ""
                val filtered = if (query.isEmpty()) {
                    fullList
                } else {
                    fullList.filter { item ->
                        when (item) {
                            is PriceItem.CurrencyItem -> item.currency.name?.contains(query, true) == true
                            is PriceItem.GoldItem -> item.gold.name?.contains(query, true) == true
                            is PriceItem.CryptocurrencyItem -> item.cryptocurrency.name?.contains(query, true) == true
                            else -> false
                        }
                    }
                }
                addCurrencyAdapter.differ.submitList(filtered)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)?.let { bottomSheet ->
            bottomSheet.layoutParams.height = (resources.displayMetrics.heightPixels * 0.5).toInt()
        }
    }
}
