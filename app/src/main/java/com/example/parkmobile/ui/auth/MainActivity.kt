package com.example.parkmobile.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import com.example.parkmobile.R
import com.example.parkmobile.ui.views.HeaderView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar Firebase
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

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

        // Chamar o seeder (apenas uma vez - recomendo comentar depois)
//        val seeder = FirestoreSeeder(firebaseAuth, firestore)
//        lifecycleScope.launch {
//            seeder.seedAdminUsers()
//        }
    }
}
