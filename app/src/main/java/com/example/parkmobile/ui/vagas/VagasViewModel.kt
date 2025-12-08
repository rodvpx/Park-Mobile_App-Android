package com.example.parkmobile.ui.vagas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkmobile.data.model.Vaga
import com.example.parkmobile.data.repository.VagaRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class VagasViewModel(private val vagaRepository: VagaRepository) : ViewModel() {

    private val _vagas = MutableLiveData<List<Vaga>>()
    val vagas: LiveData<List<Vaga>> = _vagas

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _dismiss = MutableLiveData<Boolean>()
    val dismiss: LiveData<Boolean> = _dismiss

    fun carregarVagas() {
        viewModelScope.launch {
            vagaRepository.getAllVagas().collect {
                _vagas.value = it
            }
        }
    }

    fun addVaga(codigo: String, status: String) {
        if (codigo.isBlank()) {
            _errorMessage.value = "O código da vaga não pode estar em branco."
            return
        }

        viewModelScope.launch {
            try {
                val novaVaga = Vaga(codigo = codigo, status = status)
                vagaRepository.addVaga(novaVaga)
                _dismiss.value = true // Sinaliza para o BottomSheet fechar
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun updateVaga(vaga: Vaga, novoCodigo: String, novoStatus: String) {
        if (novoCodigo.isBlank()) {
            _errorMessage.value = "O código da vaga não pode estar em branco."
            return
        }

        viewModelScope.launch {
            try {
                val vagaAtualizada = vaga.copy(codigo = novoCodigo, status = novoStatus)
                vagaRepository.updateVaga(vagaAtualizada)
                _dismiss.value = true // Sinaliza para o BottomSheet fechar
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun onDismissed() {
        _dismiss.value = false
    }
}
