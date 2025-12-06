package com.example.parkmobile.data.repository

import com.example.parkmobile.data.model.ClienteVaga
import com.example.parkmobile.data.model.Vaga
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.UUID

class EstacionamentoRepository(
    private val firestore: FirebaseFirestore,
    private val clienteRepository: ClienteRepository
) {

    suspend fun checkIn(cpf: String, placa: String, marca: String, modelo: String, cor: String): Result<ClienteVaga> {
        try {
            // 1. Busca inicial (fora da transação)
            val cliente = clienteRepository.getClienteByCpf(cpf)
                ?: return Result.failure(Exception("Cliente com CPF '$cpf' não encontrado."))

            // 2. Busca a primeira vaga livre (fora da transação)
            val vagasLivresQuery = firestore.collection("vagas")
                .whereEqualTo("status", "LIVRE").limit(1)
            val vagaLivreDoc = vagasLivresQuery.get().await().documents.firstOrNull()
                ?: return Result.failure(Exception("Nenhuma vaga livre encontrada no momento."))

            // 2. Executa a transação
            val novaClienteVaga = firestore.runTransaction { transaction ->
                // Leitura da vaga dentro da transação para garantir consistência
                val vagaSnapshot = transaction.get(vagaLivreDoc.reference)

                // Prepara os dados para escrita
                val vagaLivre = vagaSnapshot.toObject(Vaga::class.java)
                    ?: throw Exception("Falha ao converter a vaga encontrada.")
                val novaClienteVagaRef = firestore.collection("cliente_vaga").document()

                val clienteVaga = ClienteVaga(
                    id = novaClienteVagaRef.id,
                    recibo = gerarRecibo(),
                    placa = placa,
                    marca = marca,
                    modelo = modelo,
                    cor = cor,
                    dataEntrada = Date(),
                    idCliente = cliente.id,
                    idVaga = vagaLivre.id
                )

                // Escritas dentro da transação
                transaction.set(novaClienteVagaRef, clienteVaga)
                // Confirma se a vaga ainda está livre antes de ocupá-la
                if (vagaLivre.status != Vaga.StatusVaga.LIVRE.name) throw Exception("A vaga foi ocupada por outro cliente.")

                transaction.update(vagaSnapshot.reference, "status", Vaga.StatusVaga.OCUPADA.name)

                // Retorna o objeto criado para o resultado da transação
                clienteVaga
            }.await()

            return Result.success(novaClienteVaga)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun checkOut(recibo: String): Result<ClienteVaga> {
        try {
            // 1. Busca inicial (fora da transação)
            val query = firestore.collection("cliente_vaga")
                .whereEqualTo("recibo", recibo)
                .whereEqualTo("dataSaida", null) // Garante que é um check-in ativo
                .limit(1)

            val snapshot = query.get().await()
            val clienteVagaExistente = snapshot.documents.firstOrNull()?.toObject(ClienteVaga::class.java) ?: return Result.failure(Exception("Recibo '$recibo' não encontrado ou check-out já realizado."))

            // 2. Executa a transação
            val clienteVagaAtualizada = firestore.runTransaction { transaction ->
                // Referências aos documentos
                val clienteVagaRef = firestore.collection("cliente_vaga").document(clienteVagaExistente.id)
                val vagaRef = firestore.collection("vagas").document(clienteVagaExistente.idVaga)

                // Leituras dentro da transação
                val vagaSnapshot = transaction.get(vagaRef)
                if (!vagaSnapshot.exists()) throw Exception("A vaga associada a este recibo não foi encontrada.")

                // Prepara os dados para escrita
                val dataSaida = Date()
                val valor = calcularCusto(clienteVagaExistente.dataEntrada, dataSaida)
                val desconto = 0.0 // Lógica de desconto pode ser adicionada aqui

                val updatedClienteVaga = clienteVagaExistente.copy(
                    dataSaida = dataSaida,
                    valor = valor,
                    desconto = desconto
                )

                // Escritas dentro da transação
                transaction.set(clienteVagaRef, updatedClienteVaga)
                transaction.update(vagaRef, "status", Vaga.StatusVaga.LIVRE.name)

                // Retorna o objeto atualizado para o resultado da transação
                updatedClienteVaga
            }.await()

            return Result.success(clienteVagaAtualizada)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    private fun gerarRecibo(): String {
        return "REC-${UUID.randomUUID().toString().uppercase().substring(0, 12)}"
    }

    private fun calcularCusto(dataEntrada: Date?, dataSaida: Date): Double {
        if (dataEntrada == null) return 0.0
        val diffMillis = dataSaida.time - dataEntrada.time
        val hours = diffMillis / (1000.0 * 60.0 * 60.0)
        // Custo simplificado: R$10 por hora (usar Double na divisão para precisão)
        return hours * 10.0
    }
}
