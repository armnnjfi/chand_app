package com.example.chand

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chand.DataBase.ChandDatabase
import com.example.chand.ViewModel.alerts.AlertsRepository
import com.example.chand.ViewModel.alerts.AlertsViewModel
import com.example.chand.ViewModel.alerts.AlertsViewModelFactory
import com.example.chand.databinding.FragmentAlertsBinding

class AlertsFragment : Fragment() {

    private lateinit var binding: FragmentAlertsBinding

    // ViewModel
    private val viewModel: AlertsViewModel by activityViewModels {
        AlertsViewModelFactory(
            AlertsRepository(ChandDatabase.getDatabase(requireContext()).dao())
        )
    }

    private val adapter by lazy {
        AlertsAdapter { item ->
            viewModel.removeFromAlertList(item.id)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlertsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadAlerts()
        binding.addAlertButton.setOnClickListener {
            findNavController().navigate(R.id.action_alertsFragment_to_addAlertFragment)
        }
        binding.arrowBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        binding.alertsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.alertsRecyclerView.adapter = adapter
    }

    private fun loadAlerts() {
        ChandDatabase.getDatabase(requireContext())
            .dao()
            .getAllAlertsLiveData()
            .observe(viewLifecycleOwner) { alerts ->
                adapter.differ.submitList(alerts)
            }
    }
}