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

    suspend fun getVeiculosEstacionados(): List<HistoricoEstacionamento> {
        return try {
            historicoCollection.whereEqualTo("checkOut", null)
                .orderBy("checkIn", Query.Direction.ASCENDING)
                .get().await().toObjects(HistoricoEstacionamento::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getHistoricoByRecibo(recibo: String): HistoricoEstacionamento? {
        return try {
            val query = historicoCollection.whereEqualTo("recibo", recibo).limit(1).get().await()
            if (query.isEmpty) {
                null
            } else {
                query.documents.firstOrNull()?.toObject(HistoricoEstacionamento::class.java)
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun save(historico: HistoricoEstacionamento): Unit {
        historicoCollection.document(historico.id).set(historico).await()
    }
}