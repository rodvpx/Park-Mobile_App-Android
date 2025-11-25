package com.example.parkmobile.data.repository

import com.example.parkmobile.data.model.CheckOutItem

class CheckOutRepository {
    fun getCheckOutItems(): List<CheckOutItem> {
        return listOf(
            CheckOutItem("John Doe", "111.222.333-44", "ABC-1234", "02", "25/11/2025 10:00"),
            CheckOutItem("Jane Smith", "555.666.777-88", "XYZ-9876", "06", "25/11/2025 11:30"),
            CheckOutItem("Rodrigo Farias", "999.888.777-66", "QWE-5432", "09", "25/11/2025 12:15")
        )
    }
}
