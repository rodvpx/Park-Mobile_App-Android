package com.example.parkmobile.data.repository

import com.example.parkmobile.data.model.HistoricoEstacionamento
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class HistoricoEstacionamentoRepository(private val firestore: FirebaseFirestore) {

    private val historicoCollection = firestore.collection("historico_estacionamento")

    suspend fun getHistoricoCompleto(): List<HistoricoEstacionamento> {
        return try {
            historicoCollection.whereNotEqualTo("checkOut", null)
                .orderBy("checkOut", Query.Direction.DESCENDING)
                .get().await().toObjects(HistoricoEstacionamento::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getHistoricoPorClienteId(clienteId: String): List<HistoricoEstacionamento> {
        return try {
            historicoCollection.whereEqualTo("idCliente", clienteId)
                .orderBy("checkIn", Query.Direction.DESCENDING)
                .get().await().toObjects(HistoricoEstacionamento::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getHistoricoPorUsuarioId(usuarioId: String): List<HistoricoEstacionamento> {
        return try {
            historicoCollection.whereEqualTo("idUsuario", usuarioId)
                .orderBy("checkIn", Query.Direction.DESCENDING)
                .get().await().toObjects(HistoricoEstacionamento::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }
}