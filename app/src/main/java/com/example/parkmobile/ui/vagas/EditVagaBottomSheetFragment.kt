package com.example.parkmobile.ui.vagas

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.example.parkmobile.R
import com.example.parkmobile.data.model.Vaga
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class EditVagaBottomSheetFragment : BottomSheetDialogFragment() {

    private var vaga: Vaga? = null

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

        val etCodigoVaga = view.findViewById<TextInputEditText>(R.id.et_codigo_vaga)
        val rgStatusVaga = view.findViewById<RadioGroup>(R.id.rg_status_vaga)
        val rbLivre = view.findViewById<RadioButton>(R.id.rb_livre)
        val rbOcupado = view.findViewById<RadioButton>(R.id.rb_ocupado)
        val btnSalvar = view.findViewById<MaterialButton>(R.id.btn_salvar_vaga)

        vaga?.let {
            etCodigoVaga.setText(it.nome.replace("Vaga ", ""))
            if (it.status == "disponivel") {
                rbLivre.isChecked = true
            } else {
                rbOcupado.isChecked = true
            }
        }

        btnSalvar.setOnClickListener {
            // TODO: Implementar a lógica de salvar a vaga
            Toast.makeText(context, "Vaga salva com sucesso!", Toast.LENGTH_SHORT).show()
            dismiss()
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
