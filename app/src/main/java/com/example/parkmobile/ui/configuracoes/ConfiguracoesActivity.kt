package com.example.parkmobile.ui.configuracoes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.parkmobile.ui.auth.MainActivity
import com.example.parkmobile.R
import com.google.android.material.textfield.TextInputEditText

class ConfiguracoesActivity : AppCompatActivity() {

    private val viewModel: ConfiguracoesViewModel by viewModels { ConfiguracoesViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracoes)

        setupToolbar()
        setupListeners()
        observeViewModel()

        viewModel.loadUserProfile()
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar_sup)
        val toolbarTitle: TextView = toolbar.findViewById(R.id.toolbar_title)
        toolbarTitle.text = "Perfil"

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupListeners() {
        findViewById<Button>(R.id.btn_alterar_senha).setOnClickListener {
            val current = findViewById<TextInputEditText>(R.id.et_senha_atual).text.toString()
            val new = findViewById<TextInputEditText>(R.id.et_nova_senha).text.toString()
            val confirm = findViewById<TextInputEditText>(R.id.et_confirmar_senha).text.toString()
            viewModel.changePassword(current, new, confirm)
        }

        findViewById<Button>(R.id.btn_sair).setOnClickListener {
            viewModel.logout()
        }
    }

    private fun observeViewModel() {
        viewModel.state.observe(this) { state ->
            when (state) {
                is ConfiguracoesState.Loading -> {
                    // Show loading indicator
                }
                is ConfiguracoesState.ProfileLoaded -> {
                    val tvValorNome: TextView = findViewById(R.id.tv_valor_nome)
                    val tvValorCpf: TextView = findViewById(R.id.tv_valor_cpf)
                    val tvValorEmail: TextView = findViewById(R.id.tv_valor_email)
                    val tvLabelNome: TextView = findViewById(R.id.tv_label_nome)
                    val tvLabelCpf: TextView = findViewById(R.id.tv_label_cpf)
                    val tvLabelEmail: TextView = findViewById(R.id.tv_label_email)

                    if (state.profile.nome == "Administrador") {
                        tvLabelNome.visibility = View.GONE
                        tvValorNome.visibility = View.GONE
                        tvLabelCpf.visibility = View.GONE
                        tvValorCpf.visibility = View.GONE
                        tvLabelEmail.text = getString(R.string.usuario)
                        tvValorEmail.text = state.profile.email
                    } else {
                        tvValorNome.text = state.profile.nome
                        tvValorCpf.text = formatarCpf(state.profile.cpf)
                        tvValorEmail.text = state.profile.email
                    }
                }
                is ConfiguracoesState.PasswordChanged -> {
                    Toast.makeText(this, "Senha alterada com sucesso!", Toast.LENGTH_SHORT).show()
                }
                is ConfiguracoesState.LoggedOut -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                is ConfiguracoesState.Error -> {
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun formatarCpf(cpf: String): String {
        return if (cpf.length == 11) {
            cpf.replaceFirst(Regex("(\\d{3})(\\d{3})(\\d{3})(\\d{2})"), "$1.$2.$3-$4")
        } else {
            cpf
        }
    }
}
