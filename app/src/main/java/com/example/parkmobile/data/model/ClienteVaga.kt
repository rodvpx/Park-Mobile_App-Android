package com.example.parkmobile.data.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import java.util.Date

@IgnoreExtraProperties
@Parcelize
data class ClienteVaga(
    @DocumentId
    val id: String = "",
    val recibo: String = "",
    val placa: String = "",
    val marca: String = "",
    val modelo: String = "",
    val cor: String = "",
    val dataEntrada: Date? = null,
    var dataSaida: Date? = null,
    var valor: Double? = null,
    var desconto: Double? = null,
    val idCliente: String = "",
    val idVaga: String = "",
    @ServerTimestamp
    val dataCriacao: Date? = null,
    @ServerTimestamp
    val dataModificacao: Date? = null,
    val criadoPor: String = "",
    val modificadoPor: String = ""
) : Parcelable
