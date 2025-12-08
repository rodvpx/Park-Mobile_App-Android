package com.example.parkmobile.ui.clientes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkmobile.data.model.Cliente
import com.example.parkmobile.data.repository.ClienteRepository
import kotlinx.coroutines.launch

class ClientesViewModel(private val repository: ClienteRepository) : ViewModel() {

    private val _clientes = MutableLiveData<List<Cliente>>()
    val clientes: LiveData<List<Cliente>> = _clientes

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private var listaOriginalDeClientes: List<Cliente> = emptyList()

    fun carregarClientes() {
        viewModelScope.launch {
            try {
                val clientesList = repository.getAllClientes()
                listaOriginalDeClientes = clientesList
                _clientes.value = clientesList
            } catch (e: Exception) {
                _errorMessage.value = "Falha ao carregar clientes."
            }
        }
    }

    fun filtrarClientes(query: String?) {
        val listaFiltrada = if (query.isNullOrBlank()) {
            listaOriginalDeClientes
        } else {
            listaOriginalDeClientes.filter {
                it.nome.contains(query, ignoreCase = true) || it.cpf.contains(query)
            }
        }
        _clientes.value = listaFiltrada
    }

    fun addCliente(nome: String, cpf: String, idUsuario: String) {
        val novoCliente = Cliente(nome = nome, cpf = cpf, idUsuario = idUsuario)
        viewModelScope.launch {
            try {
                repository.createCliente(novoCliente)
                carregarClientes() // Recarrega a lista para mostrar o novo cliente
            } catch (e: Exception) {
                _errorMessage.value = "Falha ao adicionar cliente."
            }
        }
    }

    fun updateCliente(cliente: Cliente) {
        viewModelScope.launch {
            try {
                repository.updateCliente(cliente)
                carregarClientes() // Recarrega a lista para mostrar a atualização
            } catch (e: Exception) {
                _errorMessage.value = "Falha ao atualizar cliente."
            }
        }
    }
}
