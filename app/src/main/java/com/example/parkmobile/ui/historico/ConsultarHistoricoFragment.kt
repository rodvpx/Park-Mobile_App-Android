package com.example.parkmobile.ui.historico

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.R
import com.example.parkmobile.data.repository.ClienteRepository
import com.example.parkmobile.ui.clientes.ClientesAdapter
import com.example.parkmobile.ui.clientes.ClientesViewModel
import com.example.parkmobile.ui.clientes.ClientesViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore

class ConsultarHistoricoFragment : Fragment() {

    private val viewModel: ClientesViewModel by viewModels { 
        ClientesViewModelFactory(ClienteRepository(FirebaseFirestore.getInstance())) 
    }

    private lateinit var searchView: SearchView
    private lateinit var rvClientesResultado: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_consultar_historico, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchView = view.findViewById(R.id.search_view)
        rvClientesResultado = view.findViewById(R.id.rv_clientes_resultado)
        rvClientesResultado.layoutManager = LinearLayoutManager(requireContext())

        val clientesAdapter = ClientesAdapter { cliente ->
            val bundle = bundleOf(
                "clienteId" to cliente.id,
                "clienteNome" to cliente.nome
            )
            findNavController().navigate(R.id.action_relatoriosAdminFragment_to_resultadoHistoricoFragment, bundle)
        }
        rvClientesResultado.adapter = clientesAdapter

        setupSearch(clientesAdapter)
        observeViewModel(clientesAdapter)

        viewModel.carregarClientes()
    }

    private fun setupSearch(adapter: ClientesAdapter) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                val fullList = viewModel.clientes.value
                val query = newText.orEmpty().trim()
                if (query.isBlank()) {
                    adapter.submitList(fullList)
                } else {
                    val filteredList = fullList?.filter {
                        it.cpf.contains(query, ignoreCase = true) || it.nome.contains(query, ignoreCase = true)
                    }
                    adapter.submitList(filteredList)
                }
                return true
            }
        })
    }

    private fun observeViewModel(adapter: ClientesAdapter) {
        viewModel.clientes.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error.isNotBlank()) {
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            }
        }
    }
}
