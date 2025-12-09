package com.example.parkmobile.ui.historico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.parkmobile.data.repository.RelatorioRepository
import com.google.firebase.firestore.FirebaseFirestore

class HistoricoViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoricoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoricoViewModel(RelatorioRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
