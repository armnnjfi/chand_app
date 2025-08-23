package com.example.chand

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chand.DataBase.AlertEntity
import com.example.chand.DataBase.ChandDatabase
import com.example.chand.databinding.FragmentAlertsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlertsFragment : Fragment() {

    private lateinit var binding: FragmentAlertsBinding
    private lateinit var adapter: AlertsAdapter

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
    }

    private fun setupRecyclerView() {
        adapter = AlertsAdapter { alert, isChecked ->
            CoroutineScope(Dispatchers.IO).launch {
                ChandDatabase.getDatabase(requireContext()).dao().updateAlertStatus(alert.id, isChecked)
            }
        }
        binding.alertsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.alertsRecyclerView.adapter = adapter
    }

    private fun loadAlerts() {
        CoroutineScope(Dispatchers.IO).launch {
            val alerts = ChandDatabase.getDatabase(requireContext()).dao().getAllAlerts().value
            withContext(Dispatchers.Main) {
                alerts?.let { adapter.submitList(it) }
            }
        }
    }
}