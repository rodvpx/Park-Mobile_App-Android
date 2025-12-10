package com.example.parkmobile.ui.historico

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.R
import com.example.parkmobile.ui.recibo.ReciboDetalhesClienteBottomSheet

class HistoricoFragment : Fragment() {

    private lateinit var recyclerViewHistorico: RecyclerView
    private lateinit var progressBar: ProgressBar

    private val viewModel: HistoricoViewModel by viewModels { HistoricoViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_historico, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewHistorico = view.findViewById(R.id.recyclerViewHistorico)
        progressBar = view.findViewById(R.id.progressBar)

        val historicoAdapter = HistoricoAdapter { clienteVaga ->
            // ABRE O BOTTOM SHEET COM OS DETALHES DO RECIBO
            ReciboDetalhesClienteBottomSheet.newInstance(clienteVaga)
                .show(childFragmentManager, "ReciboDetalhesClienteBottomSheet")
        }
        recyclerViewHistorico.adapter = historicoAdapter

        observeViewModel(historicoAdapter)

        viewModel.carregarHistoricoCompleto()
    }

    private fun observeViewModel(adapter: HistoricoAdapter) {
        viewModel.historico.observe(viewLifecycleOwner) { historicoList ->
            adapter.submitList(historicoList)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.isVisible = isLoading
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error.isNotBlank()) {
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            }
        }
    }
}
