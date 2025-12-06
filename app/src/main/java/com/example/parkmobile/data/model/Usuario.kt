package com.example.parkmobile.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

@IgnoreExtraProperties
data class Usuario(
    @DocumentId
    val id: String = "",
    val username: String = "",
    val role: String = Role.CLIENTE.name,
    @ServerTimestamp
    val dataCriacao: Date? = null,
    @ServerTimestamp
    val dataModificacao: Date? = null,
    val criadoPor: String = "",
    val modificadoPor: String = ""
) {
    enum class Role {
        ADMIN, CLIENTE
    }
}
