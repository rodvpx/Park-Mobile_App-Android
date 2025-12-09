package com.example.parkmobile.ui.relatorio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.parkmobile.data.repository.ClienteRepository
import com.example.parkmobile.data.repository.HistoricoEstacionamentoRepository
import com.google.firebase.firestore.FirebaseFirestore

@Suppress("UNCHECKED_CAST")
class RelatorioViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RelatorioViewModel::class.java)) {
            val firestore = FirebaseFirestore.getInstance()
            val historicoEstacionamentoRepository = HistoricoEstacionamentoRepository(firestore)
            val clienteRepository = ClienteRepository(firestore)
            return RelatorioViewModel(historicoEstacionamentoRepository, clienteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
