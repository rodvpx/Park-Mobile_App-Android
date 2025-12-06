package com.example.parkmobile.ui.estacionamento

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkmobile.data.model.ClienteVaga
import com.example.parkmobile.data.repository.ClienteVagaRepository
import com.example.parkmobile.data.repository.EstacionamentoRepository
import kotlinx.coroutines.launch

class EstacionamentoViewModel(
    private val estacionamentoRepository: EstacionamentoRepository,
    private val clienteVagaRepository: ClienteVagaRepository
) : ViewModel() {

    // LiveData para o estado da UI
    private val _uiState = MutableLiveData<EstacionamentoUiState>()
    val uiState: LiveData<EstacionamentoUiState> = _uiState

    // LiveData para a lista de veículos estacionados
    private val _veiculosEstacionados = MutableLiveData<List<ClienteVaga>>()
    val veiculosEstacionados: LiveData<List<ClienteVaga>> = _veiculosEstacionados

    fun carregarVeiculosEstacionados() {
        viewModelScope.launch {
            try {
                _veiculosEstacionados.value = clienteVagaRepository.getVeiculosEstacionados()
            } catch (e: Exception) {
                _uiState.value = EstacionamentoUiState.Error("Erro ao carregar veículos: ${e.message}")
            }
        }
    }

    fun realizarCheckIn(cpf: String, placa: String, marca: String, modelo: String, cor: String) {
        if (cpf.isBlank() || placa.isBlank()) {
            _uiState.value = EstacionamentoUiState.Error("CPF e Placa são obrigatórios.")
            return
        }

        _uiState.value = EstacionamentoUiState.Loading

        viewModelScope.launch {
            val result = estacionamentoRepository.checkIn(cpf, placa, marca, modelo, cor)
            result.onSuccess {
                _uiState.value = EstacionamentoUiState.Success("Check-in realizado com sucesso! Recibo: ${it.recibo}", it)
                carregarVeiculosEstacionados() // Atualiza a lista após check-in
            }.onFailure {
                _uiState.value = EstacionamentoUiState.Error("Falha no Check-in: ${it.message}")
            }
        }
    }

    fun realizarCheckOut(recibo: String) {
        if (recibo.isBlank()) {
            _uiState.value = EstacionamentoUiState.Error("O número do recibo é obrigatório.")
            return
        }

        _uiState.value = EstacionamentoUiState.Loading

        viewModelScope.launch {
            val result = estacionamentoRepository.checkOut(recibo)
            result.onSuccess {
                _uiState.value = EstacionamentoUiState.Success("Check-out realizado com sucesso! Valor: R$${it.valor}", it)
                carregarVeiculosEstacionados() // Atualiza a lista após check-out
            }.onFailure {
                _uiState.value = EstacionamentoUiState.Error("Falha no Check-out: ${it.message}")
            }
        }
    }
}

// Classe para representar os diferentes estados da UI
sealed class EstacionamentoUiState {
    object Loading : EstacionamentoUiState()
    data class Success(val message: String, val clienteVaga: ClienteVaga) : EstacionamentoUiState()
    data class Error(val message: String) : EstacionamentoUiState()
}
