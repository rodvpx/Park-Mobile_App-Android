package com.example.parkmobile.data.repository

import com.example.parkmobile.data.model.ClienteVaga
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class ClienteVagaRepository(private val firestore: FirebaseFirestore) {

    // Nova função para buscar todo o histórico finalizado
    suspend fun getHistoricoCompleto(): List<ClienteVaga> {
        return try {
            clienteVagaCollection.whereNotEqualTo("dataSaida", null)
                .orderBy("dataSaida", Query.Direction.DESCENDING)
                .get().await().toObjects(ClienteVaga::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getHistoricoPorClienteId(clienteId: String): List<ClienteVaga> {
        return try {
            clienteVagaCollection.whereEqualTo("idCliente", clienteId)
                .orderBy("dataEntrada", Query.Direction.DESCENDING)
                .get().await().toObjects(ClienteVaga::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getVeiculosEstacionados(): List<ClienteVaga> {
        return try {
            clienteVagaCollection.whereEqualTo("dataSaida", null)
                .orderBy("dataEntrada", Query.Direction.ASCENDING)
                .get().await().toObjects(ClienteVaga::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getClienteVagaByRecibo(recibo: String): ClienteVaga? {
        return try {
            val query = clienteVagaCollection.whereEqualTo("recibo", recibo).limit(1).get().await()
            if (query.isEmpty) {
                null
            } else {
                query.documents.firstOrNull()?.toObject(ClienteVaga::class.java)
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun save(clienteVaga: ClienteVaga): Unit {
        clienteVagaCollection.document(clienteVaga.id).set(clienteVaga).await()
    }

    private val clienteVagaCollection = firestore.collection("cliente_vaga")
}