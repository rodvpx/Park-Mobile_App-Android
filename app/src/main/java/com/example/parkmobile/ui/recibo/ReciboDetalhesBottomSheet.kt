package com.example.parkmobile.ui.recibo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.parkmobile.data.model.ClienteVaga
import com.example.parkmobile.databinding.BottomSheetReciboDetalhesBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.Locale

class ReciboDetalhesBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetReciboDetalhesBinding? = null
    private val binding get() = _binding!!

    private var clienteVaga: ClienteVaga? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            clienteVaga = it.getParcelable(ARG_CLIENTE_VAGA)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetReciboDetalhesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clienteVaga?.let { item ->
            binding.tvReciboDetalhes.text = "Recibo: ${item.recibo}"
            binding.tvPlacaDetalhes.text = "Placa: ${item.placa}"

            val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            binding.tvDataEntradaDetalhes.text = item.dataEntrada?.let { "Entrada: ${format.format(it)}" } ?: "Entrada: --"
            binding.tvDataSaidaDetalhes.text = item.dataSaida?.let { "Saída: ${format.format(it)}" } ?: "Saída: --"

            val valorSeguro = item.valor ?: 0.0
            binding.tvValorDetalhes.text = String.format(Locale.getDefault(), "R$ %.2f", valorSeguro)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
