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
import com.example.parkmobile.ui.clientes.ClientesAdminFragment
import com.example.parkmobile.ui.configuracoes.ConfiguracoesActivity
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

class HomeAdmin : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbarTitle: TextView
    private lateinit var navClientes: LinearLayout
    private lateinit var navVagas: LinearLayout
    private lateinit var navEstacionamento: LinearLayout
    private lateinit var navRelatorios: LinearLayout
    private lateinit var profileImageCard: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_admin)

        // Handle system back to close drawer if open
        val backCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.closeDrawer(GravityCompat.END)
                } else {
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

        toolbarTitle = findViewById(R.id.toolbar_title)

        val toolbarInf = findViewById<LinearLayout>(R.id.toolbar_inf)
        navClientes = toolbarInf.findViewById(R.id.nav_clientes)
        navVagas = toolbarInf.findViewById(R.id.nav_vagas)
        navEstacionamento = toolbarInf.findViewById(R.id.nav_estacionamento)
        navRelatorios = toolbarInf.findViewById(R.id.nav_relatorios)

        // Header user name setup
        val header = navigationView.getHeaderView(0)
        val headerName = header.findViewById<TextView>(R.id.nav_header_name)
        headerName?.text = getString(R.string.nome_do_usuario)

        profileImageCard = findViewById(R.id.profile_image_card)
        profileImageCard.visibility = View.VISIBLE
        profileImageCard.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.END)
        }

        if(savedInstanceState == null){
            loadFragment(ClientesAdminFragment(), "Clientes")
            navigationView.setCheckedItem(R.id.nav_clientes)
        }

        // Load default fragment
        if (supportFragmentManager.findFragmentById(R.id.fragment_container) == null) {
            loadFragment(ClientesAdminFragment(), "Clientes")
        }

        navClientes.setOnClickListener { loadFragment(ClientesAdminFragment(), "Clientes") }
        navVagas.setOnClickListener {
            Snackbar.make(findViewById(R.id.main), "Vagas - Em desenvolvimento", Snackbar.LENGTH_SHORT).show()
        }
        navEstacionamento.setOnClickListener {
            Snackbar.make(findViewById(R.id.main), "Estacionamento - Em desenvolvimento", Snackbar.LENGTH_SHORT).show()
        }
        navRelatorios.setOnClickListener {
            Snackbar.make(findViewById(R.id.main), "Relatórios - Em desenvolvimento", Snackbar.LENGTH_SHORT).show()
        }
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
        navClientes.isSelected = title == "Clientes"
        navVagas.isSelected = title == "Vagas"
        navEstacionamento.isSelected = title == "Estacionamento"
        navRelatorios.isSelected = title == "Relatórios"
    }

    private fun loadFragment(fragment: Fragment, title: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commitNow()
        toolbarTitle.text = title
        updateButtonSelection(title)
    }
}
