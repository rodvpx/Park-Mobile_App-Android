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
import com.example.parkmobile.ui.views.HeaderView

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // configura imagem de capa
        val header: HeaderView = findViewById(R.id.header_view)

        header.setHeaderText("Park Mobile")
        header.setHeaderImageResource(R.drawable.capa_home)
        // -------------------//

        // Configura o título customizado da Toolbar
        val toolbarTitle: TextView = findViewById(R.id.toolbar_title)
        toolbarTitle.text = "Login"

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

        val btnEntrar: Button = findViewById(R.id.button_login)
        btnEntrar.setOnClickListener {
            val intent = Intent(this, ConcluirCadastroActivity::class.java)
            startActivity(intent)
        }
    }
}
