package com.example.parkmobile.data.model

import java.io.Serializable

data class HistoricoItem(
    val codigo: String,
    val vaga: String,
    val entrada: String,
    val saida: String,
    val cpf: String,
    val tempo: String,
    val placa: String,
    val marca: String,
    val modelo: String,
    val cor: String,
    val valor: String,
    val desconto: String,
    val total: String
) : Serializable
