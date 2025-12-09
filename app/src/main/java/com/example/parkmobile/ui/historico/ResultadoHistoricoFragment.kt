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
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.R
import com.example.parkmobile.ui.recibo.ReciboDetalhesBottomSheet
import com.example.parkmobile.ui.recibo.RecibosAdapter
import com.example.parkmobile.ui.relatorio.RelatorioUiState
import com.example.parkmobile.ui.relatorio.RelatorioViewModel
import com.example.parkmobile.ui.relatorio.RelatorioViewModelFactory

class ResultadoHistoricoFragment : Fragment() {

    private val viewModel: RelatorioViewModel by activityViewModels { RelatorioViewModelFactory() }

    private lateinit var tvNomeCliente: TextView
    private lateinit var rvHistoricoCliente: RecyclerView
    private lateinit var progressBar: ProgressBar

    private var clienteId: String? = null
    private var clienteNome: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            clienteId = it.getString("clienteId")
            clienteNome = it.getString("clienteNome")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_resultado_historico, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvNomeCliente = view.findViewById(R.id.tv_nome_cliente_historico)
        rvHistoricoCliente = view.findViewById(R.id.rv_historico_cliente)
        progressBar = view.findViewById(R.id.progress_bar_historico)
        rvHistoricoCliente.layoutManager = LinearLayoutManager(requireContext())

        tvNomeCliente.text = "Histórico de ${clienteNome ?: "Cliente"}"

        val recibosAdapter = RecibosAdapter { historico ->
            val bottomSheet = ReciboDetalhesBottomSheet.newInstance(historico)
            bottomSheet.show(childFragmentManager, "ReciboDetalhesBottomSheet")
        }
        rvHistoricoCliente.adapter = recibosAdapter

        observeViewModel(recibosAdapter)

        clienteId?.let {
            viewModel.buscarHistoricoPorClienteId(it)
        }
    }

    private fun observeViewModel(adapter: RecibosAdapter) {
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
