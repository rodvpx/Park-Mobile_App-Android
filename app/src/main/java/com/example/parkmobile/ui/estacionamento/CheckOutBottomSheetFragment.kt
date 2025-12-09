package com.example.parkmobile.ui.estacionamento

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.example.parkmobile.R
import com.example.parkmobile.data.model.HistoricoEstacionamento
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import java.util.Date
import java.util.Locale
import kotlin.math.ceil

class CheckOutBottomSheetFragment : BottomSheetDialogFragment() {

    private var valorOriginal: Double = 0.0
    private var descontoPorcentagem: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_check_out_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvPlaca = view.findViewById<TextView>(R.id.tv_bottom_sheet_placa)
        val tvHoras = view.findViewById<TextView>(R.id.tv_bottom_sheet_horas)
        val tvValorOriginal = view.findViewById<TextView>(R.id.tv_bottom_sheet_valor_original)
        val etDesconto = view.findViewById<TextInputEditText>(R.id.et_bottom_sheet_desconto)
        val tvValorFinal = view.findViewById<TextView>(R.id.tv_bottom_sheet_valor_final)
        val btnConfirmar = view.findViewById<Button>(R.id.btn_bottom_sheet_confirmar)
        val btnCancelar = view.findViewById<Button>(R.id.btn_bottom_sheet_cancelar)

        val historico = arguments?.getParcelable<HistoricoEstacionamento>(ARG_HISTORICO)
        if (historico == null) {
            dismiss()
            return
        }

        val horas = calcularHoras(historico.checkIn, Date())
        valorOriginal = calcularValor(horas)

        tvPlaca.text = "Placa: ${historico.placaVeiculo.uppercase()}"
        tvHoras.text = "Permanência: $horas hora(s)"
        tvValorOriginal.text = String.format(Locale.getDefault(), "Subtotal: R$ %.2f", valorOriginal)
        atualizarValorFinal(tvValorFinal)

        etDesconto.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                descontoPorcentagem = s.toString().toDoubleOrNull() ?: 0.0
                atualizarValorFinal(tvValorFinal)
            }
        })

        btnConfirmar.setOnClickListener {
            val resultBundle = bundleOf(
                RESULT_KEY_HISTORICO to historico,
                RESULT_KEY_DESCONTO to descontoPorcentagem
            )
            setFragmentResult(REQUEST_KEY, resultBundle)
            dismiss()
        }

        btnCancelar.setOnClickListener {
            dismiss()
        }
    }

    private fun atualizarValorFinal(tvValorFinal: TextView) {
        val valorComDesconto = valorOriginal * (1 - (descontoPorcentagem / 100))
        tvValorFinal.text = String.format(Locale.getDefault(), "Total: R$ %.2f", valorComDesconto)
    }

    private fun calcularHoras(checkIn: Date?, checkOut: Date): Int {
        if (checkIn == null) return 1
        val diff = checkOut.time - checkIn.time
        val horas = ceil(diff.toDouble() / (1000 * 60 * 60)).toInt()
        return if (horas < 1) 1 else horas
    }

    private fun calcularValor(horas: Int, precoPorHora: Double = 5.0): Double {
        return horas * precoPorHora
    }

    companion object {
        const val TAG = "CheckOutBottomSheet"
        const val REQUEST_KEY = "checkOutRequest"
        const val RESULT_KEY_HISTORICO = "historicoResult"
        const val RESULT_KEY_DESCONTO = "descontoResult"

        private const val ARG_HISTORICO = "arg_historico"

        fun newInstance(historico: HistoricoEstacionamento): CheckOutBottomSheetFragment {
            return CheckOutBottomSheetFragment().apply {
                arguments = bundleOf(ARG_HISTORICO to historico)
            }
        }
    }
}
