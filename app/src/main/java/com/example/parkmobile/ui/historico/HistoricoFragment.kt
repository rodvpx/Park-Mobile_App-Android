package com.example.parkmobile.ui.historico

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.parkmobile.databinding.FragmentHistoricoBinding

class HistoricoFragment : Fragment() {

    private var _binding: FragmentHistoricoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HistoricoViewModel by viewModels { HistoricoViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoricoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val historicoAdapter = HistoricoAdapter { clienteVaga ->
            // TODO: Implementar o que acontece ao clicar em um item do histórico
            Toast.makeText(context, "Recibo selecionado: ${clienteVaga.recibo}", Toast.LENGTH_SHORT).show()
        }
        binding.recyclerViewHistorico.adapter = historicoAdapter

        observeViewModel(historicoAdapter)

        viewModel.carregarHistoricoCompleto()
    }

    private fun observeViewModel(adapter: HistoricoAdapter) {
        viewModel.historico.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
