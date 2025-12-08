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

            // Verifica se já existe um cliente com este CPF sem usuário vinculado
            val query = firestore.collection("clientes")
                .whereEqualTo("cpf", cpf)
                .whereEqualTo("idUsuario", "")
                .limit(1)
                .get()
                .await()

            if (!query.isEmpty) {
                // Cliente pré-cadastrado encontrado, carrega o objeto, atualiza e salva de volta.
                val clienteDoc = query.documents.first()
                val clienteExistente = clienteDoc.toObject(Cliente::class.java)!!
                val clienteAtualizado = clienteExistente.copy(
                    nome = nome, // Atualiza para o nome que o cliente digitou
                    idUsuario = firebaseUser.uid // Vincula o novo ID de usuário
                )
                firestore.collection("clientes").document(clienteDoc.id).set(clienteAtualizado).await()

            } else {
                // Nenhum cliente pré-cadastrado, cria um novo
                val novoCliente = Cliente(
                    nome = nome,
                    cpf = cpf,
                    idUsuario = firebaseUser.uid
                )
                // Usamos o UID do usuário como ID do documento para facilitar a busca no futuro
                firestore.collection("clientes").document(firebaseUser.uid).set(novoCliente).await()
            }

            val novoUsuario = Usuario(
                id = firebaseUser.uid,
                username = email,
                role = Usuario.Role.CLIENTE.name
            )
            firestore.collection("usuarios").document(firebaseUser.uid).set(novoUsuario).await()

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

    fun logout(): Unit { // Retorno explícito para ajudar o compilador
        auth.signOut()
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
}
