package com.example.parkmobile.data.repository

import com.example.parkmobile.data.model.Cliente
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class ClienteRepository(private val firestore: FirebaseFirestore) {

    private val clienteCollection = firestore.collection("clientes")

    suspend fun getAllClientes(): List<Cliente> {
        return try {
            clienteCollection.orderBy("nome", Query.Direction.ASCENDING).get().await().toObjects(Cliente::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getCliente(id: String): Cliente? {
        return try {
            clienteCollection.document(id).get().await().toObject(Cliente::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getClienteByCpf(cpf: String): Cliente? {
        return try {
            val query = clienteCollection.whereEqualTo("cpf", cpf).limit(1).get().await()
            if (query.isEmpty) {
                null
            } else {
                query.documents.first().toObject(Cliente::class.java)
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun createCliente(cliente: Cliente) {
        clienteCollection.document(cliente.id).set(cliente).await()
    }
}
