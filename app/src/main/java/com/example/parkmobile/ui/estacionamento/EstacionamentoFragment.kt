package com.example.parkmobile.ui.estacionamento

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.parkmobile.databinding.FragmentEstacionamentoBinding
import com.google.android.material.tabs.TabLayoutMediator

class EstacionamentoFragment : Fragment() {

    private var _binding: FragmentEstacionamentoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEstacionamentoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = EstacionamentoPagerAdapter(this)
        binding.viewPagerEstacionamento.adapter = adapter

        TabLayoutMediator(binding.tabLayoutEstacionamento, binding.viewPagerEstacionamento) { tab, position ->
            tab.text = when (position) {
                0 -> "Check-in"
                1 -> "Check-out"
                else -> null
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
