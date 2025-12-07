package com.example.parkmobile.ui.clientes

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import com.example.parkmobile.R
import com.example.parkmobile.data.model.Cliente
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText

class AddEditClienteFragment : BottomSheetDialogFragment() {

    private val viewModel: ClientesViewModel by activityViewModels()

    private var cliente: Cliente? = null

    private lateinit var tvTitle: TextView
    private lateinit var etNomeCliente: TextInputEditText
    private lateinit var etCpfCliente: TextInputEditText
    private lateinit var btnSalvarCliente: Button
    private lateinit var btnDeletarCliente: Button

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
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_add_edit_cliente, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvTitle = view.findViewById(R.id.tv_title)
        etNomeCliente = view.findViewById(R.id.et_nome_cliente)
        etCpfCliente = view.findViewById(R.id.et_cpf_cliente)
        btnSalvarCliente = view.findViewById(R.id.btn_salvar_cliente)
        btnDeletarCliente = view.findViewById(R.id.btn_deletar_cliente)

        setupUI()
        setupListeners()
        observeViewModel()
    }

    private fun setupUI() {
        if (cliente == null) {
            // Modo Adicionar
            tvTitle.text = "Adicionar Cliente"
            btnDeletarCliente.visibility = View.GONE
        } else {
            // Modo Editar
            tvTitle.text = "Editar Cliente"
            etNomeCliente.setText(cliente?.nome)
            etCpfCliente.setText(cliente?.cpf)
            btnDeletarCliente.visibility = View.VISIBLE
        }
    }

    private fun setupListeners() {
        btnSalvarCliente.setOnClickListener {
            val nome = etNomeCliente.text.toString()
            val cpf = etCpfCliente.text.toString()

            if (cliente == null) {
                viewModel.addCliente(nome, cpf)
            } else {
                viewModel.updateCliente(cliente!!, nome, cpf)
            }
        }

        btnDeletarCliente.setOnClickListener {
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
