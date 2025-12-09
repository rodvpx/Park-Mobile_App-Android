package com.example.parkmobile.ui.recibo

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.parkmobile.R
import com.example.parkmobile.data.model.HistoricoEstacionamento
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class ReciboDetalhesClienteBottomSheet : BottomSheetDialogFragment() {

    private var historico: HistoricoEstacionamento? = null

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
        return inflater.inflate(R.layout.bottom_sheet_recibo_detalhes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historico?.let { item ->
            // Preenche os campos do layout com os dados do HistoricoEstacionamento
            view.findViewById<TextView>(R.id.tv_recibo_detalhes).text = "Recibo: ${item.recibo}"
            
            view.findViewById<TextView>(R.id.tv_nome_cliente_detalhes).visibility = View.GONE
            view.findViewById<TextView>(R.id.tv_cpf_cliente_detalhes).visibility = View.GONE

            view.findViewById<TextView>(R.id.tv_placa_detalhes).text = "Placa: ${item.placaVeiculo}"
            view.findViewById<TextView>(R.id.tv_marca_modelo_detalhes).text = "Veículo: ${item.marcaVeiculo} ${item.modeloVeiculo}"
            view.findViewById<TextView>(R.id.tv_cor_detalhes).text = "Cor: ${item.corVeiculo}"

            val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val entrada = item.checkIn?.let { format.format(it) } ?: "--"
            val saida = item.checkOut?.let { format.format(it) } ?: "--"
            view.findViewById<TextView>(R.id.tv_data_entrada_detalhes).text = "Entrada: $entrada"
            view.findViewById<TextView>(R.id.tv_data_saida_detalhes).text = "Saída: $saida"

            val permanenciaStr = if (item.checkIn != null && item.checkOut != null) {
                val diff = item.checkOut!!.time - item.checkIn!!.time
                val hours = TimeUnit.MILLISECONDS.toHours(diff)
                val minutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60
                "Permanência: ${hours}h ${minutes}m"
            } else {
                "Permanência: --"
            }
            view.findViewById<TextView>(R.id.tv_tempo_permanencia_detalhes).text = permanenciaStr

            val desconto = item.descontoAplicado?.let { String.format(Locale.getDefault(), "R$ %.2f", it) } ?: "R$ 0,00"
            view.findViewById<TextView>(R.id.tv_desconto_detalhes).text = "Desconto: $desconto"

            val total = item.valor ?: 0.0
            val totalFormatado = String.format(Locale.getDefault(), "R$ %.2f", total)
            view.findViewById<TextView>(R.id.tv_valor_detalhes).text = "Valor Final: $totalFormatado"
        }
    }

    companion object {
        private const val ARG_HISTORICO = "historicoEstacionamento"

        fun newInstance(historico: HistoricoEstacionamento): ReciboDetalhesClienteBottomSheet {
            return ReciboDetalhesClienteBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_HISTORICO, historico)
                }
            }
        }
    }
}
