package com.example.parkmobile.ui.vagas

import android.os.Build
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

class EditVagaBottomSheetFragment : BottomSheetDialogFragment() {

    private val viewModel: VagasViewModel by activityViewModels()

    private var vaga: Vaga? = null

    private lateinit var etCodigoVaga: TextInputEditText
    private lateinit var rbLivre: RadioButton
    private lateinit var rbOcupado: RadioButton
    private lateinit var btnSalvarVaga: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            vaga = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable("vaga", Vaga::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.getParcelable("vaga")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_edit_vaga, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etCodigoVaga = view.findViewById(R.id.et_codigo_vaga)
        rbLivre = view.findViewById(R.id.rb_livre)
        rbOcupado = view.findViewById(R.id.rb_ocupado)
        btnSalvarVaga = view.findViewById(R.id.btn_salvar_vaga)

        etCodigoVaga.setText(vaga?.codigo)

        // Pre-seleciona o RadioButton correto
        if (vaga?.status == "Livre") {
            rbLivre.isChecked = true
        } else {
            rbOcupado.isChecked = true
        }

        btnSalvarVaga.setOnClickListener {
            vaga?.let {
                val novoCodigo = etCodigoVaga.text.toString()
                val novoStatus = if (rbLivre.isChecked) {
                    "Livre"
                } else {
                    "Ocupada"
                }
                // Supondo que o ViewModel aceite String para o status.
                viewModel.updateVaga(it, novoCodigo, novoStatus)
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (message.isNotBlank()) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.dismiss.observe(viewLifecycleOwner) { shouldDismiss ->
            if (shouldDismiss) {
                dismiss()
                viewModel.onDismissed()
            }
        }
    }

    companion object {
        fun newInstance(vaga: Vaga): EditVagaBottomSheetFragment {
            val fragment = EditVagaBottomSheetFragment()
            val args = Bundle()
            args.putParcelable("vaga", vaga)
            fragment.arguments = args
            return fragment
        }
    }
}
