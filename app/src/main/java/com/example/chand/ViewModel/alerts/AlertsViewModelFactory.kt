package com.example.chand.ViewModel.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AlertsViewModelFactory(private val repository: AlertsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlertsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlertsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}