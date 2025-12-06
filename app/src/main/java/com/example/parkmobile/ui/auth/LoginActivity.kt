package com.example.parkmobile.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.parkmobile.R
import com.example.parkmobile.databinding.ActivityLoginBinding
import com.example.parkmobile.ui.HomeAdminActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by viewModels { AuthViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupListeners()
        observeAuthState()
    }

    private fun setupListeners() {
        binding.buttonLogin.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val senha = binding.editSenha.text.toString()
            viewModel.login(email, senha)
        }
    }

    private fun observeAuthState() {
        viewModel.authState.observe(this) { state ->
            when (state) {
                is AuthState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.buttonLogin.isEnabled = false
                }
                is AuthState.Authenticated -> {
                    binding.progressBar.visibility = View.GONE
                    binding.buttonLogin.isEnabled = true
                    Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                    // Navegar para a tela principal
                    startActivity(Intent(this, HomeAdminActivity::class.java))
                    finish()
                }
                is AuthState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.buttonLogin.isEnabled = true
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
