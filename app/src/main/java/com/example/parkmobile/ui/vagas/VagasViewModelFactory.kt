package com.example.parkmobile.ui.vagas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.parkmobile.data.repository.VagaRepository
import com.google.firebase.firestore.FirebaseFirestore

class VagasViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VagasViewModel::class.java)) {
            val repository = VagaRepository(FirebaseFirestore.getInstance())
            @Suppress("UNCHECKED_CAST")
            return VagasViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
