package com.example.parkmobile.ui.estacionamento

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.parkmobile.R
import com.example.parkmobile.data.repository.ClienteRepository
import com.example.parkmobile.data.repository.VagaRepository

class CheckInFragment : Fragment() {

    private lateinit var clienteRepository: ClienteRepository
    private lateinit var vagaRepository: VagaRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_check_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clienteRepository = ClienteRepository()
        vagaRepository = VagaRepository()

        // Configurar a lista de clientes
        val clientes = clienteRepository.getClientesItems().map { it.nome }
        val clienteAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, clientes)
        val actvSelecionarCliente = view.findViewById<AutoCompleteTextView>(R.id.actv_selecionar_cliente)
        actvSelecionarCliente.setAdapter(clienteAdapter)

        // Configurar a lista de vagas disponíveis
        val vagasDisponiveis = vagaRepository.getVagas().filter { it.status == "disponivel" }.map { it.nome }
        val vagaAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, vagasDisponiveis)
        val actvSelecionarVaga = view.findViewById<AutoCompleteTextView>(R.id.actv_selecionar_vaga)
        actvSelecionarVaga.setAdapter(vagaAdapter)

        // Configurar o botão de confirmação
        val btnConfirmarCheckIn = view.findViewById<Button>(R.id.btn_confirmar_check_in)
        btnConfirmarCheckIn.setOnClickListener {
            // TODO: Adicionar a lógica real de check-in
            Toast.makeText(context, "Check-in realizado com sucesso!", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }
    }
}
