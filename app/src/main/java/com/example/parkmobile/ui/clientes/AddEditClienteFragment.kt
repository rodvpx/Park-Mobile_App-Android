package com.example.parkmobile.ui.clientes

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import com.example.parkmobile.data.model.Cliente
import com.example.parkmobile.databinding.BottomSheetAddEditClienteBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddEditClienteFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetAddEditClienteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ClientesViewModel by activityViewModels()

    private var cliente: Cliente? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cliente = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(ARG_CLIENTE, Cliente::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.getParcelable(ARG_CLIENTE)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAddEditClienteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupListeners()
        observeViewModel()
    }

    private fun setupUI() {
        if (cliente == null) {
            // Modo Adicionar
            binding.tvTitle.text = "Adicionar Cliente"
            binding.btnDeletarCliente.visibility = View.GONE
        } else {
            // Modo Editar
            binding.tvTitle.text = "Editar Cliente"
            binding.etNomeCliente.setText(cliente?.nome)
            binding.etCpfCliente.setText(cliente?.cpf)
            binding.btnDeletarCliente.visibility = View.VISIBLE
        }
    }

    private fun setupListeners() {
        binding.btnSalvarCliente.setOnClickListener {
            val nome = binding.etNomeCliente.text.toString()
            val cpf = binding.etCpfCliente.text.toString()

            if (cliente == null) {
                viewModel.addCliente(nome, cpf)
            } else {
                viewModel.updateCliente(cliente!!, nome, cpf)
            }
        }

        binding.btnDeletarCliente.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Deletar Cliente")
                .setMessage("Tem certeza que deseja deletar este cliente? Esta ação não pode ser desfeita.")
                .setPositiveButton("Deletar") { _, _ ->
                    cliente?.let { viewModel.deleteCliente(it) }
                    dismiss()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }

    private fun observeViewModel() {
        viewModel.dismiss.observe(viewLifecycleOwner) { shouldDismiss ->
            if (shouldDismiss) {
                dismiss()
                viewModel.onDismissed()
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (message.isNotBlank()) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_CLIENTE = "cliente"

        fun newInstance(cliente: Cliente?): AddEditClienteFragment {
            val fragment = AddEditClienteFragment()
            val args = Bundle()
            args.putParcelable(ARG_CLIENTE, cliente)
            fragment.arguments = args
            return fragment
        }
    }
}
