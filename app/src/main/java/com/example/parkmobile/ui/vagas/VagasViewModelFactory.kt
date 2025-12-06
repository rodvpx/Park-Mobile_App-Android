package com.example.parkmobile.ui.vagas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.parkmobile.data.repository.VagaRepository

@Suppress("UNCHECKED_CAST")
class VagasViewModelFactory(private val vagaRepository: VagaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VagasViewModel::class.java)) {
            return VagasViewModel(vagaRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
