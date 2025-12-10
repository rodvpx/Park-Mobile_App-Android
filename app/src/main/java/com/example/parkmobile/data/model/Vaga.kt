package com.example.parkmobile.data.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import java.util.Date

@IgnoreExtraProperties
@Parcelize
data class Vaga(
    @DocumentId
    val id: String = "",
    val codigo: String = "",
    val status: String = StatusVaga.LIVRE.name,
    @ServerTimestamp
    val dataCriacao: Date? = null,
    @ServerTimestamp
    val dataModificacao: Date? = null,
    val criadoPor: String = "",
    val modificadoPor: String = ""
) : Parcelable {
    // Construtor vazio necessário para o Firestore
    constructor() : this("", "", StatusVaga.LIVRE.name, null, null, "", "")

    enum class StatusVaga {
        LIVRE, OCUPADA
    }
}
