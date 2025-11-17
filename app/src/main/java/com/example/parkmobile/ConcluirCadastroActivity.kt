package com.example.parkmobile

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ConcluirCadastroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_concluir_cadastro)

        // configura imagem de capa
        val header: HeaderView = findViewById(R.id.header_view)

        header.setHeaderText("Vai Estacionar?")
        header.setHeaderImageResource(R.color.md_theme_secondary)
        // -------------------//

        // Configura o título customizado da Toolbar
        val toolbarTitle: TextView = findViewById(R.id.toolbar_title)
        toolbarTitle.text = "Concluir Cadastro"

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