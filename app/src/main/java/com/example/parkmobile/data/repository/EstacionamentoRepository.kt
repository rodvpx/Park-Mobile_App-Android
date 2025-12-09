package com.example.parkmobile.data.repository

import com.example.parkmobile.data.model.Cliente
import com.example.parkmobile.data.model.HistoricoEstacionamento
import com.example.parkmobile.data.model.Vaga
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class EstacionamentoRepository {

    private val db = FirebaseFirestore.getInstance()
    private val historicoCollection = db.collection("historico_estacionamento")
    private val clientesCollection = db.collection("clientes")
    private val vagasCollection = db.collection("vagas")

    suspend fun getVeiculosEstacionados(): Result<List<HistoricoEstacionamento>> {
        return try {
            val snapshot = historicoCollection
                .whereEqualTo("checkOut", null)
                .get()
                .await()
            Result.success(snapshot.toObjects(HistoricoEstacionamento::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getClientes(): Result<List<Cliente>> {
        return try {
            val snapshot = clientesCollection.get().await()
            Result.success(snapshot.toObjects(Cliente::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getVagas(): Result<List<Vaga>> {
        return try {
            val snapshot = vagasCollection.get().await()
            Result.success(snapshot.toObjects(Vaga::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getVagasLivres(): Result<List<Vaga>> {
        return try {
            val snapshot = vagasCollection.whereEqualTo("status", Vaga.StatusVaga.LIVRE.name).get().await()
            Result.success(snapshot.toObjects(Vaga::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun realizarCheckIn(historico: HistoricoEstacionamento): Result<Unit> {
        return try {
            historicoCollection.document(historico.id).set(historico).await()
            // Marcar vaga como ocupada
            vagasCollection.document(historico.idVaga).update("status", Vaga.StatusVaga.OCUPADA.name).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun realizarCheckOut(historico: HistoricoEstacionamento): Result<Unit> {
        return try {
            historicoCollection.document(historico.id).set(historico).await()
            // Marcar vaga como livre
            if (historico.idVaga.isNotEmpty()) {
                vagasCollection.document(historico.idVaga).update("status", Vaga.StatusVaga.LIVRE.name).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
