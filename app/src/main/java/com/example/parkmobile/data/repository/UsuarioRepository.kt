package com.example.parkmobile.data.repository

import com.example.parkmobile.data.model.Usuario
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UsuarioRepository(private val firestore: FirebaseFirestore) {

    private val userCollection = firestore.collection("usuarios")

    suspend fun getUsuario(id: String): Usuario? {
        return try {
            userCollection.document(id).get().await().toObject(Usuario::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getUsuarioByUsername(username: String): Usuario? {
        return try {
            val query = userCollection.whereEqualTo("username", username).limit(1).get().await()
            if (query.isEmpty) {
                null
            } else {
                query.documents.first().toObject(Usuario::class.java)
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun createUsuario(usuario: Usuario): Unit {
        userCollection.document(usuario.id).set(usuario).await()
    }
}
