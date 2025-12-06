package com.example.parkmobile.ui.historico

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkmobile.data.model.ClienteVaga
import com.example.parkmobile.data.repository.ClienteVagaRepository
import kotlinx.coroutines.launch

class HistoricoViewModel(private val clienteVagaRepository: ClienteVagaRepository) : ViewModel() {

    private val _historico = MutableLiveData<List<ClienteVaga>>()
    val historico: LiveData<List<ClienteVaga>> = _historico

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun carregarHistoricoCompleto() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _historico.value = clienteVagaRepository.getHistoricoCompleto()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
