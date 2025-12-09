package com.example.parkmobile.ui.historico

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.parkmobile.data.model.HistoricoEstacionamento
import com.example.parkmobile.data.repository.RelatorioRepository
import kotlinx.coroutines.launch

sealed class ResultadoHistoricoUiState {
    object Loading : ResultadoHistoricoUiState()
    data class Success(val historico: List<HistoricoEstacionamento>) : ResultadoHistoricoUiState()
    data class Error(val message: String) : ResultadoHistoricoUiState()
    data class Empty(val message: String = "Nenhum histórico para este cliente.") : ResultadoHistoricoUiState()
}

class ResultadoHistoricoViewModel(private val clienteId: String, private val repository: RelatorioRepository) : ViewModel() {

    private val _uiState = MutableLiveData<ResultadoHistoricoUiState>()
    val uiState: LiveData<ResultadoHistoricoUiState> = _uiState

    fun carregarHistorico() {
        viewModelScope.launch {
            _uiState.value = ResultadoHistoricoUiState.Loading
            repository.getHistoricoPorCliente(clienteId)
                .onSuccess {
                    if (it.isEmpty()) {
                        _uiState.postValue(ResultadoHistoricoUiState.Empty())
                    } else {
                        _uiState.postValue(ResultadoHistoricoUiState.Success(it))
                    }
                }
                .onFailure { 
                    _uiState.postValue(ResultadoHistoricoUiState.Error("Erro ao carregar histórico: ${it.message}"))
                }
        }
    }
}

class ResultadoHistoricoViewModelFactory(private val clienteId: String, private val repository: RelatorioRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultadoHistoricoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ResultadoHistoricoViewModel(clienteId, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
