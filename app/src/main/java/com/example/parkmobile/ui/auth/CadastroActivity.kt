package com.example.parkmobile.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import com.example.parkmobile.R
import com.example.parkmobile.ui.home.HomeClienteActivity
import com.example.parkmobile.util.CpfMaskTextWatcher
import com.google.android.material.textfield.TextInputEditText

class CadastroActivity : AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModels { AuthViewModelFactory() }

    private lateinit var editNome: TextInputEditText
    private lateinit var editCpf: TextInputEditText
    private lateinit var editEmail: TextInputEditText
    private lateinit var editSenha: TextInputEditText
    private lateinit var editConfirmarSenha: TextInputEditText
    private lateinit var buttonCadastrar: Button
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        editNome = findViewById(R.id.edit_nome)
        editCpf = findViewById(R.id.edit_cpf)
        editEmail = findViewById(R.id.edit_email)
        editSenha = findViewById(R.id.edit_senha)
        editConfirmarSenha = findViewById(R.id.edit_confirmar_senha)
        buttonCadastrar = findViewById(R.id.button_cadastrar)
        progressBar = findViewById(R.id.progressBar)

        editCpf.addTextChangedListener(CpfMaskTextWatcher(editCpf))

        setupToolbar()
        setupListeners()
        observeAuthState()
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationOnClickListener {
            finish() // Fecha a activity e volta para a anterior
        }

        // Oculta o ícone de perfil
        val profileImageCard: CardView = findViewById(R.id.profile_image_card)
        profileImageCard.visibility = View.GONE

        // Define o título da tela
        val toolbarTitle: TextView = findViewById(R.id.toolbar_title)
        toolbarTitle.text = "Cadastro"
    }

    private fun setupListeners() {
        buttonCadastrar.setOnClickListener {
            val nome = editNome.text.toString().trim()
            val cpf = editCpf.text.toString().filter { it.isDigit() }
            val email = editEmail.text.toString().trim()
            val senha = editSenha.text.toString().trim()
            val confirmarSenha = editConfirmarSenha.text.toString().trim()

            if (nome.isBlank() || cpf.length != 11 || email.isBlank() || senha.isBlank()) {
                Toast.makeText(this, "Todos os campos são obrigatórios.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.cadastrar(email, senha, confirmarSenha, nome, cpf)
        }
    }

    private fun observeAuthState() {
        viewModel.authState.observe(this) { state ->
            when (state) {
                is AuthState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    buttonCadastrar.isEnabled = false
                }
                is AuthState.Authenticated -> {
                    progressBar.visibility = View.GONE
                    buttonCadastrar.isEnabled = true
                    Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                    // Navega para a tela de cliente, que é o correto
                    val intent = Intent(this, HomeClienteActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finishAffinity()
                }
                is AuthState.Error -> {
                    progressBar.visibility = View.GONE
                    buttonCadastrar.isEnabled = true
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
