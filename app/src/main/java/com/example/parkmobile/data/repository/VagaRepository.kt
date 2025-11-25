package com.example.parkmobile.data.repository

import com.example.parkmobile.data.model.Vaga

class VagaRepository {

    fun getVagas(): List<Vaga> {
        return listOf(
            Vaga(1, "Vaga 01", "disponivel"),
            Vaga(2, "Vaga 02", "ocupada"),
            Vaga(3, "Vaga 03", "disponivel"),
            Vaga(4, "Vaga 04", "disponivel"),
            Vaga(5, "Vaga 05", "disponivel"),
            Vaga(6, "Vaga 06", "ocupada"),
            Vaga(7, "Vaga 07", "disponivel"),
            Vaga(8, "Vaga 08", "ocupada"),
            Vaga(9, "Vaga 09", "ocupada"),
            Vaga(10, "Vaga 10", "disponivel")
        )
    }
}
