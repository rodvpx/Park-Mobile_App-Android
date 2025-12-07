package com.example.parkmobile.ui.vagas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.parkmobile.R
import com.example.parkmobile.data.model.Vaga
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText

class AddVagaBottomSheetFragment : BottomSheetDialogFragment() {

    private val viewModel: VagasViewModel by activityViewModels()

    private lateinit var etCodigoVaga: TextInputEditText
    private lateinit var rbLivre: RadioButton
    private lateinit var btnCadastrarVaga: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_add_vaga, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etCodigoVaga = view.findViewById(R.id.et_codigo_vaga)
        rbLivre = view.findViewById(R.id.rb_livre)
        btnCadastrarVaga = view.findViewById(R.id.btn_cadastrar_vaga)

        btnCadastrarVaga.setOnClickListener {
            val codigo = etCodigoVaga.text.toString()
            val status = if (rbLivre.isChecked) {
                "Livre"
            } else {
                "Ocupada"
            }
            // Supondo que o ViewModel aceite String para o status.
            // Se o ViewModel esperar Vaga.StatusVaga, você precisará ajustar aqui.
            viewModel.addVaga(codigo, status)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (message.isNotBlank()) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.dismiss.observe(viewLifecycleOwner) { shouldDismiss ->
            if (shouldDismiss) {
                dismiss()
                viewModel.onDismissed() // Reseta o estado para não fechar novamente
            }
        }
    }
}
