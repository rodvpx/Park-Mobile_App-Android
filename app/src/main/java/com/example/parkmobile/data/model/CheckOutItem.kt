package com.example.parkmobile.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CheckOutItem(
    val nome: String,
    val cpf: String,
    val placa: String,
    val vaga: String,
    val entrada: String
) : Parcelable
