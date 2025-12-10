package com.example.parkmobile.data.repository

import com.example.parkmobile.data.model.HistoricoEstacionamento
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class RelatorioRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val historicoCollection = db.collection("historico_estacionamento")

    // MODIFICADO COM LÓGICA DE DESERIALIZAÇÃO DEFENSIVA
    suspend fun getHistoricoDoUsuarioLogado(): Result<List<HistoricoEstacionamento>> {
        return try {
            val userId = auth.currentUser?.uid
                ?: return Result.failure(Exception("Usuário não está logado."))

            val snapshot = historicoCollection
                .whereEqualTo("idCliente", userId)
                .get()
                .await()

            if (snapshot.isEmpty) {
                println("ParkMobile DEBUG: A consulta ao Firestore para o usuário $userId não retornou documentos.")
                return Result.success(emptyList())
            }

            val resultados = mutableListOf<HistoricoEstacionamento>()
            for (document in snapshot.documents) {
                try {
                    val historico = document.toObject<HistoricoEstacionamento>()
                    if (historico != null) {
                        resultados.add(historico)
                    } else {
                        println("ParkMobile DEBUG: Falha ao converter o documento ${document.id}. O objeto é nulo.")
                    }
                } catch (e: Exception) {
                    // Este catch irá capturar erros de conversão (ex: tipo de dado errado)
                    println("ParkMobile DEBUG: Erro ao deserializar o documento ${document.id}. Causa: ${e.message}")
                }
            }
            Result.success(resultados)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

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
