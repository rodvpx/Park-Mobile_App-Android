package com.example.parkmobile.data.repository

import com.example.parkmobile.data.model.Cliente
import com.example.parkmobile.data.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    suspend fun cadastrar(email: String, senha: String, nome: String, cpf: String): Result<Usuario> {
        try {
            val authResult = auth.createUserWithEmailAndPassword(email, senha).await()
            val firebaseUser = authResult.user
                ?: return Result.failure(Exception("Falha ao criar o usuário no Firebase. O usuário é nulo."))

            val uid = firebaseUser.uid

            // 1. Cria Usuario com username = email
            val novoUsuario = Usuario(
                id = uid,
                username = email,
                role = Usuario.Role.CLIENTE.name,
                criadoPor = uid,
                modificadoPor = uid
            )
            firestore.collection("usuarios")
                .document(uid)
                .set(novoUsuario)
                .await()

            // 2. Procura Cliente pré-cadastrado pelo admin (cpf + idUsuario vazio)
            val query = firestore.collection("clientes")
                .whereEqualTo("cpf", cpf)
                .whereEqualTo("idUsuario", "")
                .limit(1)
                .get()
                .await()

            if (!query.isEmpty) {
                // Cliente pré-cadastrado → só atualiza
                val clienteDoc = query.documents.first()
                val clienteExistente = clienteDoc.toObject(Cliente::class.java)!!
                val clienteId = clienteDoc.id

                val clienteAtualizado = clienteExistente.copy(
                    nome = nome,
                    idUsuario = uid,
                    modificadoPor = uid
                )

                firestore.collection("clientes")
                    .document(clienteId)
                    .set(clienteAtualizado)
                    .await()

                // 3. Atualiza todos os históricos existentes desse cliente: preenche idUsuario
                val historicosSnapshot = firestore.collection("historico_estacionamento")
                    .whereEqualTo("idCliente", clienteId)
                    .whereEqualTo("idUsuario", "") // só os que ainda não têm usuário vinculado
                    .get()
                    .await()

                for (doc in historicosSnapshot.documents) {
                    doc.reference.update("idUsuario", uid).await()
                }

            } else {
                // Nenhum cliente pré-cadastrado → cria um novo Cliente com id = uid
                val novoCliente = Cliente(
                    id = uid,
                    nome = nome,
                    cpf = cpf,
                    idUsuario = uid,
                    criadoPor = uid,
                    modificadoPor = uid
                )

                firestore.collection("clientes")
                    .document(uid)
                    .set(novoCliente)
                    .await()

                // 3'. Atualiza históricos que já possam existir com idCliente = uid (caso raro)
                val historicosSnapshot = firestore.collection("historico_estacionamento")
                    .whereEqualTo("idCliente", uid)
                    .whereEqualTo("idUsuario", "")
                    .get()
                    .await()

                for (doc in historicosSnapshot.documents) {
                    doc.reference.update("idUsuario", uid).await()
                }
            }

            return Result.success(novoUsuario)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun login(email: String, senha: String): Result<Usuario> {
        try {
            val authResult = auth.signInWithEmailAndPassword(email, senha).await()
            val firebaseUser = authResult.user ?: return Result.failure(Exception("Usuário não encontrado"))
            val userDoc = firestore.collection("usuarios").document(firebaseUser.uid).get().await()
            val usuario = userDoc.toObject(Usuario::class.java) ?: return Result.failure(Exception("Dados do usuário não encontrados"))
            return Result.success(usuario)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    fun logout(): Unit {
        auth.signOut()
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
}
