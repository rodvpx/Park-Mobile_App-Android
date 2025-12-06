package com.example.parkmobile.ui.estacionamento

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.parkmobile.databinding.FragmentCheckOutBinding

class CheckOutFragment : Fragment() {

    private var _binding: FragmentCheckOutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EstacionamentoViewModel by activityViewModels {
        EstacionamentoViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckOutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        binding.rvCheckOut.adapter = adapter

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
