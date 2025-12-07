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
import com.example.parkmobile.data.model.ClienteVaga
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.*

class ConfirmCheckOutBottomSheetFragment : BottomSheetDialogFragment() {

    private val viewModel: EstacionamentoViewModel by activityViewModels {
        EstacionamentoViewModelFactory()
    }

    private var clienteVaga: ClienteVaga? = null

    private lateinit var tvConfirmPlaca: TextView
    private lateinit var tvConfirmModeloMarca: TextView
    private lateinit var tvConfirmEntrada: TextView
    private lateinit var btnCancelar: Button
    private lateinit var btnConfirmar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            clienteVaga = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(ARG_CLIENTE_VAGA, ClienteVaga::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.getParcelable(ARG_CLIENTE_VAGA)
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

        clienteVaga?.let {
            tvConfirmPlaca.text = "Placa: ${it.placa}"
            tvConfirmModeloMarca.text = "${it.modelo} - ${it.marca}"
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            tvConfirmEntrada.text = "Entrada: ${sdf.format(it.dataEntrada)}"
        }

        btnCancelar.setOnClickListener { dismiss() }

        btnConfirmar.setOnClickListener {
            clienteVaga?.recibo?.let {
                viewModel.realizarCheckOut(it)
                // O observer no fragment pai cuidará de mostrar o Toast e atualizar a lista
                dismiss()
            }
        }
    }

    companion object {
        private const val ARG_CLIENTE_VAGA = "clienteVaga"

        fun newInstance(clienteVaga: ClienteVaga): ConfirmCheckOutBottomSheetFragment {
            val fragment = ConfirmCheckOutBottomSheetFragment()
            val args = Bundle()
            args.putParcelable(ARG_CLIENTE_VAGA, clienteVaga)
            fragment.arguments = args
            return fragment
        }
    }
}
