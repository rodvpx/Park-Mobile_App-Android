package com.example.parkmobile.ui.estacionamento

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.parkmobile.databinding.FragmentCheckInBinding

class CheckInFragment : Fragment() {

    private var _binding: FragmentCheckInBinding? = null
    private val binding get() = _binding!!

    // Compartilha o ViewModel com o Fragment pai (EstacionamentoFragment)
    private val viewModel: EstacionamentoViewModel by activityViewModels {
        EstacionamentoViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.btnConfirmarCheckIn.setOnClickListener {
            val cpf = binding.etCpfCliente.text.toString()
            val placa = binding.etPlacaCarro.text.toString()
            val marca = binding.etMarcaCarro.text.toString()
            val modelo = binding.etModeloCarro.text.toString()
            val cor = binding.etCorCarro.text.toString()

            viewModel.realizarCheckIn(cpf, placa, marca, modelo, cor)
        }

        observeUiState()
    }

    private fun observeUiState() {
        val progressBar = view?.findViewById<ProgressBar>(R.id.progressBar) // Adicione um ProgressBar ao seu layout se desejar

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is EstacionamentoUiState.Loading -> {
                    progressBar?.visibility = View.VISIBLE
                    // Desabilitar botão para evitar cliques duplos
                    binding.btnConfirmarCheckIn.isEnabled = false
                }
                is EstacionamentoUiState.Success -> {
                    progressBar?.visibility = View.GONE
                    binding.btnConfirmarCheckIn.isEnabled = true
                    Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                    // Limpar campos ou navegar para outra tela
                    clearFields()
                }
                is EstacionamentoUiState.Error -> {
                    progressBar?.visibility = View.GONE
                    binding.btnConfirmarCheckIn.isEnabled = true
                    Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun clearFields() {
        binding.etCpfCliente.text?.clear()
        binding.etPlacaCarro.text?.clear()
        binding.etMarcaCarro.text?.clear()
        binding.etModeloCarro.text?.clear()
        binding.etCorCarro.text?.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
