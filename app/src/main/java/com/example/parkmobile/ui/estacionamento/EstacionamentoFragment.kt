package com.example.parkmobile.ui.estacionamento

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.parkmobile.R
import com.example.parkmobile.ui.relatorio.ConsultarHistoricoFragment
import com.example.parkmobile.ui.relatorio.ConsultarReciboFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class EstacionamentoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_estacionamento, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout_estacionamento)
        val viewPager = view.findViewById<ViewPager2>(R.id.view_pager_estacionamento)

        val adapter = EstacionamentoPagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Check-in"
                1 -> "Check-out"
                else -> null
            }
        }.attach()
    }

    private inner class EstacionamentoPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> CheckInFragment()
                1 -> CheckOutFragment()
                else -> throw IllegalStateException("Posição de aba inválida")
            }
        }
    }
}
