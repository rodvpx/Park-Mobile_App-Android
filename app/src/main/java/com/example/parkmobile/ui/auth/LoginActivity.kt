package com.example.parkmobile.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.parkmobile.R
import com.example.parkmobile.ui.home.HomeAdminActivity
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModels { AuthViewModelFactory() }

    private lateinit var editEmail: TextInputEditText
    private lateinit var editSenha: TextInputEditText
    private lateinit var buttonLogin: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editEmail = findViewById(R.id.edit_email)
        editSenha = findViewById(R.id.edit_senha)
        buttonLogin = findViewById(R.id.button_login)
        progressBar = findViewById(R.id.progressBar)

        setupListeners()
        observeAuthState()
    }

    private fun setupListeners() {
        buttonLogin.setOnClickListener {
            val email = editEmail.text.toString().trim()
            val senha = editSenha.text.toString().trim()
            viewModel.login(email, senha)
        }
    }

    private fun observeAuthState() {
        viewModel.authState.observe(this) { state ->
            when (state) {
                is AuthState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    buttonLogin.isEnabled = false
                }
                is AuthState.Authenticated -> {
                    progressBar.visibility = View.GONE
                    buttonLogin.isEnabled = true
                    Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                    // Navegar para a tela principal
                    startActivity(Intent(this, HomeAdminActivity::class.java))
                    finishAffinity() // Fecha todas as activities da pilha
                }
                is AuthState.Error -> {
                    progressBar.visibility = View.GONE
                    buttonLogin.isEnabled = true
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }
    }
}
