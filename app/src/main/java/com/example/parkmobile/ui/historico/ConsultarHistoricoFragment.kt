package com.example.parkmobile.ui.historico

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.parkmobile.databinding.FragmentConsultarHistoricoBinding
import com.example.parkmobile.ui.clientes.ClientesAdapter
import com.example.parkmobile.ui.clientes.ClientesViewModel
import com.example.parkmobile.ui.clientes.ClientesViewModelFactory

class ConsultarHistoricoFragment : Fragment() {

    private var _binding: FragmentConsultarHistoricoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ClientesViewModel by viewModels { ClientesViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConsultarHistoricoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val clientesAdapter = ClientesAdapter { cliente ->
            val action = RelatoriosAdminFragmentDirections.actionRelatoriosAdminFragmentToResultadoHistoricoFragment(cliente.id, cliente.nome)
            findNavController().navigate(action)
        }
        binding.rvClientesResultado.adapter = clientesAdapter

        setupSearch(clientesAdapter)
        observeViewModel(clientesAdapter)

        viewModel.carregarClientes()
    }

    private fun setupSearch(adapter: ClientesAdapter) {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
