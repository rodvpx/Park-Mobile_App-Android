package com.example.parkmobile.ui.vagas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.parkmobile.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton

class AddVagaBottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_add_vaga, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnCadastrar = view.findViewById<MaterialButton>(R.id.btn_cadastrar_vaga)

        btnCadastrar.setOnClickListener {
            // TODO: Implementar a lógica de cadastro da vaga
            Toast.makeText(context, "Vaga cadastrada com sucesso!", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }
}
