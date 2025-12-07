package com.example.parkmobile.ui.estacionamento

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.R

class CheckOutFragment : Fragment() {

    private lateinit var rvCheckOut: RecyclerView

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

        rvCheckOut = view.findViewById(R.id.rv_check_out)

        val adapter = EstacionamentoAdapter { clienteVaga ->
            // Confirmar antes de fazer o check-out
            AlertDialog.Builder(requireContext())
                .setTitle("Confirmar Check-out")
                .setMessage("Deseja realmente fazer o check-out do veículo com placa ${clienteVaga.placa}?")
                .setPositiveButton("Sim") { _, _ ->
                    viewModel.realizarCheckOut(clienteVaga.recibo)
                }
                .setNegativeButton("Não", null)
                .show()
        }

        rvCheckOut.adapter = adapter

        // O ViewModel precisa de um LiveData para expor a lista de veículos estacionados
        viewModel.veiculosEstacionados.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is EstacionamentoUiState.Success -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                    // A lista será atualizada automaticamente porque estamos observando `veiculosEstacionados`
                }
                is EstacionamentoUiState.Error -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                }
                is EstacionamentoUiState.Loading -> {
                    // Pode-se mostrar um indicador de loading específico para o item clicado
                }
            }
        }

        // Carregar a lista inicial
        viewModel.carregarVeiculosEstacionados()
    }
}
