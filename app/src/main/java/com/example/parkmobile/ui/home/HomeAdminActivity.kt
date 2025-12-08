package com.example.parkmobile.ui.home

import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.parkmobile.R
import com.google.android.material.navigation.NavigationView

class HomeAdminActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController

    // Botões da barra de navegação inferior
    private lateinit var navVagas: LinearLayout
    private lateinit var navClientes: LinearLayout
    private lateinit var navEstacionamento: LinearLayout
    private lateinit var navRelatorios: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_admin)

        setupNavigation()
        setupToolbar()
        setupDrawer()
        setupBottomNav()

        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateButtonSelection(destination.id)
        }

        updateButtonSelection(navController.currentDestination?.id ?: R.id.vagasFragment)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.closeDrawer(GravityCompat.END)
                } else {
                    // Disable this callback and let the default back press behavior handle it
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                    isEnabled = true // Re-enable for next back press
                }
            }
        })
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar_sup)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false) // Desabilita o título padrão
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun setupDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_hamburguer)


        navView.setNavigationItemSelectedListener {
            // Lógica para itens do drawer
            drawerLayout.closeDrawer(GravityCompat.END)
            true
        }
    }

    private fun setupBottomNav() {
        navVagas = findViewById(R.id.nav_vagas)
        navClientes = findViewById(R.id.nav_clientes)
        navEstacionamento = findViewById(R.id.nav_estacionamento)
        navRelatorios = findViewById(R.id.nav_relatorios)

        navVagas.setOnClickListener { if (navController.currentDestination?.id != R.id.vagasFragment) navController.navigate(R.id.vagasFragment) }
        navClientes.setOnClickListener { if (navController.currentDestination?.id != R.id.clientesAdminFragment) navController.navigate(R.id.clientesAdminFragment) }
        navEstacionamento.setOnClickListener { if (navController.currentDestination?.id != R.id.estacionamentoFragment) navController.navigate(R.id.estacionamentoFragment) }
        navRelatorios.setOnClickListener { if (navController.currentDestination?.id != R.id.relatoriosAdminFragment) navController.navigate(R.id.relatoriosAdminFragment) }
    }

    private fun updateButtonSelection(destinationId: Int) {
        navVagas.isSelected = false
        navClientes.isSelected = false
        navEstacionamento.isSelected = false
        navRelatorios.isSelected = false

        when (destinationId) {
            R.id.vagasFragment -> navVagas.isSelected = true
            R.id.clientesAdminFragment -> navClientes.isSelected = true
            R.id.estacionamentoFragment -> navEstacionamento.isSelected = true
            R.id.relatoriosAdminFragment -> navRelatorios.isSelected = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // O ícone do menu (home) abre e fecha o drawer lateral.
        if (item.itemId == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END)
            } else {
                drawerLayout.openDrawer(GravityCompat.END)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
