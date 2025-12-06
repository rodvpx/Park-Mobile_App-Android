package com.example.parkmobile.ui.clientes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.parkmobile.data.repository.ClienteRepository
import com.example.parkmobile.databinding.FragmentClientesAdminBinding
import com.google.firebase.firestore.FirebaseFirestore

class ClientesAdminFragment : Fragment() {

    private var _binding: FragmentClientesAdminBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ClientesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientesAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Configuração do ViewModel (sem Hilt)
        val firestore = FirebaseFirestore.getInstance()
        val clienteRepository = ClienteRepository(firestore)
        val factory = ClientesViewModelFactory(clienteRepository)
        viewModel = ViewModelProvider(this, factory)[ClientesViewModel::class.java]

        // 2. Ligar o ViewModel e o LifecycleOwner ao DataBinding
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // 3. Configuração do RecyclerView
        val clientesAdapter = ClientesAdapter { cliente ->
            // TODO: Definir ação de clique no item do cliente, ex: navegar para detalhes
            Toast.makeText(requireContext(), "Cliente selecionado: ${cliente.nome}", Toast.LENGTH_SHORT).show()
        }
        binding.rvClientes.adapter = clientesAdapter

        // 4. Observar mudanças nos dados
        viewModel.clientes.observe(viewLifecycleOwner) { clientes ->
            clientesAdapter.submitList(clientes)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        // 5. Carregar os dados iniciais
        viewModel.carregarClientes()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Evitar memory leaks
    }
}
