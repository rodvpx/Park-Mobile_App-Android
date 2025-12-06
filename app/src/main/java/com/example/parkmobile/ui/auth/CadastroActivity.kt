package com.example.parkmobile.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.parkmobile.databinding.ActivityCadastroBinding
import com.example.parkmobile.ui.home.HomeAdminActivity

class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding
    private val viewModel: AuthViewModel by viewModels { AuthViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        observeAuthState()
    }

    private fun setupListeners() {
        binding.buttonCadastrar.setOnClickListener {
            val nome = binding.editNome.text.toString()
            val cpf = binding.editCpf.text.toString()
            val email = binding.editEmail.text.toString()
            val senha = binding.editSenha.text.toString()
            val confirmarSenha = binding.editConfirmarSenha.text.toString()

            viewModel.cadastrar(email, senha, confirmarSenha, nome, cpf)
        }
    }

    private fun observeAuthState() {
        viewModel.authState.observe(this) { state ->
            when (state) {
                is AuthState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.buttonCadastrar.isEnabled = false
                }
                is AuthState.Authenticated -> {
                    binding.progressBar.visibility = View.GONE
                    binding.buttonCadastrar.isEnabled = true
                    Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                    // Navegar para a tela principal
                    startActivity(Intent(this, HomeAdminActivity::class.java))
                    finish()
                }
                is AuthState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.buttonCadastrar.isEnabled = true
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }
    }
}
