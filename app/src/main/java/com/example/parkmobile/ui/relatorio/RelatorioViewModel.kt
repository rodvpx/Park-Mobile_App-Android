package com.example.parkmobile.ui.relatorio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkmobile.data.model.Cliente
import com.example.parkmobile.data.model.HistoricoEstacionamento
import com.example.parkmobile.data.repository.ClienteRepository
import com.example.parkmobile.data.repository.HistoricoEstacionamentoRepository
import kotlinx.coroutines.launch

class RelatorioViewModel(
    private val historicoEstacionamentoRepository: HistoricoEstacionamentoRepository,
    private val clienteRepository: ClienteRepository
) : ViewModel() {

    private val _todosOsRecibos = MutableLiveData<List<HistoricoEstacionamento>>()
    val todosOsRecibos: LiveData<List<HistoricoEstacionamento>> = _todosOsRecibos

    private val _historicoCliente = MutableLiveData<List<HistoricoEstacionamento>>()
    val historicoCliente: LiveData<List<HistoricoEstacionamento>> = _historicoCliente

    private val _cliente = MutableLiveData<Cliente?>()
    val cliente: LiveData<Cliente?> = _cliente

    private val _uiState = MutableLiveData<RelatorioUiState>()
    val uiState: LiveData<RelatorioUiState> = _uiState

    fun carregarTodosOsRecibos() {
        _uiState.value = RelatorioUiState.Loading
        viewModelScope.launch {
            try {
                val resultado = historicoEstacionamentoRepository.getHistoricoCompleto()
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

    fun buscarHistoricoPorClienteId(clienteId: String) {
        if (clienteId.isBlank()) {
            _uiState.value = RelatorioUiState.Error("ID do cliente é obrigatório.")
            return
        }

        _uiState.value = RelatorioUiState.Loading
        viewModelScope.launch {
            try {
                val resultado = historicoEstacionamentoRepository.getHistoricoPorClienteId(clienteId)
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

    fun buscarClientePorId(clienteId: String) {
        if (clienteId.isBlank()) return

        viewModelScope.launch {
            try {
                _cliente.value = clienteRepository.getCliente(clienteId)
            } catch (e: Exception) {
                // Tratar o erro, se necessário
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
