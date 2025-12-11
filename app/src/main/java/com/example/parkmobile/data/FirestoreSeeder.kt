package com.example.parkmobile.data

import android.util.Log
import com.example.parkmobile.data.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreSeeder(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    // Data class para organizar as informações dos admins a serem criados
    private data class AdminUserData(
        val email: String,
        val senha: String
    )

    suspend fun seedAdminUsers() {
        try {
            Log.d("FirestoreSeeder", "Iniciando o seeding de usuários administradores...")

            val adminUsers = listOf(
                AdminUserData("rodrigo@parkmobile.com", "senha123"),
                AdminUserData("luan@parkmobile.com", "senha123")
            )

            for (adminData in adminUsers) {
                try {
                    // 1. Verifica se o usuário já existe na coleção 'usuarios' do Firestore
                    //    pelo email (que usamos como username).
                    val existingUser = firestore.collection("usuarios")
                        .whereEqualTo("username", adminData.email)
                        .limit(1)
                        .get()
                        .await()

                    if (existingUser.isEmpty) {
                        // 2. Se não existir no Firestore, cria o usuário no Firebase Authentication.
                        val authResult = firebaseAuth.createUserWithEmailAndPassword(
                            adminData.email,
                            adminData.senha
                        ).await()

                        val firebaseUser = authResult.user
                        if (firebaseUser != null) {
                            // 3. Com o usuário criado na autenticação, cria o documento correspondente no Firestore.
                            val novoAdminUser = Usuario(
                                id = firebaseUser.uid,
                                username = adminData.email, // O username é o próprio email
                                role = Usuario.Role.ADMIN.name,
                                criadoPor = "SEEDER",
                                modificadoPor = "SEEDER"
                            )

                            firestore.collection("usuarios")
                                .document(firebaseUser.uid)
                                .set(novoAdminUser)
                                .await()

                            Log.d("FirestoreSeeder", "✓ Usuário admin criado com sucesso: ${adminData.email}")
                        }
                    } else {
                        Log.d("FirestoreSeeder", "⚠ Usuário admin já existe no Firestore: ${adminData.email}")
                    }
                } catch (e: Exception) {
                    // Trata erros na criação de um admin específico (ex: email já cadastrado no Auth).
                    Log.e("FirestoreSeeder", "✗ Erro ao criar o admin '${adminData.email}': ${e.message}")
                }
            }

            Log.d("FirestoreSeeder", "✓ Seeding de usuários admin concluído!")

        } catch (e: Exception) {
            // Trata erros gerais no processo de seeding.
            Log.e("FirestoreSeeder", "✗ Erro geral durante o seeding de administradores: ${e.message}", e)
        }
    }
}
