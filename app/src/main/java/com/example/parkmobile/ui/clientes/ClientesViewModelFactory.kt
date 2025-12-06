package com.example.parkmobile.ui.clientes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.parkmobile.data.repository.ClienteRepository

@Suppress("UNCHECKED_CAST")
class ClientesViewModelFactory(private val clienteRepository: ClienteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClientesViewModel::class.java)) {
            return ClientesViewModel(clienteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
