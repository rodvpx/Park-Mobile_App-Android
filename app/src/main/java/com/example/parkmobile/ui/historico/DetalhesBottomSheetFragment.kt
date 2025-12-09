package com.example.parkmobile.ui.historico

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

class DetalhesBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var detalhesCodigoValor: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recibo_detalhes_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detalhesCodigoValor = view.findViewById(R.id.detalhes_codigo_valor)

        val item = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(ARG_HISTORICO, HistoricoEstacionamento::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable<HistoricoEstacionamento>(ARG_HISTORICO)
        }

        item?.let { historico ->
            // Popula o código do recibo
            detalhesCodigoValor.text = historico.recibo

            // Popula os itens restantes
            setupItem(view.findViewById(R.id.item_cpf), "ID Cliente:", historico.idCliente)
            setupItem(view.findViewById(R.id.item_vaga), "ID Vaga:", historico.idVaga)

            val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val entrada = historico.checkIn?.let { format.format(it) } ?: "--"
            val saida = historico.checkOut?.let { format.format(it) } ?: "--"

            var tempoEstacionado = "--"
            historico.checkIn?.let { checkInDate ->
                historico.checkOut?.let { checkOutDate ->
                    val diffInMillis = checkOutDate.time - checkInDate.time
                    val diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
                    val hours = diffInMinutes / 60
                    val minutes = diffInMinutes % 60
                    tempoEstacionado = String.format("%02dh %02dmin", hours, minutes)
                }
            }


            setupItem(view.findViewById(R.id.item_entrada), "Entrada:", entrada)
            setupItem(view.findViewById(R.id.item_saida), "Saída:", saida)
            setupItem(view.findViewById(R.id.item_tempo), "Tempo:", tempoEstacionado)

            setupItem(view.findViewById(R.id.item_placa), "Placa:", historico.placaVeiculo)
            setupItem(view.findViewById(R.id.item_marca), "Marca:", historico.marcaVeiculo)
            setupItem(view.findViewById(R.id.item_modelo), "Modelo:", historico.modeloVeiculo)
            setupItem(view.findViewById(R.id.item_cor), "Cor:", historico.corVeiculo)

            val valor = historico.valor?.let { String.format(Locale.getDefault(), "R$ %.2f", it) } ?: "R$ 0,00"
            val desconto = historico.descontoAplicado?.let { String.format(Locale.getDefault(), "R$ %.2f", it) } ?: "R$ 0,00"
            val total = (historico.valor ?: 0.0) - (historico.descontoAplicado ?: 0.0)
            val totalFormatado = String.format(Locale.getDefault(), "R$ %.2f", total)

            setupItem(view.findViewById(R.id.item_valor), "Valor:", valor)
            setupItem(view.findViewById(R.id.item_desconto), "Desconto:", desconto)
            setupItem(view.findViewById(R.id.item_valor_total), "Total:", totalFormatado)
        }
    }

    private fun setupItem(itemView: View, label: String, value: String?) {
        val labelView = itemView.findViewById<TextView>(R.id.label)
        val valueView = itemView.findViewById<TextView>(R.id.valor)

        labelView.text = label
        valueView.text = value
    }

    companion object {
        private const val ARG_HISTORICO = "historico"

        fun newInstance(item: HistoricoEstacionamento): DetalhesBottomSheetFragment {
            val fragment = DetalhesBottomSheetFragment()
            val args = Bundle()
            args.putParcelable(ARG_HISTORICO, item)
            fragment.arguments = args
            return fragment
        }
    }
}
