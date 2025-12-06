package com.example.parkmobile.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

@IgnoreExtraProperties
data class Cliente(
    @DocumentId
    val id: String = "",
    val nome: String = "",
    val cpf: String = "",
    val idUsuario: String = "",
    @ServerTimestamp
    val dataCriacao: Date? = null,
    @ServerTimestamp
    val dataModificacao: Date? = null,
    val criadoPor: String = "",
    val modificadoPor: String = ""
)
