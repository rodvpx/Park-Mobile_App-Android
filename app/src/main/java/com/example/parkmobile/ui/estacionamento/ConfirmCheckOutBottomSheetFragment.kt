package com.example.parkmobile.ui.estacionamento

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.example.parkmobile.R
import com.example.parkmobile.data.model.HistoricoEstacionamento
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.*

class ConfirmCheckOutBottomSheetFragment : BottomSheetDialogFragment() {

    private val viewModel: EstacionamentoViewModel by activityViewModels {
        EstacionamentoViewModelFactory()
    }

    private var historico: HistoricoEstacionamento? = null

    private lateinit var tvConfirmPlaca: TextView
    private lateinit var tvConfirmModeloMarca: TextView
    private lateinit var tvConfirmEntrada: TextView
    private lateinit var btnCancelar: Button
    private lateinit var btnConfirmar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            historico = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(ARG_HISTORICO, HistoricoEstacionamento::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.getParcelable(ARG_HISTORICO)
            }
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

        tvConfirmPlaca = view.findViewById(R.id.tv_confirm_placa)
        tvConfirmModeloMarca = view.findViewById(R.id.tv_confirm_modelo_marca)
        tvConfirmEntrada = view.findViewById(R.id.tv_confirm_entrada)
        btnCancelar = view.findViewById(R.id.btn_cancelar)
        btnConfirmar = view.findViewById(R.id.btn_confirmar)

        historico?.let { h ->
            tvConfirmPlaca.text = "Placa: ${h.placaVeiculo.uppercase()}"
            tvConfirmModeloMarca.text = "${h.modeloVeiculo} - ${h.marcaVeiculo}"
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val checkInDateFormatted = h.checkIn?.let { sdf.format(it) } ?: "N/A"
            tvConfirmEntrada.text = "Entrada: $checkInDateFormatted"
        }

        btnCancelar.setOnClickListener { dismiss() }

        btnConfirmar.setOnClickListener {
            historico?.let { h ->
                viewModel.realizarCheckOut(h)
                dismiss()
            }
        }
    }

    companion object {
        const val TAG = "ConfirmCheckOutBottomSheet"
        private const val ARG_HISTORICO = "historico"

        fun newInstance(historico: HistoricoEstacionamento): ConfirmCheckOutBottomSheetFragment {
            return ConfirmCheckOutBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_HISTORICO, historico)
                }
            }
        }
    }
}
