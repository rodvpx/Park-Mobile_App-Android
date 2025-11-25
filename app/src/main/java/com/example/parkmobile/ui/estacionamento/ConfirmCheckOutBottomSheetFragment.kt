package com.example.parkmobile.ui.estacionamento

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.BundleCompat
import com.example.parkmobile.R
import com.example.parkmobile.data.model.CheckOutItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ConfirmCheckOutBottomSheetFragment : BottomSheetDialogFragment() {

    private var checkOutItem: CheckOutItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            checkOutItem = BundleCompat.getParcelable(it, "checkOutItem", CheckOutItem::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_confirm_checkout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvNome = view.findViewById<TextView>(R.id.tv_confirm_nome_cliente)
        val tvCpf = view.findViewById<TextView>(R.id.tv_confirm_cpf_cliente)
        val tvPlaca = view.findViewById<TextView>(R.id.tv_confirm_placa)
        val tvVaga = view.findViewById<TextView>(R.id.tv_confirm_vaga)
        val tvEntrada = view.findViewById<TextView>(R.id.tv_confirm_entrada)
        val btnCancelar = view.findViewById<Button>(R.id.btn_cancelar)
        val btnConfirmar = view.findViewById<Button>(R.id.btn_confirmar)

        checkOutItem?.let {
            tvNome.text = it.nome
            tvCpf.text = "CPF: ${it.cpf}"
            tvPlaca.text = "Placa: ${it.placa}"
            tvVaga.text = "Vaga: ${it.vaga}"
            tvEntrada.text = "Entrada: ${it.entrada}"
        }

        btnCancelar.setOnClickListener {
            dismiss()
        }

        btnConfirmar.setOnClickListener {
            // TODO: Adicionar lógica de confirmação real
            Toast.makeText(context, "Check-out confirmado!", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }

    companion object {
        fun newInstance(checkOutItem: CheckOutItem): ConfirmCheckOutBottomSheetFragment {
            val fragment = ConfirmCheckOutBottomSheetFragment()
            val args = Bundle()
            args.putParcelable("checkOutItem", checkOutItem)
            fragment.arguments = args
            return fragment
        }
    }
}
