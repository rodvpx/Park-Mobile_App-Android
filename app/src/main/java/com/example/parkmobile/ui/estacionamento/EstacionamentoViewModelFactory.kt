package com.example.parkmobile.ui.estacionamento

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.parkmobile.data.repository.EstacionamentoRepository

class EstacionamentoViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EstacionamentoViewModel::class.java)) {
            return EstacionamentoViewModel(EstacionamentoRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
