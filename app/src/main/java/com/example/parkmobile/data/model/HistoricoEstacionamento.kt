package com.example.parkmobile.data.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import java.util.Date

@IgnoreExtraProperties
@Parcelize
data class HistoricoEstacionamento(
    @DocumentId
    val id: String = "",
    val recibo: String = "",
    val placaVeiculo: String = "",
    val marcaVeiculo: String = "",
    val modeloVeiculo: String = "",
    val corVeiculo: String = "",
    val checkIn: Date? = null,
    val checkOut: Date? = null,
    var valor: Double? = null,
    var descontoAplicado: Double? = null, // Renomeado de 'desconto' para clareza
    val idCliente: String = "",
    val idVaga: String = "",
    @ServerTimestamp
    val dataCriacao: Date? = null,
    @ServerTimestamp
    val dataModificacao: Date? = null
) : Parcelable {
    // Construtor vazio necessário para o Firestore
    constructor() : this("", "", "", "", "", "", null, null, null, null, "", "", null, null)
}
