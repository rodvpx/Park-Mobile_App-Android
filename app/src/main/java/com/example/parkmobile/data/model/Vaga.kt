package com.example.parkmobile.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Vaga(
    val id: Int,
    val nome: String,
    val status: String // Pode ser "disponível", "ocupada", "indisponível"
) : Parcelable