package com.example.parkmobile.data.repository

import com.example.parkmobile.data.model.Cliente

class ClienteRepository {

    fun getClientesItems(): List<Cliente> {
        return createMockData()
    }

    private fun createMockData(): List<Cliente> {
        return listOf(
            Cliente(1, "João Silva", "123.456.789-00"),
            Cliente(2, "Maria Santos", "987.654.321-00"),
            Cliente(3, "Pedro Oliveira", "111.222.333-44"),
            Cliente(4, "Ana Souza", "555.666.777-88"),
            Cliente(5, "Carlos Pereira", "999.888.777-66"),
            Cliente(6, "Laura Mendes", "444.555.666-77"),
            Cliente(7, "Fernando Costa", "222.333.444-55"),
            Cliente(8, "Isabela Santos", "777.888.999-00"),
            Cliente(9, "Rafael Lima", "333.444.555-66"),
            Cliente(10, "Julia Almeida", "666.777.888-99")
        )
    }
}