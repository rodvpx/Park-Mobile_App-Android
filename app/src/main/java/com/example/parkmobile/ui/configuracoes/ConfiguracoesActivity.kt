package com.example.parkmobile.ui.configuracoes

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.parkmobile.R

class ConfiguracoesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracoes)

        // Configura a Toolbar
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

        // Preenche os dados do perfil
        setupInfoItem(R.id.info_nome, "Nome", "Murilo Silva")
        setupInfoItem(R.id.info_cpf, "CPF", "455.899.554-05")
        setupInfoItem(R.id.info_email, "Email", "murilo.silva@gmail.com")
    }

    private fun setupInfoItem(viewId: Int, label: String, value: String) {
        val itemView = findViewById<View>(viewId)
        val labelView = itemView.findViewById<TextView>(R.id.label)
        val valueView = itemView.findViewById<TextView>(R.id.valor)

        labelView.text = label
        valueView.text = value
    }
}
