package com.example.parkmobile.ui.auth

import android.os.Bundle
import android.view.Menu
import androidx.activity.enableEdgeToEdge
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.parkmobile.R

class CadastroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cadastro)

        // Configura o título customizado da Toolbar
        val toolbarTitle: TextView = findViewById(R.id.toolbar_title)
        toolbarTitle.text = "Cadastro"

        // Configura a Toolbar para ter o botão de voltar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        // Define a ação do clique no botão de voltar
        toolbar.setNavigationOnClickListener {
            finish() // <--- Ação para voltar
        }
    }
}
