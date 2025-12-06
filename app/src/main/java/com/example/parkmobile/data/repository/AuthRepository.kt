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

            val novoUsuario = Usuario(
                id = firebaseUser.uid,
                username = email,
                role = Usuario.Role.CLIENTE.name
            )
            firestore.collection("usuarios").document(firebaseUser.uid).set(novoUsuario).await()

            val novoCliente = Cliente(
                id = firebaseUser.uid,
                nome = nome,
                cpf = cpf,
                idUsuario = firebaseUser.uid
            )
            firestore.collection("clientes").document(firebaseUser.uid).set(novoCliente).await()

            return Result.success(novoUsuario)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun login(email: String, senha: String): Result<Unit> {
        try {
            auth.signInWithEmailAndPassword(email, senha).await()
            return Result.success(Unit)
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
