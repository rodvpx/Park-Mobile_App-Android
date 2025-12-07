package com.example.parkmobile.ui.historico

import android.os.Build
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
            arguments?.getParcelable(ARG_CLIENTE_VAGA, ClienteVaga::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable<ClienteVaga>(ARG_CLIENTE_VAGA)
        }

        item?.let { clienteVaga ->
            // Popula o código do recibo
            detalhesCodigoValor.text = clienteVaga.recibo

            // Popula os itens restantes
            setupItem(view.findViewById(R.id.item_cpf), "ID Cliente:", clienteVaga.idCliente)
            setupItem(view.findViewById(R.id.item_vaga), "ID Vaga:", clienteVaga.idVaga)
            
            val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val entrada = clienteVaga.dataEntrada?.let { format.format(it) } ?: "--"
            val saida = clienteVaga.dataSaida?.let { format.format(it) } ?: "--"

            setupItem(view.findViewById(R.id.item_entrada), "Entrada:", entrada)
            setupItem(view.findViewById(R.id.item_saida), "Saída:", saida)
            setupItem(view.findViewById(R.id.item_tempo), "Tempo:", "--") // O tempo não está disponível em ClienteVaga

            setupItem(view.findViewById(R.id.item_placa), "Placa:", clienteVaga.placa)
            setupItem(view.findViewById(R.id.item_marca), "Marca:", clienteVaga.marca)
            setupItem(view.findViewById(R.id.item_modelo), "Modelo:", clienteVaga.modelo)
            setupItem(view.findViewById(R.id.item_cor), "Cor:", clienteVaga.cor)

            val valor = clienteVaga.valor?.let { String.format(Locale.getDefault(), "R$ %.2f", it) } ?: "R$ 0,00"
            val desconto = clienteVaga.desconto?.let { String.format(Locale.getDefault(), "R$ %.2f", it) } ?: "R$ 0,00"
            val total = (clienteVaga.valor ?: 0.0) - (clienteVaga.desconto ?: 0.0)
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
        private const val ARG_CLIENTE_VAGA = "clienteVaga"

        fun newInstance(item: ClienteVaga): DetalhesBottomSheetFragment {
            val fragment = DetalhesBottomSheetFragment()
            val args = Bundle()
            args.putParcelable(ARG_CLIENTE_VAGA, item)
            fragment.arguments = args
            return fragment
        }
    }
}
