package com.example.parkmobile.ui.vagas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.parkmobile.R
import com.example.parkmobile.data.model.Vaga
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddEditVagaFragment : BottomSheetDialogFragment() {

    private val viewModel: VagasViewModel by activityViewModels { VagasViewModelFactory() }

    private var vaga: Vaga? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Usando o layout mais completo para ambas as operações
        return inflater.inflate(R.layout.bottom_sheet_edit_vaga, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvTitle = view.findViewById<TextView>(R.id.tv_bottom_sheet_title)
        val etCodigo = view.findViewById<EditText>(R.id.et_codigo_vaga)
        val rgStatus = view.findViewById<RadioGroup>(R.id.rg_status_vaga)
        val btnSalvar = view.findViewById<Button>(R.id.btn_salvar_vaga)

        arguments?.let {
            vaga = it.getParcelable("vaga")
        }

        if (vaga == null) {
            tvTitle.text = "Adicionar Vaga"
        } else {
            tvTitle.text = "Editar Vaga"
            etCodigo.setText(vaga?.codigo)
            if (vaga?.status == Vaga.StatusVaga.OCUPADA.name) {
                rgStatus.check(R.id.rb_ocupado)
            } else {
                rgStatus.check(R.id.rb_livre)
            }
        }

        btnSalvar.setOnClickListener {
            val codigo = etCodigo.text.toString().trim()
            val status = if (rgStatus.checkedRadioButtonId == R.id.rb_ocupado) {
                Vaga.StatusVaga.OCUPADA.name
            } else {
                Vaga.StatusVaga.LIVRE.name
            }

            if (vaga == null) {
                viewModel.addVaga(codigo, status)
            } else {
                viewModel.updateVaga(vaga!!, codigo, status)
            }
        }

        viewModel.dismiss.observe(viewLifecycleOwner) {
            if (it) {
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
        fun newInstance(vaga: Vaga?): AddEditVagaFragment {
            val fragment = AddEditVagaFragment()
            val args = Bundle()
            vaga?.let { args.putParcelable("vaga", it) }
            fragment.arguments = args
            return fragment
        }
    }
}
