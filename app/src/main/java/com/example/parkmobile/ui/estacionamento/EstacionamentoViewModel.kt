package com.example.parkmobile.ui.estacionamento

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkmobile.data.model.Cliente
import com.example.parkmobile.data.model.HistoricoEstacionamento
import com.example.parkmobile.data.model.HistoricoEstacionamentoDetalhado
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

    // Lista original, sem filtro
    private var _listaCompletaVeiculosEstacionados = listOf<HistoricoEstacionamentoDetalhado>()

    // Lista a ser exibida na UI (pode ser filtrada)
    private val _veiculosEstacionadosDetalhado = MutableLiveData<List<HistoricoEstacionamentoDetalhado>>()
    val veiculosEstacionadosDetalhado: LiveData<List<HistoricoEstacionamentoDetalhado>> = _veiculosEstacionadosDetalhado

    private val _clientes = MutableLiveData<List<Cliente>>()
    val clientes: LiveData<List<Cliente>> = _clientes

    private val _vagasLivres = MutableLiveData<List<Vaga>>()
    val vagasLivres: LiveData<List<Vaga>> = _vagasLivres

    fun carregarVeiculosEstacionados() {
        viewModelScope.launch {
            _uiState.value = EstacionamentoUiState.Loading
            val historicoResult = repository.getVeiculosEstacionados()
            val clientesResult = repository.getClientes()
            val vagasResult = repository.getVagas()

            if (historicoResult.isSuccess && clientesResult.isSuccess && vagasResult.isSuccess) {
                val historicos = historicoResult.getOrThrow()
                val clientes = clientesResult.getOrThrow()
                val vagas = vagasResult.getOrThrow()

                val listaDetalhada = historicos.map { historico ->
                    HistoricoEstacionamentoDetalhado(
                        historico = historico,
                        cliente = clientes.find { it.id == historico.idCliente },
                        vaga = vagas.find { it.id == historico.idVaga }
                    )
                }
                _listaCompletaVeiculosEstacionados = listaDetalhada
                _veiculosEstacionadosDetalhado.postValue(listaDetalhada)
                _uiState.value = EstacionamentoUiState.Success("Veículos carregados") // Mensagem interna
            } else {
                val errorMsg = historicoResult.exceptionOrNull()?.message ?: clientesResult.exceptionOrNull()?.message ?: vagasResult.exceptionOrNull()?.message
                _uiState.value = EstacionamentoUiState.Error("Erro ao carregar veículos: $errorMsg")
            }
        }
    }

    fun filtrarVeiculos(query: String?) {
        val listaFiltrada = if (query.isNullOrBlank()) {
            _listaCompletaVeiculosEstacionados
        } else {
            val lowerCaseQuery = query.lowercase()
            _listaCompletaVeiculosEstacionados.filter { detalhado ->
                detalhado.historico.placaVeiculo.lowercase().contains(lowerCaseQuery) ||
                detalhado.historico.marcaVeiculo.lowercase().contains(lowerCaseQuery) ||
                detalhado.historico.modeloVeiculo.lowercase().contains(lowerCaseQuery) ||
                detalhado.cliente?.nome?.lowercase()?.contains(lowerCaseQuery) == true ||
                detalhado.cliente?.cpf?.lowercase()?.contains(lowerCaseQuery) == true ||
                detalhado.vaga?.codigo?.lowercase()?.contains(lowerCaseQuery) == true
            }
        }
        _veiculosEstacionadosDetalhado.postValue(listaFiltrada)
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
                    carregarVagasLivres()
                }
                .onFailure { _uiState.postValue(EstacionamentoUiState.Error("Erro no check-in: ${it.message}")) }
        }
    }

    fun calcularHoras(checkIn: Date, checkOut: Date): Int {
        val diff = checkOut.time - checkIn.time
        val horas = ceil(diff.toDouble() / (1000 * 60 * 60)).toInt()
        return if (horas < 1) 1 else horas
    }

    fun calcularValor(horas: Int): Double {
        return horas * PRECO_POR_HORA
    }

    fun realizarCheckOut(historico: HistoricoEstacionamento, descontoPorcentagem: Double) {
        viewModelScope.launch {
            _uiState.value = EstacionamentoUiState.Loading
            
            val checkOutTime = Date()
            val horasEstacionado = historico.checkIn?.let { calcularHoras(it, checkOutTime) } ?: 1
            val valorOriginal = calcularValor(horasEstacionado)

            val valorFinal = valorOriginal * (1 - (descontoPorcentagem / 100))

            val historicoAtualizado = historico.copy(
                checkOut = checkOutTime,
                valor = valorFinal,
                descontoAplicado = descontoPorcentagem
            )

            repository.realizarCheckOut(historicoAtualizado)
                .onSuccess {
                    _uiState.postValue(EstacionamentoUiState.Success("Check-out de ${historico.placaVeiculo.uppercase()} realizado!"))
                    carregarVeiculosEstacionados() // Recarrega a lista detalhada
                }
                .onFailure { _uiState.postValue(EstacionamentoUiState.Error("Erro no check-out: ${it.message}")) }
        }
    }
}
