package com.example.parkmobile.ui.historico

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.parkmobile.databinding.FragmentResultadoHistoricoBinding
import com.example.parkmobile.ui.recibo.ReciboDetalhesBottomSheet
import com.example.parkmobile.ui.relatorio.RelatorioUiState
import com.example.parkmobile.ui.relatorio.RelatorioViewModel
import com.example.parkmobile.ui.relatorio.RelatorioViewModelFactory

class ResultadoHistoricoFragment : Fragment() {

    private var _binding: FragmentResultadoHistoricoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RelatorioViewModel by viewModels { RelatorioViewModelFactory() }
    private val args: ResultadoHistoricoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultadoHistoricoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvTitle.text = "Histórico de ${args.nomeCliente}"

        val historicoAdapter = HistoricoAdapter { clienteVaga ->
            val bottomSheet = ReciboDetalhesBottomSheet.Companion.newInstance(clienteVaga)
            bottomSheet.show(childFragmentManager, "ReciboDetalhesBottomSheet")
        }
        binding.rvHistoricoResultado.adapter = historicoAdapter

        observeViewModel(historicoAdapter)

        viewModel.buscarHistoricoPorClienteId(args.clienteId)
    }

    private fun observeViewModel(adapter: HistoricoAdapter) {
        viewModel.historicoCliente.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            binding.progressBar.isVisible = state is RelatorioUiState.Loading

            if (state is RelatorioUiState.Empty) {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
            }
            if (state is RelatorioUiState.Error) {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
