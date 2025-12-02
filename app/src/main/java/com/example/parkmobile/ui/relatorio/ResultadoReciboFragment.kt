package com.example.parkmobile.ui.relatorio

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
            @Suppress("DEPRECATION")
            recibo = it.getSerializable(ARG_RECIBO) as? HistoricoItem
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_resultado_recibo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recibo?.let { item ->
            view.findViewById<TextView>(R.id.tv_codigo_recibo).text = item.codigo
            view.findViewById<TextView>(R.id.tv_cpf).text = item.cpf
            view.findViewById<TextView>(R.id.tv_vaga).text = item.vaga
            view.findViewById<TextView>(R.id.tv_entrada).text = item.entrada
            view.findViewById<TextView>(R.id.tv_saida).text = item.saida
            view.findViewById<TextView>(R.id.tv_placa).text = item.placa
            view.findViewById<TextView>(R.id.tv_veiculo).text = item.marca
            view.findViewById<TextView>(R.id.tv_modelo).text = item.modelo
            view.findViewById<TextView>(R.id.tv_cor).text = item.cor
            view.findViewById<TextView>(R.id.tv_valor).text = item.valor
            view.findViewById<TextView>(R.id.tv_desconto).text = item.desconto
            view.findViewById<TextView>(R.id.tv_total).text = item.total
        }
    }

    companion object {
        private const val ARG_RECIBO = "recibo"

        fun newInstance(recibo: HistoricoItem) =
            ResultadoReciboFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_RECIBO, recibo)
                }
            }
    }
}

