package com.example.parkmobile.ui.relatorio

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class RelatoriosAdminAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ConsultarReciboFragment()
            1 -> ConsultarHistoricoFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}
