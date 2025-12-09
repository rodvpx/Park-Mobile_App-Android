package com.example.parkmobile.ui.historico

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.parkmobile.data.model.HistoricoEstacionamento
import com.example.parkmobile.data.repository.RelatorioRepository
import kotlinx.coroutines.launch

class HistoricoViewModel(private val repository: RelatorioRepository) : ViewModel() {

    private var listaCompletaHistorico = listOf<HistoricoEstacionamento>()

    private val _historico = MutableLiveData<List<HistoricoEstacionamento>>()
    val historico: LiveData<List<HistoricoEstacionamento>> = _historico

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun carregarHistoricoCompleto() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getTodosOsRecibos()
                .onSuccess {
                    listaCompletaHistorico = it
                    _historico.postValue(it)
                }
                .onFailure {
                    _errorMessage.postValue("Erro ao carregar histórico: ${it.message}")
                }
            _isLoading.value = false
        }
    }

    fun filtrarHistorico(query: String?) {
        val listaFiltrada = if (query.isNullOrBlank()) {
            listaCompletaHistorico
        } else {
            val lowerCaseQuery = query.lowercase().trim()
            listaCompletaHistorico.filter {
                it.recibo.lowercase().contains(lowerCaseQuery) || it.placaVeiculo.lowercase().contains(lowerCaseQuery)
            }
        }
        _historico.postValue(listaFiltrada)
    }
}
