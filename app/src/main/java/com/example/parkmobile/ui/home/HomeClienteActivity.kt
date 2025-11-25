package com.example.parkmobile.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.parkmobile.R
import com.example.parkmobile.ui.configuracoes.ConfiguracoesActivity
import com.example.parkmobile.ui.historico.HistoricoFragment
import com.example.parkmobile.ui.relatorio.RelatorioFragment
import com.google.android.material.navigation.NavigationView

class HomeClienteActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbarTitle: TextView
    private lateinit var historicoButton: LinearLayout
    private lateinit var relatoriosButton: LinearLayout
    private lateinit var profileImageCard: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_cliente)

        // Handle system back (including gesture) to close drawer if it's open
        val backCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.closeDrawer(GravityCompat.END)
                } else {
                    // disable and let system handle back
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                    isEnabled = true
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, backCallback)

        val toolbar: Toolbar = findViewById(R.id.toolbar_sup)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        // Acessa a view de header do NavigationView e configura clique em "Configurar Conta"
        val header = navigationView.getHeaderView(0)
        // atualiza o nome do header a partir de resources (permite tradução)
        val headerName = header.findViewById<TextView>(R.id.nav_header_name)
        headerName?.text = getString(R.string.nome_do_usuario)
        // Botão de voltar (seta) não é necessário quando se tem o menu lateral
        // Ação de abrir o menu será no ícone de perfil
        profileImageCard = findViewById(R.id.profile_image_card)
        profileImageCard.visibility = View.VISIBLE
        profileImageCard.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.END)
        }

        toolbarTitle = findViewById(R.id.toolbar_title)

        // Encontra a toolbar inferior e seus botões
        val toolbarInf = findViewById<LinearLayout>(R.id.toolbar_inf)
        historicoButton = toolbarInf.findViewById<LinearLayout>(R.id.bottom_toolbar_history)
        relatoriosButton = toolbarInf.findViewById<LinearLayout>(R.id.bottom_toolbar_reports)

        if (savedInstanceState == null) {
            loadFragment(HistoricoFragment(), "Histórico")
            navigationView.setCheckedItem(R.id.nav_configuracoes) // Marcar item como selecionado
        }

        historicoButton.setOnClickListener { loadFragment(HistoricoFragment(), "Histórico") }
        relatoriosButton.setOnClickListener { loadFragment(RelatorioFragment(), "Relatórios") }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_configuracoes -> {
                val intent = Intent(this, ConfiguracoesActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_sair -> {
                // Aqui você pode adicionar a lógica para fazer logout
                Toast.makeText(this, "Saindo...", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.END)
        return true
    }

    private fun updateButtonSelection(title: String) {
        historicoButton.isSelected = title == "Histórico"
        relatoriosButton.isSelected = title == "Relatórios"
    }

    private fun loadFragment(fragment: Fragment, title: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
        toolbarTitle.text = title
        updateButtonSelection(title)
    }

    // back is handled via OnBackPressedCallback registered in onCreate

}
