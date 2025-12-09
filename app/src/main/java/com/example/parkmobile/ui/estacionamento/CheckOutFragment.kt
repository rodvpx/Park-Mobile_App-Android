package com.example.parkmobile.ui.estacionamento

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.R
import com.example.parkmobile.data.model.HistoricoEstacionamento
import java.util.Date
import java.util.Locale

class CheckOutFragment : Fragment() {

    private lateinit var rvCheckOut: RecyclerView
    private lateinit var adapter: CheckOutAdapter
    private lateinit var searchView: SearchView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmptyState: TextView

    private val viewModel: EstacionamentoViewModel by activityViewModels {
        EstacionamentoViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_check_out, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews(view)
        setupRecyclerView()
        setupSearchView()
        setupFragmentResultListener()
        observeViewModel()

        viewModel.carregarVeiculosEstacionados()
    }

    private fun bindViews(view: View) {
        rvCheckOut = view.findViewById(R.id.rv_check_out)
        searchView = view.findViewById(R.id.search_view_checkout)
        progressBar = view.findViewById(R.id.progress_bar_checkout)
        tvEmptyState = view.findViewById(R.id.tv_empty_state)
    }

    private fun setupRecyclerView() {
        rvCheckOut.layoutManager = LinearLayoutManager(context)
        adapter = CheckOutAdapter { historico ->
            CheckOutBottomSheetFragment.newInstance(historico)
                .show(childFragmentManager, CheckOutBottomSheetFragment.TAG)
        }
        rvCheckOut.adapter = adapter
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.filtrarVeiculos(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.filtrarVeiculos(newText)
                return true
            }
        })
    }

    private fun setupFragmentResultListener() {
        childFragmentManager.setFragmentResultListener(CheckOutBottomSheetFragment.REQUEST_KEY, viewLifecycleOwner) { _, bundle ->
            val historico = bundle.getParcelable<HistoricoEstacionamento>(CheckOutBottomSheetFragment.RESULT_KEY_HISTORICO)
            val desconto = bundle.getDouble(CheckOutBottomSheetFragment.RESULT_KEY_DESCONTO)
            historico?.let {
                viewModel.realizarCheckOut(it, desconto)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.veiculosEstacionadosDetalhado.observe(viewLifecycleOwner) { veiculos ->
            adapter.submitList(veiculos)
            tvEmptyState.isVisible = veiculos.isEmpty()
        }

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            progressBar.isVisible = state is EstacionamentoUiState.Loading

            when (state) {
                is EstacionamentoUiState.Success -> {
                    if (state.message.contains("Check-out")) {
                        Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is EstacionamentoUiState.Error -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.carregarVeiculosEstacionados()
    }
}
