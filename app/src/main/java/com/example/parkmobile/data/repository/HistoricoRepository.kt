package com.example.parkmobile.data.repository

import com.example.parkmobile.data.model.HistoricoItem

class HistoricoRepository {

    // No futuro, isso pode buscar dados de uma API ou banco de dados
    fun getHistoricoItems(): List<HistoricoItem> {
        return createMockData()
    }

    private fun createMockData(): List<HistoricoItem> {
        return listOf(
            HistoricoItem("#12345", "A-01", "10:00", "11:30", "123.456.789-00", "1h 30m", "ABC-1234", "Fiat", "Uno", "Branco", "R$ 15,00", "R$ 0,00", "R$ 15,00"),
            HistoricoItem("#67890", "B-05", "12:00", "13:00", "987.654.321-00", "1h 0m", "DEF-5678", "Chevrolet", "Onix", "Prata", "R$ 10,00", "R$ 0,00", "R$ 10,00"),
            HistoricoItem("#54321", "C-03", "14:15", "15:00", "111.222.333-44", "0h 45m", "GHI-9012", "Ford", "Ka", "Preto", "R$ 7,50", "R$ 0,00", "R$ 7,50"),
            HistoricoItem("#98765", "A-02", "16:00", "18:30", "555.666.777-88", "2h 30m", "JKL-3456", "Hyundai", "HB20", "Vermelho", "R$ 25,00", "R$ 5,00", "R$ 20,00"),
            HistoricoItem("#11223", "D-07", "09:00", "10:45", "999.888.777-66", "1h 45m", "MNO-7890", "Volkswagen", "Gol", "Azul", "R$ 17,50", "R$ 0,00", "R$ 17,50")
        )
    }
}
