package com.example.parkmobile.ui.historico

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.parkmobile.data.model.HistoricoEstacionamento
import com.example.parkmobile.data.repository.RelatorioRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class HistoricoViewModel(private val repository: RelatorioRepository) : ViewModel() {

    private var listaCompletaHistorico = listOf<HistoricoEstacionamento>()

    private val _historico = MutableLiveData<List<HistoricoEstacionamento>>()
    val historico: LiveData<List<HistoricoEstacionamento>> = _historico

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // Buscar histórico do usuário logado por idUsuario
    fun carregarHistoricoDoUsuarioLogado() {
        _isLoading.postValue(true)

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            _errorMessage.postValue("Usuário não logado, impossível buscar histórico.")
            _isLoading.postValue(false)
            _historico.postValue(emptyList())
            return
        }

        FirebaseFirestore.getInstance().collection("historico_estacionamento")
            .whereEqualTo("idUsuario", userId) // agora sempre por idUsuario
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.isEmpty) {
                    _historico.postValue(emptyList())
                } else {
                    try {
                        val recibos = snapshot.toObjects(HistoricoEstacionamento::class.java)
                        _historico.postValue(recibos)
                    } catch (e: Exception) {
                        _errorMessage.postValue("Erro ao processar dados: ${e.message}")
                    }
                }
                _isLoading.postValue(false)
            }
            .addOnFailureListener { e ->
                _errorMessage.postValue("Falha no acesso ao banco: ${e.message}")
                _isLoading.postValue(false)
            }
    }

    // Função para o Admin
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
}
