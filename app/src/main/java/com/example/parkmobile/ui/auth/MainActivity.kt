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

    // ✅ Função 1: VERIFICAÇÃO (executa no onCreate - SEM DADOS)
    private fun verificarBanco() {
        lifecycleScope.launch {
            try {
                Log.d("MainActivity", "🔍 Verificando banco de dados...")
                FirestoreSeeder.seedDatabase() // VERIFICA se já tem dados (NÃO popula)
            } catch (e: Exception) {
                Log.e("MainActivity", "Erro na verificação", e)
            }
        }
    }

    // ✅ Função 2: POPULAR (descomente 1x para popular, depois comente)
    /*
    private fun popularBanco() {
        lifecycleScope.launch {
            try {
                Log.d("MainActivity", "🚀 POPULANDO banco de dados...")
                FirestoreSeeder.seedDatabase(force = false) // Popula só se estiver vazio
            } catch (e: Exception) {
                Log.e("MainActivity", "Erro no seed", e)
            }
        }
    }
    */

    // ✅ Função 3: FORÇAR RECRIAÇÃO (descomente só para resetar TUDO)
    /*
    private fun forcarRecriacao() {
        lifecycleScope.launch {
            try {
                Log.d("MainActivity", "💥 FORÇANDO recriação completa...")
                FirestoreSeeder.seedDatabase(force = true) // LIMPA TUDO e recria
            } catch (e: Exception) {
                Log.e("MainActivity", "Erro no force seed", e)
            }
        }
    }
    */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // --- Configuração da Toolbar ---
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val profileImageCard: CardView = findViewById(R.id.profile_image_card)
        profileImageCard.visibility = View.GONE

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

        // ✅ CHAMADA DA VERIFICAÇÃO (executa sempre, só verifica)
        verificarBanco()

        // forcarRecriacao() // 👈 DESCOMENTE só para resetar TUDO depois popule

        // popularBanco()  // 👈 DESCOMENTE 1x para popular, depois COMENTE

    }
}
