package com.example.parkmobile.data.repository

import com.example.parkmobile.data.model.ClienteVaga
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class ClienteVagaRepository(private val firestore: FirebaseFirestore) {

    private val clienteVagaCollection = firestore.collection("cliente_vaga")

    suspend fun getVeiculosEstacionados(): List<ClienteVaga> {
        return try {
            clienteVagaCollection.whereEqualTo("dataSaida", null)
                .orderBy("dataEntrada", Query.Direction.ASCENDING)
                .get().await().toObjects(ClienteVaga::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getClienteVaga(id: String): ClienteVaga? {
        return try {
            clienteVagaCollection.document(id).get().await().toObject(ClienteVaga::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getClienteVagaByRecibo(recibo: String): ClienteVaga? {
        return try {
            val query = clienteVagaCollection.whereEqualTo("recibo", recibo).limit(1).get().await()
            if (query.isEmpty) {
                null
            } else {
                query.documents.first().toObject(ClienteVaga::class.java)
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getHistoricoCliente(idCliente: String): List<ClienteVaga> {
        return try {
            clienteVagaCollection.whereEqualTo("idCliente", idCliente)
                .orderBy("dataEntrada", Query.Direction.DESCENDING)
                .get().await().toObjects(ClienteVaga::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun save(clienteVaga: ClienteVaga) {
        clienteVagaCollection.document(clienteVaga.id).set(clienteVaga).await()
    }
}
