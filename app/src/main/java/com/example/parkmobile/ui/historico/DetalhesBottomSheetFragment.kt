package com.example.parkmobile.ui.historico

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.parkmobile.data.model.HistoricoItem
import com.example.parkmobile.databinding.FragmentReciboDetalhesBottomSheetBinding
import com.example.parkmobile.databinding.PartialReciboItemBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DetalhesBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentReciboDetalhesBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReciboDetalhesBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val item = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("historico_item", HistoricoItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable<HistoricoItem>("historico_item")
        }

        item?.let {
            // Popula o código do recibo
            binding.detalhesCodigoValor.text = it.codigo

            // Popula os itens restantes
            setupItem(binding.itemCpf, "CPF:", it.cpf)
            setupItem(binding.itemVaga, "Vaga:", it.vaga)
            setupItem(binding.itemEntrada, "Entrada:", it.entrada)
            setupItem(binding.itemSaida, "Saída:", it.saida)
            setupItem(binding.itemTempo, "Tempo:", it.tempo)

            setupItem(binding.itemPlaca, "Placa:", it.placa)
            setupItem(binding.itemMarca, "Marca:", it.marca)
            setupItem(binding.itemModelo, "Modelo:", it.modelo)
            setupItem(binding.itemCor, "Cor:", it.cor)

            setupItem(binding.itemValor, "Valor:", it.valor)
            setupItem(binding.itemDesconto, "Desconto:", it.desconto)
            setupItem(binding.itemValorTotal, "Total:", it.total)
        }
    }

    private fun setupItem(itemBinding: PartialReciboItemBinding, label: String, value: String?) {
        itemBinding.label.text = label
        itemBinding.valor.text = value
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(item: HistoricoItem): DetalhesBottomSheetFragment {
            val fragment = DetalhesBottomSheetFragment()
            val args = Bundle()
            args.putParcelable("historico_item", item)
            fragment.arguments = args
            return fragment
        }
    }
}
