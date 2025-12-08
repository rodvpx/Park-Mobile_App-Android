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
import androidx.fragment.app.Fragment
import com.example.parkmobile.ui.auth.MainActivity
import com.example.parkmobile.R
import com.example.parkmobile.ui.configuracoes.ConfiguracoesActivity
import com.example.parkmobile.ui.historico.HistoricoFragment
import com.example.parkmobile.ui.relatorio.RelatorioFragment
import com.google.firebase.auth.FirebaseAuth

class HomeClienteActivity : AppCompatActivity() {

    private lateinit var toolbarTitle: TextView
    private lateinit var historicoButton: LinearLayout
    private lateinit var relatoriosButton: LinearLayout
    private lateinit var profileImageCard: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_cliente)

        val toolbar: Toolbar = findViewById(R.id.toolbar_sup)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        profileImageCard = findViewById(R.id.profile_image_card)
        profileImageCard.visibility = View.VISIBLE
        profileImageCard.setOnClickListener { view ->
            showProfileMenu(view)
        }

        toolbarTitle = findViewById(R.id.toolbar_title)

        // Encontra a toolbar inferior e seus botões
        val toolbarInf = findViewById<LinearLayout>(R.id.toolbar_inf)
        historicoButton = toolbarInf.findViewById<LinearLayout>(R.id.bottom_toolbar_history)
        relatoriosButton = toolbarInf.findViewById<LinearLayout>(R.id.bottom_toolbar_reports)

        if (savedInstanceState == null) {
            loadFragment(HistoricoFragment(), "Histórico")
        }

        historicoButton.setOnClickListener { loadFragment(HistoricoFragment(), "Histórico") }
        relatoriosButton.setOnClickListener { loadFragment(RelatorioFragment(), "Relatórios") }
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
}
