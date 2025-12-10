package com.example.parkmobile.data

import android.util.Log
import com.example.parkmobile.data.model.Cliente
import com.example.parkmobile.data.model.HistoricoEstacionamento
import com.example.parkmobile.data.model.Usuario
import com.example.parkmobile.data.model.Vaga
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.Date

object FirestoreSeeder {

    private const val TAG = "FirestoreSeeder"
    private const val DEFAULT_PASSWORD = "senha123" // Senha padrão para usuários de teste

    suspend fun seedDatabase() {
        val auth = Firebase.auth
        val db = Firebase.firestore

        try {
            Log.d(TAG, "Iniciando o processo de popular o banco de dados...")

            // 1. Checar e Criar Vagas (A01-A10, B01-B10)
            val vagasCollection = db.collection("vagas")
            if (vagasCollection.limit(1).get().await().isEmpty) {
                Log.d(TAG, "Criando 20 vagas...")
                val vagasBatch = db.batch()

                // Vagas A01 até A10
                (1..10).forEach { i ->
                    val vaga = Vaga(codigo = "A${String.format("%02d", i)}", status = Vaga.StatusVaga.LIVRE.name, criadoPor = "seeder")
                    val docRef = vagasCollection.document(vaga.codigo)
                    vagasBatch.set(docRef, vaga)
                }

                // Vagas B01 até B10
                (1..10).forEach { i ->
                    val vaga = Vaga(codigo = "B${String.format("%02d", i)}", status = Vaga.StatusVaga.LIVRE.name, criadoPor = "seeder")
                    val docRef = vagasCollection.document(vaga.codigo)
                    vagasBatch.set(docRef, vaga)
                }

                vagasBatch.commit().await()
                Log.d(TAG, "20 vagas criadas: A01-A10 e B01-B10.")
            } else {
                Log.d(TAG, "Coleção 'vagas' já existe. Pulando criação de vagas.")
            }

            // 2. Criar Administradores (3 admins)
            Log.d(TAG, "Criando administradores...")
            val adminEmails = listOf(
                "admin1@parkmobile.com",
                "admin2@parkmobile.com",
                "admin3@parkmobile.com"
            )

            // 3. Criar Clientes (10 clientes) - CPF SEM PONTOS E TRAÇOS
            Log.d(TAG, "Criando 10 clientes...")
            val clientesInfo = mapOf(
                "cliente1@parkmobile.com" to Cliente(nome = "João Silva", cpf = "11111111111"),
                "cliente2@parkmobile.com" to Cliente(nome = "Maria Oliveira", cpf = "22222222222"),
                "cliente3@parkmobile.com" to Cliente(nome = "Pedro Santos", cpf = "33333333333"),
                "cliente4@parkmobile.com" to Cliente(nome = "Ana Costa", cpf = "44444444444"),
                "cliente5@parkmobile.com" to Cliente(nome = "Carlos Lima", cpf = "55555555555"),
                "cliente6@parkmobile.com" to Cliente(nome = "Fernanda Souza", cpf = "66666666666"),
                "cliente7@parkmobile.com" to Cliente(nome = "Ricardo Almeida", cpf = "77777777777"),
                "cliente8@parkmobile.com" to Cliente(nome = "Juliana Pereira", cpf = "88888888888"),
                "cliente9@parkmobile.com" to Cliente(nome = "Lucas Ferreira", cpf = "99999999999"),
                "cliente10@parkmobile.com" to Cliente(nome = "Camila Rodrigues", cpf = "00000000000")
            )

            val usuariosParaCriar = adminEmails.map { Pair(it, Usuario.Role.ADMIN) } +
                    clientesInfo.keys.map { Pair(it, Usuario.Role.CLIENTE) }

            val firestoreBatch = db.batch()
            val userEmailMap = mutableMapOf<String, String>() // email -> uid

            // Criar usuários no Auth e preparar documentos Firestore
            for ((email, role) in usuariosParaCriar) {
                try {
                    val authResult = auth.createUserWithEmailAndPassword(email, DEFAULT_PASSWORD).await()
                    val firebaseUser = authResult.user!!
                    userEmailMap[email] = firebaseUser.uid
                    Log.d(TAG, "Usuário criado no Auth: $email -> ${firebaseUser.uid}")

                    // Documento usuário
                    val usuario = Usuario(
                        id = firebaseUser.uid,
                        username = email,
                        role = role.name,
                        criadoPor = "seeder"
                    )
                    val usuarioDocRef = db.collection("usuarios").document(firebaseUser.uid)
                    firestoreBatch.set(usuarioDocRef, usuario)

                    // Documento cliente se for cliente
                    if (role == Usuario.Role.CLIENTE) {
                        clientesInfo[email]?.let { clienteInfo ->
                            val cliente = clienteInfo.copy(idUsuario = firebaseUser.uid, criadoPor = "seeder")
                            val clienteDocRef = db.collection("clientes").document()
                            firestoreBatch.set(clienteDocRef, cliente)
                        }
                    }
                } catch (e: FirebaseAuthUserCollisionException) {
                    Log.w(TAG, "Usuário '$email' já existe no Auth. Pulando.")
                } catch (e: Exception) {
                    Log.e(TAG, "Erro ao criar usuário '$email': ${e.message}", e)
                }
            }

            firestoreBatch.commit().await()

            // 4. Criar MUITOS Históricos de Estacionamento (12 registros: 7 SEM checkout, 5 COM checkout)
            Log.d(TAG, "Criando 12 registros de histórico de estacionamento...")
            val historicoBatch = db.batch()

            val agora = System.currentTimeMillis()
            val historicos = listOf(
                // 🚗 CHECK-INS ATIVOS (SEM CHECKOUT) - 7 vagas ocupadas
                HistoricoEstacionamento(
                    recibo = "REC001", placaVeiculo = "ABC-1234", marcaVeiculo = "VW", modeloVeiculo = "Gol", corVeiculo = "Prata",
                    checkIn = Date(agora - 2 * 60 * 60 * 1000),  // 2h atrás
                    idCliente = userEmailMap["cliente1@parkmobile.com"]!!, idVaga = "A01"
                ),
                HistoricoEstacionamento(
                    recibo = "REC002", placaVeiculo = "XYZ-5678", marcaVeiculo = "Fiat", modeloVeiculo = "Uno", corVeiculo = "Preto",
                    checkIn = Date(agora - 1 * 60 * 60 * 1000),  // 1h atrás
                    idCliente = userEmailMap["cliente2@parkmobile.com"]!!, idVaga = "B05"
                ),
                HistoricoEstacionamento(
                    recibo = "REC003", placaVeiculo = "DEF-9012", marcaVeiculo = "Honda", modeloVeiculo = "Civic", corVeiculo = "Branco",
                    checkIn = Date(agora - 45 * 60 * 1000),     // 45min atrás
                    idCliente = userEmailMap["cliente5@parkmobile.com"]!!, idVaga = "A07"
                ),
                HistoricoEstacionamento(
                    recibo = "REC004", placaVeiculo = "GHI-3456", marcaVeiculo = "Toyota", modeloVeiculo = "Corolla", corVeiculo = "Azul",
                    checkIn = Date(agora - 3 * 60 * 60 * 1000),  // 3h atrás
                    idCliente = userEmailMap["cliente7@parkmobile.com"]!!, idVaga = "B08"
                ),
                HistoricoEstacionamento(
                    recibo = "REC005", placaVeiculo = "JKL-7890", marcaVeiculo = "Chevrolet", modeloVeiculo = "Onix", corVeiculo = "Vermelho",
                    checkIn = Date(agora - 20 * 60 * 1000),     // 20min atrás
                    idCliente = userEmailMap["cliente9@parkmobile.com"]!!, idVaga = "A04"
                ),
                HistoricoEstacionamento(
                    recibo = "REC006", placaVeiculo = "MNO-1122", marcaVeiculo = "Hyundai", modeloVeiculo = "HB20", corVeiculo = "Cinza",
                    checkIn = Date(agora - 5 * 60 * 60 * 1000),  // 5h atrás
                    idCliente = userEmailMap["cliente3@parkmobile.com"]!!, idVaga = "B03"
                ),
                HistoricoEstacionamento(
                    recibo = "REC007", placaVeiculo = "PQR-4455", marcaVeiculo = "Ford", modeloVeiculo = "Ka", corVeiculo = "Amarelo",
                    checkIn = Date(agora - 90 * 60 * 1000),     // 1h30 atrás
                    idCliente = userEmailMap["cliente10@parkmobile.com"]!!, idVaga = "A09"
                ),

                // ✅ CHECK-INS CONCLUÍDOS (COM CHECKOUT) - 5 registros
                HistoricoEstacionamento(
                    recibo = "REC008", placaVeiculo = "STU-7788", marcaVeiculo = "VW", modeloVeiculo = "Polo", corVeiculo = "Preto",
                    checkIn = Date(agora - 4 * 60 * 60 * 1000), checkOut = Date(agora - 30 * 60 * 1000),
                    valor = 15.00, descontoAplicado = 0.0,
                    idCliente = userEmailMap["cliente4@parkmobile.com"]!!, idVaga = "A03"
                ),
                HistoricoEstacionamento(
                    recibo = "REC009", placaVeiculo = "VWX-9900", marcaVeiculo = "Fiat", modeloVeiculo = "Argo", corVeiculo = "Branco",
                    checkIn = Date(agora - 24 * 60 * 60 * 1000), checkOut = Date(agora - 2 * 60 * 60 * 1000),
                    valor = 32.50, descontoAplicado = 3.25,
                    idCliente = userEmailMap["cliente6@parkmobile.com"]!!, idVaga = "B02"
                ),
                HistoricoEstacionamento(
                    recibo = "REC010", placaVeiculo = "YZA-2233", marcaVeiculo = "Renault", modeloVeiculo = "Sandero", corVeiculo = "Prata",
                    checkIn = Date(agora - 8 * 60 * 60 * 1000), checkOut = Date(agora - 1 * 60 * 60 * 1000),
                    valor = 22.00, descontoAplicado = 0.0,
                    idCliente = userEmailMap["cliente8@parkmobile.com"]!!, idVaga = "A06"
                ),
                HistoricoEstacionamento(
                    recibo = "REC011", placaVeiculo = "BCD-5566", marcaVeiculo = "Honda", modeloVeiculo = "Fit", corVeiculo = "Azul",
                    checkIn = Date(agora - 12 * 60 * 60 * 1000), checkOut = Date(agora - 6 * 60 * 60 * 1000),
                    valor = 18.75, descontoAplicado = 1.87,
                    idCliente = userEmailMap["cliente1@parkmobile.com"]!!, idVaga = "B07"
                ),
                HistoricoEstacionamento(
                    recibo = "REC012", placaVeiculo = "EFG-8899", marcaVeiculo = "Toyota", modeloVeiculo = "Etios", corVeiculo = "Cinza",
                    checkIn = Date(agora - 2 * 24 * 60 * 60 * 1000), checkOut = Date(agora - 12 * 60 * 60 * 1000),
                    valor = 48.00, descontoAplicado = 4.80,
                    idCliente = userEmailMap["cliente2@parkmobile.com"]!!, idVaga = "A02"
                )
            )

            historicos.forEach { historico ->
                val historicoDocRef = db.collection("historico_estacionamento").document(historico.recibo)
                historicoBatch.set(historicoDocRef, historico)
            }

            historicoBatch.commit().await()
            Log.d(TAG, "✅ 12 registros de histórico criados (7 ATIVOS, 5 CONCLUÍDOS).")

            // 5. Atualizar status das 7 vagas OCUPADAS
            Log.d(TAG, "Atualizando status das 7 vagas ocupadas...")
            val vagasOcupadasBatch = db.batch()
            val vagasOcupadas = listOf("A01", "B05", "A07", "B08", "A04", "B03", "A09")
            vagasOcupadas.forEach { vagaId ->
                vagasOcupadasBatch.update(vagasCollection.document(vagaId), "status", Vaga.StatusVaga.OCUPADA.name)
            }
            vagasOcupadasBatch.commit().await()
            Log.d(TAG, "Vagas ocupadas: $vagasOcupadas")

            Log.d(TAG, "✅ Banco de dados popularizado com sucesso!")
            Log.d(TAG, "📋 RESUMO COMPLETO:")
            Log.d(TAG, "   • 20 vagas (A01-A10, B01-B10)")
            Log.d(TAG, "   • 3 administradores (senha: senha123)")
            Log.d(TAG, "   • 10 clientes (CPFs 11 dígitos)")
            Log.d(TAG, "   • 🟢 7 check-ins ATIVOS (sem checkout)")
            Log.d(TAG, "   • 🟡 5 check-outs CONCLUÍDOS")
            Log.d(TAG, "   • 🔴 7 vagas OCUPADAS / 13 LIVRES")

        } catch (e: Exception) {
            Log.e(TAG, "❌ Erro ao popular o banco de dados", e)
            throw e
        }
    }
}
