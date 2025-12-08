package com.example.parkmobile.data.repository

import com.example.parkmobile.data.model.Vaga
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class VagaRepository(private val firestore: FirebaseFirestore) {

    private val vagasCollection = firestore.collection("vagas")

    // Agora retorna um Flow para atualizações em tempo real
    fun getAllVagas(): Flow<List<Vaga>> = callbackFlow {
        val listener = vagasCollection
            .orderBy("codigo", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error) // Fecha o flow com erro
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val vagas = snapshot.toObjects(Vaga::class.java)
                    trySend(vagas).isSuccess // Envia a nova lista para o flow
                }
            }
        awaitClose { listener.remove() } // Garante que o listener seja removido quando o flow for cancelado
    }

    suspend fun addVaga(vaga: Vaga) {
        // O ID do documento será o próprio código da vaga para fácil acesso
        vagasCollection.document(vaga.codigo).set(vaga).await()
    }

    suspend fun updateVaga(vaga: Vaga) {
        vagasCollection.document(vaga.id).set(vaga).await()
    }
}
