package com.example.chand

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.chand.DataBase.AlertEntity
import com.example.chand.DataBase.ChandDatabase
import com.example.chand.DataBase.toPriceItem
import com.example.chand.ViewModel.alerts.AlertsRepository
import com.example.chand.ViewModel.alerts.AlertsViewModel
import com.example.chand.ViewModel.alerts.AlertsViewModelFactory
import com.example.chand.databinding.FragmentAddAlertBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddAlertFragment : Fragment() {

    private var _binding: FragmentAddAlertBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AlertsViewModel by activityViewModels {
        AlertsViewModelFactory(
            AlertsRepository(ChandDatabase.getDatabase(requireActivity().application).dao())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddAlertBinding.inflate(inflater, container, false)
        val view = binding.root

        // data Loading observe_viewModel
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

        // set save button
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
                    viewModel.addToAlertList(alert)
                }
                requireActivity().onBackPressed()
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}