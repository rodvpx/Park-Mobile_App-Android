package com.example.parkmobile.ui.configuracoes

import com.example.parkmobile.data.model.Cliente
import com.example.parkmobile.data.model.Usuario
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class UserProfile(val nome: String, val cpf: String, val email: String)

class ConfiguracoesRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    suspend fun getUserProfile(): Result<UserProfile> {
        return try {
            val firebaseUser = auth.currentUser ?: return Result.failure(Exception("Usuário não logado"))
            val uid = firebaseUser.uid

            val userDoc = firestore.collection("usuarios").document(uid).get().await()
            val usuario = userDoc.toObject(Usuario::class.java)

            val userProfile = if (usuario != null) {
                // Usuário encontrado na coleção 'usuarios', usa a role para decidir
                when (Usuario.Role.valueOf(usuario.role)) {
                    Usuario.Role.ADMIN -> {
                        UserProfile(nome = "Administrador", cpf = "N/A", email = firebaseUser.email ?: "")
                    }
                    Usuario.Role.CLIENTE -> {
                        fetchClienteProfile(uid, firebaseUser.email ?: "")
                    }
                }
            } else {
                // Usuário não encontrado, assume que é um cliente (provavelmente conta antiga)
                fetchClienteProfile(uid, firebaseUser.email ?: "")
            }
            Result.success(userProfile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun fetchClienteProfile(uid: String, email: String): UserProfile {
        // Busca o cliente pelo campo idUsuario, que é a forma correta e unificada
        val query = firestore.collection("clientes")
            .whereEqualTo("idUsuario", uid)
            .limit(1)
            .get()
            .await()

        if (!query.isEmpty) {
            val clienteDoc = query.documents.first()
            val cliente = clienteDoc.toObject(Cliente::class.java)
            if (cliente != null) {
                return UserProfile(nome = cliente.nome, cpf = cliente.cpf, email = email)
            }
        }
        // Se não encontrar o cliente por algum motivo, retorna o perfil padrão
        return UserProfile(nome = "Cliente", cpf = "N/A", email = email)
    }

    suspend fun changePassword(current: String, new: String): Result<Unit> {
        return try {
            val user = auth.currentUser!!
            val credential = EmailAuthProvider.getCredential(user.email!!, current)
            user.reauthenticate(credential).await()
            user.updatePassword(new).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        auth.signOut()
    }
}
