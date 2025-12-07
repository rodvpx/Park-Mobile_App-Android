package com.example.parkmobile.ui.estacionamento

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.parkmobile.R
import com.google.android.material.textfield.TextInputEditText

class CheckInFragment : Fragment() {

    private lateinit var etCpfCliente: TextInputEditText
    private lateinit var etPlacaCarro: TextInputEditText
    private lateinit var etMarcaCarro: TextInputEditText
    private lateinit var etModeloCarro: TextInputEditText
    private lateinit var etCorCarro: TextInputEditText
    private lateinit var btnConfirmarCheckIn: Button
    private lateinit var progressBar: ProgressBar

    // Compartilha o ViewModel com o Fragment pai (EstacionamentoFragment)
    private val viewModel: EstacionamentoViewModel by activityViewModels {
        EstacionamentoViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_check_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etCpfCliente = view.findViewById(R.id.et_cpf_cliente)
        etPlacaCarro = view.findViewById(R.id.et_placa_carro)
        etMarcaCarro = view.findViewById(R.id.et_marca_carro)
        etModeloCarro = view.findViewById(R.id.et_modelo_carro)
        etCorCarro = view.findViewById(R.id.et_cor_carro)
        btnConfirmarCheckIn = view.findViewById(R.id.btn_confirmar_check_in)
        // Atenção: Adicione um ProgressBar com o id `progressBar` ao seu layout XML.
        // progressBar = view.findViewById(R.id.progressBar)

        btnConfirmarCheckIn.setOnClickListener {
            val cpf = etCpfCliente.text.toString()
            val placa = etPlacaCarro.text.toString()
            val marca = etMarcaCarro.text.toString()
            val modelo = etModeloCarro.text.toString()
            val cor = etCorCarro.text.toString()

            viewModel.realizarCheckIn(cpf, placa, marca, modelo, cor)
        }

        observeUiState()
    }

    private fun observeUiState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is EstacionamentoUiState.Loading -> {
                    // progressBar.visibility = View.VISIBLE
                    btnConfirmarCheckIn.isEnabled = false
                }
                is EstacionamentoUiState.Success -> {
                    // progressBar.visibility = View.GONE
                    btnConfirmarCheckIn.isEnabled = true
                    Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                    clearFields()
                }
                is EstacionamentoUiState.Error -> {
                    // progressBar.visibility = View.GONE
                    btnConfirmarCheckIn.isEnabled = true
                    Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun clearFields() {
        etCpfCliente.text?.clear()
        etPlacaCarro.text?.clear()
        etMarcaCarro.text?.clear()
        etModeloCarro.text?.clear()
        etCorCarro.text?.clear()
    }
}
