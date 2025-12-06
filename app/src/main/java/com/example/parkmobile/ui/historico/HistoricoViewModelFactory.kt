package com.example.parkmobile.ui.historico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.parkmobile.data.repository.ClienteVagaRepository
import com.google.firebase.firestore.FirebaseFirestore

@Suppress("UNCHECKED_CAST")
class HistoricoViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoricoViewModel::class.java)) {
            val firestore = FirebaseFirestore.getInstance()
            val clienteVagaRepository = ClienteVagaRepository(firestore)
            return HistoricoViewModel(clienteVagaRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
