package com.example.parkmobile.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.parkmobile.ui.auth.MainActivity
import com.example.parkmobile.R
import com.example.parkmobile.ui.configuracoes.ConfiguracoesActivity
import com.google.firebase.auth.FirebaseAuth

class HomeAdminActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var toolbarTitle: TextView

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
        setupBottomNav()

        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateScreenForDestination(destination.id)
        }

        // Garante que o estado inicial esteja correto
        updateScreenForDestination(navController.currentDestination?.id ?: R.id.vagasFragment)
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar_sup)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false) // Desabilita o título padrão

        toolbarTitle = findViewById(R.id.toolbar_title)

        val profileImageCard: CardView = findViewById(R.id.profile_image_card)
        profileImageCard.visibility = View.VISIBLE
        profileImageCard.setOnClickListener { view ->
            showProfileMenu(view)
        }
    }

    private fun showProfileMenu(anchor: View) {
        val popup = PopupMenu(this, anchor)
        popup.menuInflater.inflate(R.menu.profile_menu, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_config -> {
                    startActivity(Intent(this, ConfiguracoesActivity::class.java))
                    true
                }
                R.id.menu_sair -> {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
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

    private fun updateScreenForDestination(destinationId: Int) {
        // Atualiza o título
        toolbarTitle.text = when (destinationId) {
            R.id.vagasFragment -> "Vagas"
            R.id.clientesAdminFragment -> "Clientes"
            R.id.estacionamentoFragment -> "Estacionamento"
            R.id.relatoriosAdminFragment -> "Relatórios"
            else -> ""
        }

        // Atualiza a seleção do botão
        navVagas.isSelected = destinationId == R.id.vagasFragment
        navClientes.isSelected = destinationId == R.id.clientesAdminFragment
        navEstacionamento.isSelected = destinationId == R.id.estacionamentoFragment
        navRelatorios.isSelected = destinationId == R.id.relatoriosAdminFragment
    }
}
