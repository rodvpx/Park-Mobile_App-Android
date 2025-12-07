package com.example.parkmobile.ui.clientes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.R
import com.example.parkmobile.data.repository.ClienteRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class ClientesAdminFragment : Fragment() {

    private val clienteRepository by lazy { ClienteRepository(FirebaseFirestore.getInstance()) }
    private val viewModel: ClientesViewModel by activityViewModels { ClientesViewModelFactory(clienteRepository) }

    private lateinit var rvClientes: RecyclerView
    private lateinit var fabAddCliente: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_clientes_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvClientes = view.findViewById(R.id.rv_clientes)
        fabAddCliente = view.findViewById(R.id.fab_add_cliente)

        val clientesAdapter = ClientesAdapter { cliente ->
            AddEditClienteFragment.newInstance(cliente).show(childFragmentManager, "AddEditClienteFragment")
        }
        rvClientes.adapter = clientesAdapter

        viewModel.clientes.observe(viewLifecycleOwner) { clientes ->
            clientesAdapter.submitList(clientes)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (message.isNotBlank()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.carregarClientes()

        fabAddCliente.setOnClickListener {
            AddEditClienteFragment.newInstance(null).show(childFragmentManager, "AddEditClienteFragment")
        }
    }
}
