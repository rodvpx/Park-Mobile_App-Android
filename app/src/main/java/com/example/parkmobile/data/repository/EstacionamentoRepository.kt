package com.example.parkmobile.data.repository

import com.example.parkmobile.data.model.ClienteVaga
import com.example.parkmobile.data.model.Vaga
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.UUID

class EstacionamentoRepository(
    private val firestore: FirebaseFirestore,
    private val clienteVagaRepository: ClienteVagaRepository,
    private val clienteRepository: ClienteRepository,
    private val vagaRepository: VagaRepository
) {

    suspend fun checkIn(cpf: String, placa: String, marca: String, modelo: String, cor: String): Result<ClienteVaga> {
        return try {
            val cliente = clienteRepository.getClienteByCpf(cpf)
                ?: return Result.failure(Exception("Cliente não encontrado"))

            firestore.runTransaction {
                val vagaLivre = vagaRepository.getVagaLivre() ?: throw Exception("Nenhuma vaga livre encontrada")

                val novaClienteVaga = ClienteVaga(
                    id = firestore.collection("cliente_vaga").document().id,
                    recibo = gerarRecibo(),
                    placa = placa,
                    marca = marca,
                    modelo = modelo,
                    cor = cor,
                    dataEntrada = Date(),
                    idCliente = cliente.id,
                    idVaga = vagaLivre.id
                )

                vagaRepository.updateVaga(vagaLivre.copy(status = Vaga.StatusVaga.OCUPADA.name))
                clienteVagaRepository.save(novaClienteVaga)
                novaClienteVaga
            }.await()
            Result.success(clienteVagaRepository.getClienteVagaByRecibo(recibo)!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun checkOut(recibo: String): Result<ClienteVaga> {
        return try {
            val clienteVaga = clienteVagaRepository.getClienteVagaByRecibo(recibo)
                ?: return Result.failure(Exception("Recibo não encontrado"))

            firestore.runTransaction {
                val vaga = vagaRepository.getVaga(clienteVaga.idVaga) ?: throw Exception("Vaga não encontrada")

                val dataSaida = Date()
                val valor = calcularCusto(clienteVaga.dataEntrada, dataSaida)
                // A lógica de desconto pode ser mais complexa e talvez precise de mais dados.
                // O ideal é que isso seja feito em um Cloud Function.
                val desconto = 0.0

                val updatedClienteVaga = clienteVaga.copy(
                    dataSaida = dataSaida,
                    valor = valor,
                    desconto = desconto
                )

                vagaRepository.updateVaga(vaga.copy(status = Vaga.StatusVaga.LIVRE.name))
                clienteVagaRepository.save(updatedClienteVaga)
                updatedClienteVaga
            }.await()
            Result.success(clienteVagaRepository.getClienteVagaByRecibo(recibo)!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun gerarRecibo(): String {
        return "REC-${UUID.randomUUID().toString().uppercase().substring(0, 12)}"
    }

    private fun calcularCusto(dataEntrada: Date?, dataSaida: Date): Double {
        if (dataEntrada == null) return 0.0
        val diff = dataSaida.time - dataEntrada.time
        val hours = diff / (1000 * 60 * 60)
        // Custo simplificado: R$10 por hora
        return hours * 10.0
    }
}
