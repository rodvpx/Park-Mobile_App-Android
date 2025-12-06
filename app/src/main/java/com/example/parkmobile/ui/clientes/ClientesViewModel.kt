package com.example.parkmobile.ui.clientes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkmobile.data.model.Cliente
import com.example.parkmobile.data.repository.ClienteRepository
import kotlinx.coroutines.launch

class ClientesViewModel(private val clienteRepository: ClienteRepository) : ViewModel() {

    private val _clientes = MutableLiveData<List<Cliente>>()
    val clientes: LiveData<List<Cliente>> = _clientes

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun carregarClientes() {
        viewModelScope.launch {
            try {
                val clientesResult = clienteRepository.getAllClientes()
                _clientes.value = clientesResult
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }
}
