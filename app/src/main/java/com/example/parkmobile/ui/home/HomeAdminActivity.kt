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
import androidx.fragment.app.FragmentManager
import com.example.parkmobile.R
import com.example.parkmobile.ui.clientes.ClientesAdminFragment
import com.example.parkmobile.ui.configuracoes.ConfiguracoesActivity
import com.example.parkmobile.ui.estacionamento.EstacionamentoFragment
import com.example.parkmobile.ui.relatorio.RelatoriosActivity
import com.example.parkmobile.ui.vagas.VagasFragment
import com.google.android.material.navigation.NavigationView

class HomeAdminActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

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

        val toolbar: Toolbar = findViewById(R.id.toolbar_sup)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Listener for the toolbar's navigation icon (back arrow)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        // Update the toolbar whenever the back stack changes
        supportFragmentManager.addOnBackStackChangedListener { updateToolbarNavigation() }

        // Handle system back press
        val backCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.closeDrawer(GravityCompat.END)
                } else {
                    // If the back stack is not empty, let the default behavior (pop back stack) happen
                    if (supportFragmentManager.backStackEntryCount > 0) {
                        isEnabled = false
                        onBackPressedDispatcher.onBackPressed()
                        isEnabled = true
                    } else {
                        // Handle or disable back press on top-level fragments if needed, otherwise finish
                        finish()
                    }
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, backCallback)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        toolbarTitle = findViewById(R.id.toolbar_title)

        val toolbarInf = findViewById<LinearLayout>(R.id.toolbar_inf)
        navClientes = toolbarInf.findViewById(R.id.nav_clientes)
        navVagas = toolbarInf.findViewById(R.id.nav_vagas)
        navEstacionamento = toolbarInf.findViewById(R.id.nav_estacionamento)
        navRelatorios = toolbarInf.findViewById(R.id.nav_relatorios)

        val header = navigationView.getHeaderView(0)
        val headerName = header.findViewById<TextView>(R.id.nav_header_name)
        headerName?.text = getString(R.string.nome_do_usuario)

        profileImageCard = findViewById(R.id.profile_image_card)
        profileImageCard.setOnClickListener { drawerLayout.openDrawer(GravityCompat.END) }

        if (savedInstanceState == null) {
            loadFragment(ClientesAdminFragment(), "Clientes", true)
        }

        navClientes.setOnClickListener { loadFragment(ClientesAdminFragment(), "Clientes", true) }
        navVagas.setOnClickListener { loadFragment(VagasFragment(), "Vagas", true) }
        navEstacionamento.setOnClickListener { loadFragment(EstacionamentoFragment(), "Estacionamento", true) }
        navRelatorios.setOnClickListener {
            startActivity(Intent(this, RelatoriosActivity::class.java))
        }

        updateToolbarNavigation() // Set initial state
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_configuracoes -> {
                startActivity(Intent(this, ConfiguracoesActivity::class.java))
            }
            R.id.nav_sair -> {
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

    private fun loadFragment(fragment: Fragment, title: String, isTopLevel: Boolean) {
        if (isTopLevel) {
            // Clear the entire back stack for top-level destinations
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)

        if (!isTopLevel) {
            transaction.addToBackStack(null) // Add to back stack only if it's not a top-level destination
        }
        
        transaction.commit()
        toolbarTitle.text = title
        updateButtonSelection(title)
    }

    private fun updateToolbarNavigation() {
        val hasBackStack = supportFragmentManager.backStackEntryCount > 0
        supportActionBar?.setDisplayHomeAsUpEnabled(hasBackStack)
    }
}
