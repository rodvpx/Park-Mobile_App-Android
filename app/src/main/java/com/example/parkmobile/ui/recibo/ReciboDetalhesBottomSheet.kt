package com.example.parkmobile.ui.recibo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.parkmobile.R
import com.example.parkmobile.data.model.ClienteVaga
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.Locale

class ReciboDetalhesBottomSheet : BottomSheetDialogFragment() {

    private var clienteVaga: ClienteVaga? = null

    private lateinit var tvReciboDetalhes: TextView
    private lateinit var tvPlacaDetalhes: TextView
    private lateinit var tvDataEntradaDetalhes: TextView
    private lateinit var tvDataSaidaDetalhes: TextView
    private lateinit var tvValorDetalhes: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            clienteVaga = it.getParcelable(ARG_CLIENTE_VAGA)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_recibo_detalhes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvReciboDetalhes = view.findViewById(R.id.tv_recibo_detalhes)
        tvPlacaDetalhes = view.findViewById(R.id.tv_placa_detalhes)
        tvDataEntradaDetalhes = view.findViewById(R.id.tv_data_entrada_detalhes)
        tvDataSaidaDetalhes = view.findViewById(R.id.tv_data_saida_detalhes)
        tvValorDetalhes = view.findViewById(R.id.tv_valor_detalhes)

        clienteVaga?.let { item ->
            tvReciboDetalhes.text = "Recibo: ${item.recibo}"
            tvPlacaDetalhes.text = "Placa: ${item.placa}"

            val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            tvDataEntradaDetalhes.text = item.dataEntrada?.let { "Entrada: ${format.format(it)}" } ?: "Entrada: --"
            tvDataSaidaDetalhes.text = item.dataSaida?.let { "Saída: ${format.format(it)}" } ?: "Saída: --"

            val valorSeguro = item.valor ?: 0.0
            tvValorDetalhes.text = String.format(Locale.getDefault(), "R$ %.2f", valorSeguro)
        }
    }

    companion object {
        private const val ARG_CLIENTE_VAGA = "clienteVaga"

        fun newInstance(clienteVaga: ClienteVaga): ReciboDetalhesBottomSheet {
            return ReciboDetalhesBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_CLIENTE_VAGA, clienteVaga)
                }
            }
        }
    }
}
