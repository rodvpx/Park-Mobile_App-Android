package com.example.parkmobile.ui.estacionamento

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkmobile.data.model.Cliente
import com.example.parkmobile.data.model.HistoricoEstacionamento
import com.example.parkmobile.data.model.Vaga
import com.example.parkmobile.data.repository.EstacionamentoRepository
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import kotlin.math.ceil

class EstacionamentoViewModel(private val repository: EstacionamentoRepository) : ViewModel() {

    companion object {
        private const val PRECO_POR_HORA = 5.00
    }

    private val _uiState = MutableLiveData<EstacionamentoUiState>()
    val uiState: LiveData<EstacionamentoUiState> = _uiState

    private val _veiculosEstacionados = MutableLiveData<List<HistoricoEstacionamento>>()
    val veiculosEstacionados: LiveData<List<HistoricoEstacionamento>> = _veiculosEstacionados

    private val _clientes = MutableLiveData<List<Cliente>>()
    val clientes: LiveData<List<Cliente>> = _clientes

    private val _vagasLivres = MutableLiveData<List<Vaga>>()
    val vagasLivres: LiveData<List<Vaga>> = _vagasLivres

    fun carregarVeiculosEstacionados() {
        viewModelScope.launch {
            repository.getVeiculosEstacionados()
                .onSuccess { _veiculosEstacionados.postValue(it) }
                .onFailure { _uiState.postValue(EstacionamentoUiState.Error("Erro ao carregar veículos: ${it.message}")) }
        }
    }

    fun carregarClientes() {
        viewModelScope.launch {
            repository.getClientes()
                .onSuccess { _clientes.postValue(it) }
                .onFailure { _uiState.postValue(EstacionamentoUiState.Error("Erro ao carregar clientes: ${it.message}")) }
        }
    }

    fun carregarVagasLivres() {
        viewModelScope.launch {
            repository.getVagasLivres()
                .onSuccess { _vagasLivres.postValue(it) }
                .onFailure { _uiState.postValue(EstacionamentoUiState.Error("Erro ao carregar vagas: ${it.message}")) }
        }
    }

    fun realizarCheckIn(idCliente: String, idVaga: String, placa: String, marca: String, modelo: String, cor: String) {
        if (idCliente.isBlank() || idVaga.isBlank() || placa.isBlank() || marca.isBlank() || modelo.isBlank() || cor.isBlank()) {
            _uiState.value = EstacionamentoUiState.Error("Todos os campos são obrigatórios")
            return
        }

        viewModelScope.launch {
            _uiState.value = EstacionamentoUiState.Loading
            val novoCheckin = HistoricoEstacionamento(
                id = UUID.randomUUID().toString(),
                recibo = "REC-${System.currentTimeMillis()}",
                placaVeiculo = placa,
                marcaVeiculo = marca,
                modeloVeiculo = modelo,
                corVeiculo = cor,
                checkIn = Date(),
                idCliente = idCliente,
                idVaga = idVaga
            )

            repository.realizarCheckIn(novoCheckin)
                .onSuccess {
                    _uiState.postValue(EstacionamentoUiState.Success("Check-in de ${placa.uppercase()} realizado!"))
                    carregarVeiculosEstacionados()
                    carregarVagasLivres()
                }
                .onFailure { _uiState.postValue(EstacionamentoUiState.Error("Erro no check-in: ${it.message}")) }
        }
    }

    fun calcularValor(checkIn: Date, checkOut: Date): Double {
        val diff = checkOut.time - checkIn.time
        val horas = ceil(diff.toDouble() / (1000 * 60 * 60)).toInt()
        val horasCobradas = if (horas < 1) 1 else horas
        return horasCobradas * PRECO_POR_HORA
    }

    fun realizarCheckOut(historico: HistoricoEstacionamento) {
        viewModelScope.launch {
            _uiState.value = EstacionamentoUiState.Loading
            
            val checkOutTime = Date()
            val valorCalculado = historico.checkIn?.let { calcularValor(it, checkOutTime) } ?: PRECO_POR_HORA

            val historicoAtualizado = historico.copy(checkOut = checkOutTime, valor = valorCalculado)

            repository.realizarCheckOut(historicoAtualizado)
                .onSuccess {
                    _uiState.postValue(EstacionamentoUiState.Success("Check-out de ${historico.placaVeiculo.uppercase()} realizado!"))
                    carregarVeiculosEstacionados()
                    carregarVagasLivres()
                }
                .onFailure { _uiState.postValue(EstacionamentoUiState.Error("Erro no check-out: ${it.message}")) }
        }
    }
}
