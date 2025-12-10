package com.example.parkmobile.data.repository

import com.example.parkmobile.data.model.Cliente
import com.example.parkmobile.data.model.HistoricoEstacionamento
import com.example.parkmobile.data.model.Vaga
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.UUID

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

    // MODIFICADO: Recebe os IDs e outros dados, e monta o objeto aqui dentro
    suspend fun realizarCheckIn(
        clienteDocId: String,
        idVaga: String,
        placa: String,
        marca: String,
        modelo: String,
        cor: String
    ): Result<Unit> {
        return try {
            // 1. Busca o cliente para pegar o ID de autenticação (idUsuario)
            val clienteDoc = clientesCollection.document(clienteDocId).get().await()
            val idUsuarioAuth = clienteDoc.getString("idUsuario")
                ?: return Result.failure(Exception("Cliente selecionado não possui um usuário vinculado."))

            // 2. Cria o objeto de histórico com o ID de autenticação correto
            val novoHistorico = HistoricoEstacionamento(
                id = UUID.randomUUID().toString(),
                recibo = "REC-${System.currentTimeMillis()}",
                placaVeiculo = placa,
                marcaVeiculo = marca,
                modeloVeiculo = modelo,
                corVeiculo = cor,
                checkIn = Date(),
                idCliente = idUsuarioAuth, // CORREÇÃO APLICADA
                idVaga = idVaga
            )

            // 3. Salva o histórico e atualiza o status da vaga
            historicoCollection.document(novoHistorico.id).set(novoHistorico).await()
            vagasCollection.document(idVaga).update("status", Vaga.StatusVaga.OCUPADA.name).await()
            
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
