package com.example.parkmobile.ui.estacionamento

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.parkmobile.R
import com.example.parkmobile.data.model.Cliente
import com.example.parkmobile.data.model.Vaga
import com.google.android.material.textfield.TextInputEditText

class CheckInFragment : Fragment() {

    private lateinit var actvCliente: AutoCompleteTextView
    private lateinit var actvVaga: AutoCompleteTextView
    private lateinit var etPlacaCarro: TextInputEditText
    private lateinit var etMarcaCarro: TextInputEditText
    private lateinit var etModeloCarro: TextInputEditText
    private lateinit var etCorCarro: TextInputEditText
    private lateinit var btnConfirmarCheckIn: Button
    private lateinit var progressBar: ProgressBar

    private var clientes: List<Cliente> = emptyList()
    private var vagas: List<Vaga> = emptyList()

    private var selectedCliente: Cliente? = null
    private var selectedVaga: Vaga? = null

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

        bindViews(view)
        setupAdapters()
        setupListeners()
        observeViewModel()

        viewModel.carregarClientes()
        viewModel.carregarVagasLivres()
    }

    private fun bindViews(view: View) {
        actvCliente = view.findViewById(R.id.actv_cliente)
        actvVaga = view.findViewById(R.id.actv_vaga)
        etPlacaCarro = view.findViewById(R.id.et_placa_carro)
        etMarcaCarro = view.findViewById(R.id.et_marca_carro)
        etModeloCarro = view.findViewById(R.id.et_modelo_carro)
        etCorCarro = view.findViewById(R.id.et_cor_carro)
        btnConfirmarCheckIn = view.findViewById(R.id.btn_confirmar_check_in)
        // Corrigido para o ID correto do XML
        progressBar = view.findViewById(R.id.progress_bar_check_in) 
    }

    private fun setupAdapters() {
        actvCliente.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, mutableListOf<String>()))
        actvVaga.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, mutableListOf<String>()))
    }

    private fun setupListeners() {
        actvCliente.setOnItemClickListener { _, _, position, _ ->
            selectedCliente = clientes.getOrNull(position)
        }

        actvVaga.setOnItemClickListener { _, _, position, _ ->
            selectedVaga = vagas.getOrNull(position)
        }

        btnConfirmarCheckIn.setOnClickListener {
            val placa = etPlacaCarro.text.toString()
            val marca = etMarcaCarro.text.toString()
            val modelo = etModeloCarro.text.toString()
            val cor = etCorCarro.text.toString()

            val cliente = selectedCliente
            val vaga = selectedVaga

            if (cliente == null || vaga == null) {
                Toast.makeText(context, "Selecione um cliente e uma vaga.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.realizarCheckIn(cliente.id, vaga.id, placa, marca, modelo, cor)
        }
    }

    private fun observeViewModel() {
        viewModel.clientes.observe(viewLifecycleOwner) { listaClientes ->
            clientes = listaClientes
            val nomesClientes = listaClientes.map { "${it.nome} - ${it.cpf}" }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, nomesClientes)
            actvCliente.setAdapter(adapter)
        }

        viewModel.vagasLivres.observe(viewLifecycleOwner) { listaVagas ->
            vagas = listaVagas
            val codigosVagas = listaVagas.map { it.codigo }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, codigosVagas)
            actvVaga.setAdapter(adapter)
        }

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            val isLoading = state is EstacionamentoUiState.Loading
            progressBar.isVisible = isLoading
            btnConfirmarCheckIn.isEnabled = !isLoading

            when (state) {
                is EstacionamentoUiState.Success -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                    clearFields()
                }
                is EstacionamentoUiState.Error -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }
    }

    private fun clearFields() {
        actvCliente.text.clear()
        actvVaga.text.clear()
        etPlacaCarro.text?.clear()
        etMarcaCarro.text?.clear()
        etModeloCarro.text?.clear()
        etCorCarro.text?.clear()
        selectedCliente = null
        selectedVaga = null
        actvCliente.clearFocus()
        actvVaga.clearFocus()
        viewModel.carregarVagasLivres()
    }

    override fun onResume() {
        super.onResume()
        viewModel.carregarClientes()
        viewModel.carregarVagasLivres()
    }
}
