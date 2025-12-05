package com.example.parkmobile.ui.relatorio

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.parkmobile.R
import com.example.parkmobile.data.model.HistoricoItem

class ResultadoReciboFragment : Fragment() {

    private var recibo: HistoricoItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            recibo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(ARG_RECIBO, HistoricoItem::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.getParcelable(ARG_RECIBO)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recibo_detalhes_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recibo?.let { item ->
            view.findViewById<TextView>(R.id.detalhes_codigo_valor).text = item.codigo

            val cpfView = view.findViewById<View>(R.id.item_cpf)
            cpfView.findViewById<TextView>(R.id.label).text = "CPF:"
            cpfView.findViewById<TextView>(R.id.valor).text = item.cpf

            val vagaView = view.findViewById<View>(R.id.item_vaga)
            vagaView.findViewById<TextView>(R.id.label).text = "Vaga:"
            vagaView.findViewById<TextView>(R.id.valor).text = item.vaga

            val entradaView = view.findViewById<View>(R.id.item_entrada)
            entradaView.findViewById<TextView>(R.id.label).text = "Entrada:"
            entradaView.findViewById<TextView>(R.id.valor).text = item.entrada

            val saidaView = view.findViewById<View>(R.id.item_saida)
            saidaView.findViewById<TextView>(R.id.label).text = "Saída:"
            saidaView.findViewById<TextView>(R.id.valor).text = item.saida

            val placaView = view.findViewById<View>(R.id.item_placa)
            placaView.findViewById<TextView>(R.id.label).text = "Placa:"
            placaView.findViewById<TextView>(R.id.valor).text = item.placa

            val marcaView = view.findViewById<View>(R.id.item_marca)
            marcaView.findViewById<TextView>(R.id.label).text = "Veículo:"
            marcaView.findViewById<TextView>(R.id.valor).text = item.marca

            val modeloView = view.findViewById<View>(R.id.item_modelo)
            modeloView.findViewById<TextView>(R.id.label).text = "Modelo:"
            modeloView.findViewById<TextView>(R.id.valor).text = item.modelo

            val corView = view.findViewById<View>(R.id.item_cor)
            corView.findViewById<TextView>(R.id.label).text = "Cor:"
            corView.findViewById<TextView>(R.id.valor).text = item.cor

            val valorView = view.findViewById<View>(R.id.item_valor)
            valorView.findViewById<TextView>(R.id.label).text = "Valor:"
            valorView.findViewById<TextView>(R.id.valor).text = item.valor

            val descontoView = view.findViewById<View>(R.id.item_desconto)
            descontoView.findViewById<TextView>(R.id.label).text = "Desconto:"
            descontoView.findViewById<TextView>(R.id.valor).text = item.desconto

            val totalView = view.findViewById<View>(R.id.item_valor_total)
            totalView.findViewById<TextView>(R.id.label).text = "Total:"
            totalView.findViewById<TextView>(R.id.valor).text = item.total
        }
    }

    companion object {
        private const val ARG_RECIBO = "recibo"

        fun newInstance(recibo: HistoricoItem) =
            ResultadoReciboFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_RECIBO, recibo)
                }
            }
    }
}
