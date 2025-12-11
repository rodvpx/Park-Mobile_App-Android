package com.example.parkmobile.data.repository

import com.example.parkmobile.data.model.HistoricoEstacionamento
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class RelatorioRepository {

    private val db = FirebaseFirestore.getInstance()
    private val historicoCollection = db.collection("historico_estacionamento")

    suspend fun getTodosOsRecibos(): Result<List<HistoricoEstacionamento>> {
        return try {
            val snapshot = historicoCollection
                .whereNotEqualTo("checkOut", null)
                .orderBy("checkOut", Query.Direction.DESCENDING)
                .get()
                .await()
            Result.success(snapshot.toObjects(HistoricoEstacionamento::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getHistoricoPorCliente(clienteId: String): Result<List<HistoricoEstacionamento>> {
        return try {
            val snapshot = historicoCollection
                .whereEqualTo("idCliente", clienteId)
                .orderBy("checkIn", Query.Direction.DESCENDING)
                .get()
                .await()
            Result.success(snapshot.toObjects(HistoricoEstacionamento::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
