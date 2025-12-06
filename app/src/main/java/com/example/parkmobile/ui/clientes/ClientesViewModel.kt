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

    private val _dismiss = MutableLiveData<Boolean>()
    val dismiss: LiveData<Boolean> = _dismiss

    fun carregarClientes() {
        viewModelScope.launch {
            try {
                _clientes.value = clienteRepository.getAllClientes()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun addCliente(nome: String, cpf: String) {
        if (nome.isBlank() || cpf.isBlank()) {
            _errorMessage.value = "Nome e CPF são obrigatórios."
            return
        }
        viewModelScope.launch {
            try {
                // Nota: Um cliente adicionado por aqui não terá um usuário de login associado.
                // Isso é adequado para clientes adicionados manualmente por um admin.
                val novoCliente = Cliente(nome = nome, cpf = cpf)
                clienteRepository.createCliente(novoCliente)
                carregarClientes()
                _dismiss.value = true
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao adicionar cliente: ${e.message}"
            }
        }
    }

    fun updateCliente(cliente: Cliente, novoNome: String, novoCpf: String) {
        if (novoNome.isBlank() || novoCpf.isBlank()) {
            _errorMessage.value = "Nome e CPF são obrigatórios."
            return
        }
        viewModelScope.launch {
            try {
                val clienteAtualizado = cliente.copy(nome = novoNome, cpf = novoCpf)
                clienteRepository.updateCliente(clienteAtualizado)
                carregarClientes()
                _dismiss.value = true
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao atualizar cliente: ${e.message}"
            }
        }
    }

    fun deleteCliente(cliente: Cliente) {
        viewModelScope.launch {
            try {
                clienteRepository.deleteCliente(cliente.id)
                carregarClientes()
            } catch (e: Exception) {
                _errorMessage.value = "Erro ao deletar cliente: ${e.message}"
            }
        }
    }

    fun onDismissed() {
        _dismiss.value = false
    }
}
