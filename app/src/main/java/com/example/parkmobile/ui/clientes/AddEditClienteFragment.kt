package com.example.parkmobile.ui.clientes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.parkmobile.R
import com.example.parkmobile.data.model.Cliente
import com.example.parkmobile.data.repository.ClienteRepository
import com.example.parkmobile.util.CpfMaskTextWatcher
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddEditClienteFragment : BottomSheetDialogFragment() {

    private val clienteRepository by lazy { ClienteRepository(FirebaseFirestore.getInstance()) }
    private val viewModel: ClientesViewModel by activityViewModels { ClientesViewModelFactory(clienteRepository) }
    private val auth by lazy { FirebaseAuth.getInstance() } // Instância do Firebase Auth

    private var cliente: Cliente? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_add_edit_cliente, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etNome = view.findViewById<EditText>(R.id.et_nome_cliente)
        val etCpf = view.findViewById<EditText>(R.id.et_cpf_cliente)
        val btnSalvar = view.findViewById<Button>(R.id.btn_salvar_cliente)

        etCpf.addTextChangedListener(CpfMaskTextWatcher(etCpf))

        arguments?.let {
            cliente = it.getParcelable("cliente")
            cliente?.let {
                etNome.setText(it.nome)
                etCpf.setText(it.cpf)
            }
        }

        btnSalvar.setOnClickListener {
            val nome = etNome.text.toString().trim()
            val cpf = etCpf.text.toString().filter { it.isDigit() }

            if (nome.isBlank() || cpf.length != 11) {
                Toast.makeText(requireContext(), "Nome e CPF (11 dígitos) são obrigatórios.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = auth.currentUser?.uid
            if (userId == null) {
                Toast.makeText(requireContext(), "Erro: Usuário não autenticado.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (cliente == null) {
                // Admin criando cliente
                viewModel.addCliente(nome, cpf, userId)
            } else {
                // Editando cliente existente
                viewModel.updateCliente(cliente!!.copy(nome = nome, cpf = cpf), userId)
            }
            dismiss()
        }

    }

    companion object {
        fun newInstance(cliente: Cliente?): AddEditClienteFragment {
            val fragment = AddEditClienteFragment()
            val args = Bundle()
            args.putParcelable("cliente", cliente)
            fragment.arguments = args
            return fragment
        }
    }
}
