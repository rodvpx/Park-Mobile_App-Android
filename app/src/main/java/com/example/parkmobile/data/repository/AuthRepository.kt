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
        return try {
            // 1. Criar o usuário no Firebase Auth
            val authResult = auth.createUserWithEmailAndPassword(email, senha).await()
            val firebaseUser = authResult.user ?: return Result.failure(Exception("Usuário Firebase não encontrado após cadastro."))

            // 2. Criar o nosso objeto de Usuário
            val novoUsuario = Usuario(
                id = firebaseUser.uid,
                username = email,
                role = Usuario.Role.ROLE_CLIENTE.name
            )
            firestore.collection("usuarios").document(firebaseUser.uid).set(novoUsuario).await()

            // 3. Criar o nosso objeto de Cliente associado
            val novoCliente = Cliente(
                // O id do cliente pode ser o mesmo do usuário ou um novo ID.
                // Usar o mesmo ID do usuário simplifica as buscas.
                id = firebaseUser.uid,
                nome = nome,
                cpf = cpf,
                idUsuario = firebaseUser.uid
            )
            firestore.collection("clientes").document(firebaseUser.uid).set(novoCliente).await()

            Result.success(novoUsuario)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, senha: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, senha).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
}
