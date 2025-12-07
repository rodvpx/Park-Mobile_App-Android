package com.example.parkmobile.ui.historico

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.R
import com.example.parkmobile.ui.recibo.ReciboDetalhesBottomSheet
import com.example.parkmobile.ui.relatorio.RelatorioUiState
import com.example.parkmobile.ui.relatorio.RelatorioViewModel
import com.example.parkmobile.ui.relatorio.RelatorioViewModelFactory

class ResultadoHistoricoFragment : Fragment() {

    private val viewModel: RelatorioViewModel by viewModels { RelatorioViewModelFactory() }

    private lateinit var tvTitle: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var rvHistoricoResultado: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_resultado_historico, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvTitle = view.findViewById(R.id.tv_title)
        progressBar = view.findViewById(R.id.progressBar)
        rvHistoricoResultado = view.findViewById(R.id.rv_historico_resultado)

        // O título será genérico pois não recebemos mais o nome do cliente
        tvTitle.text = "Histórico do Cliente"

        val historicoAdapter = HistoricoAdapter { clienteVaga ->
            val bottomSheet = ReciboDetalhesBottomSheet.newInstance(clienteVaga)
            bottomSheet.show(childFragmentManager, "ReciboDetalhesBottomSheet")
        }
        rvHistoricoResultado.adapter = historicoAdapter

        observeViewModel(historicoAdapter)

        // A lógica para buscar por um cliente específico precisa ser refeita
        // Por agora, vamos carregar um histórico geral ou vazio
        // viewModel.buscarHistoricoPorClienteId(args.clienteId) // Esta linha não funciona mais
    }

    private fun observeViewModel(adapter: HistoricoAdapter) {
        viewModel.historicoCliente.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            progressBar.isVisible = state is RelatorioUiState.Loading

            if (state is RelatorioUiState.Empty) {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
            }
            if (state is RelatorioUiState.Error) {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}
