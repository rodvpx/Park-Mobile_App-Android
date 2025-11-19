package com.example.parkmobile.ui.home

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.parkmobile.R
import com.example.parkmobile.ui.historico.HistoricoFragment

class HomeCliente : AppCompatActivity() {

    private lateinit var toolbarTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_cliente)

        toolbarTitle = findViewById(R.id.toolbar_title)

        // Configura a Toolbar para ter o botão de voltar
        val toolbar: Toolbar = findViewById(R.id.toolbar_sup)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        toolbar.setNavigationOnClickListener { finish() }

        // Carrega o fragmento inicial (Histórico) se for a primeira criação
        if (savedInstanceState == null) {
            loadFragment(HistoricoFragment(), "Histórico")
        }

        // Encontra a toolbar inferior e seus botões
        val toolbarInf = findViewById<LinearLayout>(R.id.toolbar_inf)
        val historicoButton = toolbarInf.findViewById<LinearLayout>(R.id.bottom_toolbar_history)
        val relatoriosButton = toolbarInf.findViewById<LinearLayout>(R.id.bottom_toolbar_reports)

        // Configura os cliques nos botões da toolbar inferior
        historicoButton.setOnClickListener {
            loadFragment(HistoricoFragment(), "Histórico")
        }

        relatoriosButton.setOnClickListener {
            // TODO: Substituir por RelatoriosFragment quando ele for criado
            // loadFragment(RelatoriosFragment(), "Relatórios")
            // Por enquanto, podemos carregar um fragmento em branco ou manter o histórico
            // loadFragment(Fragment(), "Relatórios") // Exemplo com fragmento vazio
        }
    }

    private fun loadFragment(fragment: Fragment, title: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
        toolbarTitle.text = title
    }
}
