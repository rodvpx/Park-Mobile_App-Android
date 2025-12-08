package com.example.parkmobile.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.example.parkmobile.R
import com.example.parkmobile.data.FirestoreSeeder
import com.example.parkmobile.ui.views.HeaderView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // --- Configuração da Toolbar ---
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar) // Define como a action bar principal
        supportActionBar?.setDisplayShowTitleEnabled(false) // Desabilita o título padrão

        // oculta o icon de profile na toolbar
        val profileImageCard: CardView = findViewById(R.id.profile_image_card)
        profileImageCard.visibility = View.GONE

        // Define o título customizado
        val toolbarTitle: TextView = findViewById(R.id.toolbar_title)
        toolbarTitle.text = "Park Mobile"
        // --- Fim da configuração da Toolbar ---

        // configura imagem de capa
        val header: HeaderView = findViewById(R.id.header_view)

        header.setHeaderText("Park Mobile")
        header.setHeaderImageResource(R.drawable.capa_home)

        val btnLogin = findViewById<Button>(R.id.bt_entrar)
        btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val btnCadastro = findViewById<Button>(R.id.bt_cadastrar)
        btnCadastro.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
        }

        // Verifica e popula o banco de dados se necessário
        lifecycleScope.launch {
            try {
                Log.d("MainActivity", "Verificando e populando o banco de dados se necessário...")
                FirestoreSeeder.seedDatabase()
            } catch (e: Exception) {
                Log.e("MainActivity", "Ocorreu um erro durante o processo de seed automático.", e)
            }
        }
    }
}
