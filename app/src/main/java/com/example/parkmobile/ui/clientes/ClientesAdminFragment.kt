package com.example.parkmobile.ui.clientes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.parkmobile.data.repository.ClienteRepository
import com.example.parkmobile.databinding.FragmentClientesAdminBinding
import com.google.firebase.firestore.FirebaseFirestore

class ClientesAdminFragment : Fragment() {

    private var _binding: FragmentClientesAdminBinding? = null
    private val binding get() = _binding!!

    private val clienteRepository by lazy { ClienteRepository(FirebaseFirestore.getInstance()) }
    private val viewModel: ClientesViewModel by activityViewModels { ClientesViewModelFactory(clienteRepository) }

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

        val clientesAdapter = ClientesAdapter { cliente ->
            AddEditClienteFragment.newInstance(cliente).show(childFragmentManager, "AddEditClienteFragment")
        }
        binding.rvClientes.adapter = clientesAdapter

        viewModel.clientes.observe(viewLifecycleOwner) { clientes ->
            clientesAdapter.submitList(clientes)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (message.isNotBlank()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.carregarClientes()

        binding.fabAddCliente.setOnClickListener {
            AddEditClienteFragment.newInstance(null).show(childFragmentManager, "AddEditClienteFragment")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
