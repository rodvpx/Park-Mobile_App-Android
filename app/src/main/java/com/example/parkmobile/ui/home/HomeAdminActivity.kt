package com.example.parkmobile.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.parkmobile.R
import com.example.parkmobile.ui.configuracoes.ConfiguracoesActivity
import com.google.android.material.navigation.NavigationView

class HomeAdminActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbarTitle: TextView
    private lateinit var navClientes: LinearLayout
    private lateinit var navVagas: LinearLayout
    private lateinit var navEstacionamento: LinearLayout
    private lateinit var navRelatorios: LinearLayout
    private lateinit var profileImageCard: CardView
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_admin)

        val toolbar: Toolbar = findViewById(R.id.toolbar_sup)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Define os destinos de nível superior para o ícone do menu (não mostrará a seta para trás)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.clientesAdminFragment, R.id.vagasFragment, R.id.estacionamentoFragment, R.id.relatoriosAdminFragment),
            drawerLayout
        )

        // Configura a action bar para funcionar com o NavController
        setupActionBarWithNavController(navController, appBarConfiguration)

        toolbarTitle = findViewById(R.id.toolbar_title)

        // Listener para atualizar título e seleção de botões
        navController.addOnDestinationChangedListener { _, destination, _ ->
            toolbarTitle.text = destination.label
            updateButtonSelection(destination.id)
        }

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

        setupBottomNavListeners()
    }

    private fun setupBottomNavListeners() {
        navClientes.setOnClickListener { navController.navigate(R.id.clientesAdminFragment) }
        navVagas.setOnClickListener { navController.navigate(R.id.vagasFragment) }
        navEstacionamento.setOnClickListener { navController.navigate(R.id.estacionamentoFragment) }
        navRelatorios.setOnClickListener { navController.navigate(R.id.relatoriosAdminFragment) }
    }

    override fun onSupportNavigateUp(): Boolean {
        // Permite que o NavController gerencie o botão "para cima" (back)
        return navController.navigateUp() || super.onSupportNavigateUp()
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

    private fun updateButtonSelection(destinationId: Int) {
        navClientes.isSelected = destinationId == R.id.clientesAdminFragment
        navVagas.isSelected = destinationId == R.id.vagasFragment
        navEstacionamento.isSelected = destinationId == R.id.estacionamentoFragment
        navRelatorios.isSelected = destinationId == R.id.relatoriosAdminFragment
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END)
        } else {
            super.onBackPressed()
        }
    }
}
