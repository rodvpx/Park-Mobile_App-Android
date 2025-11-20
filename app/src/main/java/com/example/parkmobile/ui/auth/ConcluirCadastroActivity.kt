package com.example.parkmobile.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import com.example.parkmobile.R
import com.example.parkmobile.ui.home.HomeCliente
import com.example.parkmobile.ui.views.HeaderView

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

        val profileImageCard: CardView = findViewById(R.id.profile_image_card)
        profileImageCard.visibility = View.GONE

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

        val btnConluir: Button = findViewById(R.id.button_concluir_cadastro)
        btnConluir.setOnClickListener {
            val intent = Intent(this, HomeCliente::class.java)
            startActivity(intent)
        }
    }
}
