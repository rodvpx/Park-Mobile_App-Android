package com.example.parkmobile.ui.estacionamento

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class EstacionamentoPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CheckInFragment()
            1 -> CheckOutFragment()
            else -> throw IllegalStateException("Posição de aba inválida")
        }
    }
}
