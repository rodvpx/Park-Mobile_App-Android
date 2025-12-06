package com.example.parkmobile.ui.relatorio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkmobile.data.model.ClienteVaga
import com.example.parkmobile.data.repository.ClienteRepository
import com.example.parkmobile.data.repository.ClienteVagaRepository
import kotlinx.coroutines.launch

class RelatorioViewModel(
    private val clienteVagaRepository: ClienteVagaRepository,
    private val clienteRepository: ClienteRepository
) : ViewModel() {

    private val _todosOsRecibos = MutableLiveData<List<ClienteVaga>>()
    val todosOsRecibos: LiveData<List<ClienteVaga>> = _todosOsRecibos

    private val _historicoCliente = MutableLiveData<List<ClienteVaga>>()
    val historicoCliente: LiveData<List<ClienteVaga>> = _historicoCliente

    private val _uiState = MutableLiveData<RelatorioUiState>()
    val uiState: LiveData<RelatorioUiState> = _uiState

    fun carregarTodosOsRecibos() {
        _uiState.value = RelatorioUiState.Loading
        viewModelScope.launch {
            try {
                // CORREÇÃO 1: Usando o nome correto do método do repositório
                val resultado = clienteVagaRepository.getHistoricoCompleto()
                _todosOsRecibos.value = resultado
                if (resultado.isEmpty()) {
                    _uiState.value = RelatorioUiState.Empty("Nenhum recibo encontrado.")
                } else {
                    _uiState.value = RelatorioUiState.Success("Recibos carregados.")
                }
            } catch (e: Exception) {
                _uiState.value = RelatorioUiState.Error("Falha ao carregar recibos: ${e.message}")
            }
        }
    }

    // CORREÇÃO 2: Nova função para buscar pelo ID, que é o correto no fluxo
    fun buscarHistoricoPorClienteId(clienteId: String) {
        if (clienteId.isBlank()) {
            _uiState.value = RelatorioUiState.Error("ID do cliente é obrigatório.")
            return
        }

        _uiState.value = RelatorioUiState.Loading
        viewModelScope.launch {
            try {
                val resultado = clienteVagaRepository.getHistoricoPorClienteId(clienteId)
                _historicoCliente.value = resultado
                if (resultado.isEmpty()) {
                    _uiState.value = RelatorioUiState.Empty("Nenhum histórico para este cliente.")
                } else {
                    _uiState.value = RelatorioUiState.Success("Histórico carregado.")
                }
            } catch (e: Exception) {
                _uiState.value = RelatorioUiState.Error("Falha ao buscar histórico: ${e.message}")
            }
        }
    }
}

sealed class RelatorioUiState {
    object Loading : RelatorioUiState()
    data class Success(val message: String) : RelatorioUiState()
    data class Empty(val message: String) : RelatorioUiState()
    data class Error(val message: String) : RelatorioUiState()
}
