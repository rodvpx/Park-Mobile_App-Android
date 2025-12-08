package com.example.parkmobile.ui.estacionamento

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.R
import java.util.Date
import java.util.Locale

class CheckOutFragment : Fragment() {

    private lateinit var rvCheckOut: RecyclerView
    private lateinit var adapter: EstacionamentoAdapter // Corrigido para EstacionamentoAdapter

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

        setupRecyclerView(view)
        observeViewModel()

        viewModel.carregarVeiculosEstacionados()
    }

    private fun setupRecyclerView(view: View) {
        rvCheckOut = view.findViewById(R.id.rv_check_out)
        rvCheckOut.layoutManager = LinearLayoutManager(context)

        // Corrigido para EstacionamentoAdapter
        adapter = EstacionamentoAdapter { historico -> 
            val checkInTime = historico.checkIn
            if (checkInTime == null) {
                Toast.makeText(context, "Erro: Horário de check-in não encontrado.", Toast.LENGTH_SHORT).show()
                return@EstacionamentoAdapter
            }

            val valorCalculado = viewModel.calcularValor(checkInTime, Date())
            val valorFormatado = String.format(Locale.getDefault(), "%.2f", valorCalculado)

            AlertDialog.Builder(requireContext())
                .setTitle("Confirmar Check-out")
                .setMessage("Veículo: ${historico.placaVeiculo.uppercase()}\nValor a pagar: R$ $valorFormatado\n\nDeseja confirmar o check-out?")
                .setPositiveButton("Confirmar") { _, _ ->
                    viewModel.realizarCheckOut(historico)
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
        rvCheckOut.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.veiculosEstacionados.observe(viewLifecycleOwner) { veiculos ->
            adapter.submitList(veiculos)
        }

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is EstacionamentoUiState.Success -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                }
                is EstacionamentoUiState.Error -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                }
                is EstacionamentoUiState.Loading -> {
                    // Lidar com o estado de carregamento, se necessário
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.carregarVeiculosEstacionados()
    }
}
