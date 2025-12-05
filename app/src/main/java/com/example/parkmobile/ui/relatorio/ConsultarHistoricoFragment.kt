package com.example.parkmobile.ui.relatorio

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.R
import com.example.parkmobile.data.model.Cliente
import com.example.parkmobile.data.model.HistoricoItem
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.button.MaterialButton

class ConsultarHistoricoFragment : Fragment() {

    private lateinit var etNumeroCpf: TextInputEditText
    private lateinit var btnBuscar: MaterialButton
    private lateinit var rvClientes: RecyclerView
    private lateinit var adapter: ClienteAdapter
    private val todosClientes = mutableListOf<Cliente>()
    private val clientesEncontrados = mutableListOf<Cliente>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_consultar_historico, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etNumeroCpf = view.findViewById(R.id.et_numero_cpf)
        btnBuscar = view.findViewById(R.id.btn_buscar)
        rvClientes = view.findViewById(R.id.rv_clientes_resultado)

        // Configurar RecyclerView
        rvClientes.layoutManager = LinearLayoutManager(requireContext())
        adapter = ClienteAdapter(clientesEncontrados) { cliente ->
            // Ao clicar no cliente, abrir histórico
            val historico = gerarHistoricoFake(cliente.cpf)
            val fragment = ResultadoHistoricoFragment.newInstance(ArrayList(historico))
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack("historico_${cliente.cpf}")
                .commit()
        }
        rvClientes.adapter = adapter

        // Carrega a lista inicial e exibe
        carregarEExibirClientesIniciais()

        btnBuscar.setOnClickListener {
            buscarClientes()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun carregarEExibirClientesIniciais() {
        // Simula o carregamento de uma lista completa de clientes (substitua por sua lógica de repositório)
        todosClientes.clear()
        todosClientes.addAll(
            listOf(
                Cliente(1, "Fulano de Tal", "11122233344"),
                Cliente(2, "Ciclano Silva", "22233344455"),
                Cliente(3, "Beltrano Santos", "33344455566"),
                Cliente(4, "João da Esquina", "44455566677"),
                Cliente(5, "Maria das Dores", "55566677788")
            )
        )
        
        // Exibe todos os clientes inicialmente
        clientesEncontrados.clear()
        clientesEncontrados.addAll(todosClientes)
        adapter.notifyDataSetChanged()
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun buscarClientes() {
        val numeroCpf = etNumeroCpf.text.toString().trim()

        val clientesFiltrados = if (numeroCpf.isBlank()) {
            todosClientes // Se a busca estiver vazia, mostra todos
        } else {
            todosClientes.filter { it.cpf.contains(numeroCpf, ignoreCase = true) }
        }

        clientesEncontrados.clear()
        clientesEncontrados.addAll(clientesFiltrados)
        adapter.notifyDataSetChanged()

        if (clientesEncontrados.isEmpty() && numeroCpf.isNotBlank()) {
            Toast.makeText(requireContext(), "Nenhum cliente encontrado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun gerarHistoricoFake(cpf: String): List<HistoricoItem> {
        return listOf(
            HistoricoItem(
                codigo = "#1525626", cpf = cpf, vaga = "A-05", entrada = "04/10/25 - 15:00",
                saida = "04/10/25 - 16:30", tempo = "1h 30min", placa = "ABC3456",
                marca = "Mitsubishi", modelo = "L200", cor = "Branco",
                valor = "R$ 40,00", desconto = "R$ 5,00", total = "R$ 35,00"
            ),
            HistoricoItem(
                codigo = "#1525627", cpf = cpf, vaga = "B-12", entrada = "05/10/25 - 09:30",
                saida = "05/10/25 - 11:45", tempo = "2h 15min", placa = "XYZ9876",
                marca = "Toyota", modelo = "Corolla", cor = "Prata",
                valor = "R$ 50,00", desconto = "R$ 0,00", total = "R$ 50,00"
            )
        )
    }
}
