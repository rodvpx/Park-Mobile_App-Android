package com.example.parkmobile.ui.home

import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.parkmobile.R
import com.google.android.material.snackbar.Snackbar

class HomeAdmin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_admin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navClientes = findViewById<LinearLayout>(R.id.nav_clientes)
        val navVagas = findViewById<LinearLayout>(R.id.nav_vagas)
        val navEstacionamento = findViewById<LinearLayout>(R.id.nav_estacionamento)
        val navRelatorios = findViewById<LinearLayout>(R.id.nav_relatorios)

        navClientes.setOnClickListener {
            Snackbar.make(findViewById(R.id.main), "Clientes", Snackbar.LENGTH_SHORT).show()
        }

        navVagas.setOnClickListener {
            Snackbar.make(findViewById(R.id.main), "Vagas", Snackbar.LENGTH_SHORT).show()
        }

        navEstacionamento.setOnClickListener {
            Snackbar.make(findViewById(R.id.main), "Estacionamento", Snackbar.LENGTH_SHORT).show()
        }

        navRelatorios.setOnClickListener {
            Snackbar.make(findViewById(R.id.main), "Relatórios", Snackbar.LENGTH_SHORT).show()
        }
    }
}