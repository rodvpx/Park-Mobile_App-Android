package com.example.parkmobile.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HistoricoEstacionamentoDetalhado(
    val historico: HistoricoEstacionamento,
    val cliente: Cliente?,
    val vaga: Vaga?
) : Parcelable
