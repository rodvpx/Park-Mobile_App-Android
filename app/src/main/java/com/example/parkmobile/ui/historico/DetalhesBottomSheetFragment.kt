package com.example.parkmobile.ui.historico

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.parkmobile.R
import com.example.parkmobile.data.model.HistoricoItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DetalhesBottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_receipt_detalhes_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val item = arguments?.getSerializable("historico_item") as? HistoricoItem

        item?.let {
            // Popula o código do recibo
            view.findViewById<TextView>(R.id.detalhes_codigo_valor).text = it.codigo

            // Popula os itens restantes
            setupItem(view.findViewById(R.id.item_cpf), "CPF:", it.cpf)
            setupItem(view.findViewById(R.id.item_vaga), "Vaga:", it.vaga)
            setupItem(view.findViewById(R.id.item_entrada), "Entrada:", it.entrada)
            setupItem(view.findViewById(R.id.item_saida), "Saída:", it.saida)
            setupItem(view.findViewById(R.id.item_tempo), "Tempo:", it.tempo)
            
            setupItem(view.findViewById(R.id.item_placa), "Placa:", it.placa)
            setupItem(view.findViewById(R.id.item_marca), "Marca:", it.marca)
            setupItem(view.findViewById(R.id.item_modelo), "Modelo:", it.modelo)
            setupItem(view.findViewById(R.id.item_cor), "Cor:", it.cor)
            
            setupItem(view.findViewById(R.id.item_valor), "Valor:", it.valor)
            setupItem(view.findViewById(R.id.item_desconto), "Desconto:", it.desconto)
            setupItem(view.findViewById(R.id.item_valor_total), "Total:", it.total)
        }
    }

    private fun setupItem(view: View, label: String, value: String?) {
        val labelTextView = view.findViewById<TextView>(R.id.label)
        val valueTextView = view.findViewById<TextView>(R.id.valor)
        labelTextView.text = label
        valueTextView.text = value
    }

    companion object {
        fun newInstance(item: HistoricoItem): DetalhesBottomSheetFragment {
            val fragment = DetalhesBottomSheetFragment()
            val args = Bundle()
            args.putSerializable("historico_item", item)
            fragment.arguments = args
            return fragment
        }
    }
}
