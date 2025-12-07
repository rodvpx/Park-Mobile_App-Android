package com.example.parkmobile.ui.recibo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.R
import com.example.parkmobile.ui.historico.HistoricoAdapter
import com.example.parkmobile.ui.relatorio.RelatorioUiState
import com.example.parkmobile.ui.relatorio.RelatorioViewModel
import com.example.parkmobile.ui.relatorio.RelatorioViewModelFactory

class ConsultarReciboFragment : Fragment() {

    private val viewModel: RelatorioViewModel by viewModels { RelatorioViewModelFactory() }

    private lateinit var searchView: SearchView
    private lateinit var progressBar: ProgressBar
    private lateinit var rvRecibos: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_consultar_recibo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchView = view.findViewById(R.id.search_view)
        progressBar = view.findViewById(R.id.progress_bar)
        rvRecibos = view.findViewById(R.id.rv_recibos)

        val recibosAdapter = HistoricoAdapter { clienteVaga ->
            val bottomSheet = ReciboDetalhesBottomSheet.newInstance(clienteVaga)
            bottomSheet.show(childFragmentManager, "ReciboDetalhesBottomSheet")
        }
        rvRecibos.adapter = recibosAdapter

        setupSearch(recibosAdapter)
        observeViewModel(recibosAdapter)

        viewModel.carregarTodosOsRecibos()
    }

    private fun setupSearch(adapter: HistoricoAdapter) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                val fullList = viewModel.todosOsRecibos.value
                val query = newText.orEmpty().trim()
                if (query.isBlank()) {
                    adapter.submitList(fullList)
                } else {
                    val filteredList = fullList?.filter {
                        it.recibo.contains(query, ignoreCase = true)
                    }
                    adapter.submitList(filteredList)
                }
                return true
            }
        })
    }

    private fun observeViewModel(adapter: HistoricoAdapter) {
        viewModel.todosOsRecibos.observe(viewLifecycleOwner) {
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
