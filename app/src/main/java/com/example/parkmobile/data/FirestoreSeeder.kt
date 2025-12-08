package com.example.parkmobile.data

import android.util.Log
import com.example.parkmobile.data.model.Cliente
import com.example.parkmobile.data.model.Usuario
import com.example.parkmobile.data.model.Vaga
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object FirestoreSeeder {

    private const val TAG = "FirestoreSeeder"
    private const val DEFAULT_PASSWORD = "password123" // Senha padrão para usuários de teste

    suspend fun seedDatabase() {
        val auth = Firebase.auth
        val db = Firebase.firestore

        try {
            Log.d(TAG, "Iniciando o processo de popular o banco de dados...")

            // 1. Checar e Criar Vagas (apenas se não existirem)
            val vagasCollection = db.collection("vagas")
            if (vagasCollection.limit(1).get().await().isEmpty) {
                Log.d(TAG, "Criando vagas...")
                val vagasBatch = db.batch()
                (1..20).forEach { i ->
                    val vaga = Vaga(codigo = "V$i", status = Vaga.StatusVaga.LIVRE.name, criadoPor = "seeder")
                    val docRef = vagasCollection.document(vaga.codigo)
                    vagasBatch.set(docRef, vaga)
                }
                vagasBatch.commit().await()
                Log.d(TAG, "20 vagas criadas com sucesso.")
            } else {
                Log.d(TAG, "Coleção 'vagas' já existe. Pulando criação de vagas.")
            }

            // 2. Criar Usuários na Autenticação e no Firestore
            Log.d(TAG, "Criando usuários e clientes...")
            val usuariosParaCriar = listOf(
                Pair("admin@parkmobile.com", Usuario.Role.ADMIN),
                Pair("cliente1@email.com", Usuario.Role.CLIENTE),
                Pair("cliente2@email.com", Usuario.Role.CLIENTE),
                Pair("cliente3@email.com", Usuario.Role.CLIENTE)
            )
            val clientesInfo = mapOf(
                "cliente1@email.com" to Cliente(nome = "João da Silva", cpf = "111.111.111-11"),
                "cliente2@email.com" to Cliente(nome = "Maria Oliveira", cpf = "222.222.222-22"),
                "cliente3@email.com" to Cliente(nome = "Pedro Martins", cpf = "333.333.333-33")
            )

            val firestoreBatch = db.batch()

            for ((email, role) in usuariosParaCriar) {
                try {
                    // Criar usuário no Firebase Auth
                    val authResult = auth.createUserWithEmailAndPassword(email, DEFAULT_PASSWORD).await()
                    val firebaseUser = authResult.user!!
                    Log.d(TAG, "Usuário criado no Auth com sucesso: ${firebaseUser.email}")

                    // Criar documento na coleção 'usuarios'
                    val usuario = Usuario(id = firebaseUser.uid, username = email, role = role.name, criadoPor = "seeder")
                    val usuarioDocRef = db.collection("usuarios").document(firebaseUser.uid)
                    firestoreBatch.set(usuarioDocRef, usuario)

                    // Se for um cliente, criar documento na coleção 'clientes'
                    if (role == Usuario.Role.CLIENTE) {
                        clientesInfo[email]?.let { clienteInfo ->
                            val cliente = clienteInfo.copy(idUsuario = firebaseUser.uid, criadoPor = "seeder")
                            val clienteDocRef = db.collection("clientes").document()
                            firestoreBatch.set(clienteDocRef, cliente)
                        }
                    }
                } catch (e: FirebaseAuthUserCollisionException) {
                    Log.w(TAG, "Usuário '$email' já existe no Firebase Auth. Pulando criação no Auth e Firestore.")
                } catch (e: Exception) {
                    Log.e(TAG, "Erro ao criar usuário '$email' no Auth.", e)
                }
            }

            firestoreBatch.commit().await()
            Log.d(TAG, "Processo de popular o banco de dados finalizado.")

        } catch (e: Exception) {
            Log.e(TAG, "Erro GERAL ao popular o banco de dados", e)
            throw e
        }
    }
}
