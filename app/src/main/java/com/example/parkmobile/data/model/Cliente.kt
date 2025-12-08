package com.example.parkmobile.data.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import java.util.Date

@IgnoreExtraProperties
@Parcelize
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
) : Parcelable {
    // Construtor vazio necessário para o Firestore
    constructor() : this("", "", "", "", null, null, "", "")
}
