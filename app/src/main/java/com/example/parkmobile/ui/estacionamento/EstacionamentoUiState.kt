package com.example.parkmobile.ui.estacionamento

sealed class EstacionamentoUiState {
    object Loading : EstacionamentoUiState()
    data class Success(val message: String) : EstacionamentoUiState()
    data class Error(val message: String) : EstacionamentoUiState()
}
