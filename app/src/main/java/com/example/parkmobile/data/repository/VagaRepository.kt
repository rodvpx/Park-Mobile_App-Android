package com.example.parkmobile.data.repository

import com.example.parkmobile.data.model.Vaga
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class VagaRepository(private val firestore: FirebaseFirestore) {

    private val vagaCollection = firestore.collection("vagas")

    suspend fun getAllVagas(): List<Vaga> {
        return try {
            vagaCollection.orderBy("codigo", Query.Direction.ASCENDING).get().await().toObjects(Vaga::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addVaga(vaga: Vaga): Unit {
        // O Firestore vai gerar um ID automaticamente se não passarmos um no document()
        vagaCollection.add(vaga).await()
    }

    suspend fun getVaga(id: String): Vaga? {
        return try {
            vagaCollection.document(id).get().await().toObject(Vaga::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getVagaByCodigo(codigo: String): Vaga? {
        return try {
            val query = vagaCollection.whereEqualTo("codigo", codigo).limit(1).get().await()
            if (query.isEmpty) {
                null
            } else {
                query.documents.first().toObject(Vaga::class.java)
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getVagaLivre(): Vaga? {
        return try {
            val query = vagaCollection.whereEqualTo("status", Vaga.StatusVaga.LIVRE.name).limit(1).get().await()
            if (query.isEmpty) {
                null
            } else {
                query.documents.first().toObject(Vaga::class.java)
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateVaga(vaga: Vaga): Unit {
        vagaCollection.document(vaga.id).set(vaga).await()
    }
}
