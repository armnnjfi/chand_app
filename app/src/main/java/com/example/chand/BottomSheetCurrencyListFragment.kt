package com.example.chand

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.chand.ViewModel.WatchlistRepository
import com.example.chand.ViewModel.WatchlistViewModel
import com.example.chand.ViewModel.WatchlistViewModelFactory
import com.example.chand.DataBase.toEntity

class BottomSheetCurrencyListFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddCurrencyListBinding
    //api
    private val api by lazy { ApiClient().getClient().create<ApiServices>(ApiServices::class.java) }

    //viewModel
    private val viewModel: WatchlistViewModel by activityViewModels {
        WatchlistViewModelFactory(
            WatchlistRepository(ChandDatabase.getDatabase(requireContext()).dao())
        )
    }
    //adapter
    private val addCurrencyAdapter by lazy {
        AddCurrencyListAdapter { priceItem ->
            val entity = priceItem.toEntity()
            viewModel.addToWatchlist(entity)
            dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddCurrencyListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.apply {
            val callApi = api.getCurrencyPrice("FreejCpsMTnCQC5VM5mod6U35aNqCq5c")
            callApi.enqueue(object : Callback<Response_Currency_Price> {
                override fun onResponse(
                    call: Call<Response_Currency_Price?>,
                    response: Response<Response_Currency_Price?>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { itBody ->


                            val priceItems = mutableListOf<PriceItem>()

                            // Currency
                            itBody.currency?.filterNotNull()?.forEach { currency ->
                                priceItems.add(PriceItem.CurrencyItem(currency))
                            }

                            // Gold
                            itBody.gold?.filterNotNull()?.forEach { gold->
                                priceItems.add(
                                    PriceItem.GoldItem(gold)
                                )
                            }

                            // Crypto
                            itBody.cryptocurrency?.filterNotNull()?.forEach {crypto ->
                                priceItems.add(PriceItem.CryptocurrencyItem(crypto))
                            }

                            addCurrencyAdapter.differ.submitList(priceItems)

                            addCurrencyList.adapter = addCurrencyAdapter
                            addCurrencyList.layoutManager = LinearLayoutManager(requireContext())
                        }
                    }
                }
                override fun onFailure(
                    call: Call<Response_Currency_Price?>,
                    response: Throwable
                ) {
                    Log.e("onFailure","Err : ${response.message}")
                }
            })
        }

    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val bottomSheet = it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.layoutParams?.height = (resources.displayMetrics.heightPixels * 0.5).toInt()
        }
    }
}