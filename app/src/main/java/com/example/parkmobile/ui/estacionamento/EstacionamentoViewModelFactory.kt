package com.example.parkmobile.ui.estacionamento

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.parkmobile.data.repository.ClienteRepository
import com.example.parkmobile.data.repository.ClienteVagaRepository
import com.example.parkmobile.data.repository.EstacionamentoRepository
import com.example.parkmobile.data.repository.VagaRepository
import com.google.firebase.firestore.FirebaseFirestore

@Suppress("UNCHECKED_CAST")
class EstacionamentoViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EstacionamentoViewModel::class.java)) {
            // Construir manualmente a cadeia de dependências
            val firestore = FirebaseFirestore.getInstance()
            val clienteRepository = ClienteRepository(firestore)
            val vagaRepository = VagaRepository(firestore)
            val clienteVagaRepository = ClienteVagaRepository(firestore)
            val estacionamentoRepository = EstacionamentoRepository(
                firestore,
                clienteVagaRepository,
                clienteRepository,
                vagaRepository
            )
            return EstacionamentoViewModel(estacionamentoRepository, clienteVagaRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
