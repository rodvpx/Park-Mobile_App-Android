package com.example.parkmobile.ui.recibo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.parkmobile.databinding.FragmentResultadoReciboBinding

class ResultadoReciboFragment : Fragment() {

    private var _binding: FragmentResultadoReciboBinding? = null
    private val binding get() = _binding!!

    // Recebendo os argumentos de forma segura com Safe Args
    private val args: ResultadoReciboFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultadoReciboBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ligando o objeto recebido diretamente ao Data Binding
        binding.clienteVaga = args.clienteVaga
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
