package com.example.parkmobile.ui.relatorio

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.parkmobile.R

class RelatoriosActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_relatorios)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Relatórios"
        supportActionBar?.setDisplayShowTitleEnabled(true)
        updateToolbarNavigation()

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        supportFragmentManager.addOnBackStackChangedListener {
            updateToolbarNavigation()
        }

        if (savedInstanceState == null) {
            loadFragment(RelatoriosMainFragment(), "Relatórios", true)
        }
    }


    fun loadFragment(fragment: Fragment, title: String, isTopLevel: Boolean = false) {
        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)

        if (isTopLevel) {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        } else {
            transaction.addToBackStack(null)
        }

        transaction.commit()
        supportActionBar?.title = title
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateToolbarNavigation() {
        val hasBackStack = supportFragmentManager.backStackEntryCount > 0
        supportActionBar?.setDisplayHomeAsUpEnabled(hasBackStack)
    }
}