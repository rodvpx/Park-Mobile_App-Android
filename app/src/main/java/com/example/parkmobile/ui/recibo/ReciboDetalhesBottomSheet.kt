package com.example.parkmobile.ui.recibo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.example.parkmobile.R
import com.example.parkmobile.data.model.HistoricoEstacionamento
import com.example.parkmobile.ui.relatorio.RelatorioViewModel
import com.example.parkmobile.ui.relatorio.RelatorioViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class ReciboDetalhesBottomSheet : BottomSheetDialogFragment() {

    private var historico: HistoricoEstacionamento? = null
    private val viewModel: RelatorioViewModel by activityViewModels { RelatorioViewModelFactory() }

    private lateinit var tvReciboDetalhes: TextView
    private lateinit var tvNomeClienteDetalhes: TextView
    private lateinit var tvCpfClienteDetalhes: TextView
    private lateinit var tvPlacaDetalhes: TextView
    private lateinit var tvMarcaModeloDetalhes: TextView
    private lateinit var tvCorDetalhes: TextView
    private lateinit var tvDataEntradaDetalhes: TextView
    private lateinit var tvDataSaidaDetalhes: TextView
    private lateinit var tvTempoPermanenciaDetalhes: TextView
    private lateinit var tvDescontoDetalhes: TextView
    private lateinit var tvValorDetalhes: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            historico = it.getParcelable(ARG_HISTORICO)
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
        setupViews(view)
        historico?.let {
            viewModel.buscarClientePorId(it.idCliente)
            observeCliente()
            bindHistoricoData(it)
        }
    }

    private fun setupViews(view: View) {
        tvReciboDetalhes = view.findViewById(R.id.tv_recibo_detalhes)
        tvNomeClienteDetalhes = view.findViewById(R.id.tv_nome_cliente_detalhes)
        tvCpfClienteDetalhes = view.findViewById(R.id.tv_cpf_cliente_detalhes)
        tvPlacaDetalhes = view.findViewById(R.id.tv_placa_detalhes)
        tvMarcaModeloDetalhes = view.findViewById(R.id.tv_marca_modelo_detalhes)
        tvCorDetalhes = view.findViewById(R.id.tv_cor_detalhes)
        tvDataEntradaDetalhes = view.findViewById(R.id.tv_data_entrada_detalhes)
        tvDataSaidaDetalhes = view.findViewById(R.id.tv_data_saida_detalhes)
        tvTempoPermanenciaDetalhes = view.findViewById(R.id.tv_tempo_permanencia_detalhes)
        tvDescontoDetalhes = view.findViewById(R.id.tv_desconto_detalhes)
        tvValorDetalhes = view.findViewById(R.id.tv_valor_detalhes)
    }

    private fun observeCliente() {
        viewModel.cliente.observe(viewLifecycleOwner) {
            it?.let {
                tvNomeClienteDetalhes.text = "Cliente: ${it.nome}"
                tvCpfClienteDetalhes.text = "CPF: ${it.cpf}"
            }
        }
    }

    private fun bindHistoricoData(item: HistoricoEstacionamento) {
        tvReciboDetalhes.text = "Recibo: ${item.recibo}"
        tvPlacaDetalhes.text = "Placa: ${item.placaVeiculo}"
        tvMarcaModeloDetalhes.text = "Veículo: ${item.marcaVeiculo} ${item.modeloVeiculo}"
        tvCorDetalhes.text = "Cor: ${item.corVeiculo}"

        val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        tvDataEntradaDetalhes.text = item.checkIn?.let { "Entrada: ${format.format(it)}" } ?: "Entrada: --"
        tvDataSaidaDetalhes.text = item.checkOut?.let { "Saída: ${format.format(it)}" } ?: "Saída: --"

        item.checkIn?.let { checkIn ->
            item.checkOut?.let { checkOut ->
                val diff = checkOut.time - checkIn.time
                val hours = TimeUnit.MILLISECONDS.toHours(diff)
                val minutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60
                tvTempoPermanenciaDetalhes.text = "Permanência: ${hours}h ${minutes}m"
            }
        }

        val desconto = item.descontoAplicado ?: 0.0
        tvDescontoDetalhes.text = String.format(Locale.getDefault(), "Desconto: R$ %.2f", desconto)

        val valorFinal = (item.valor ?: 0.0) - desconto
        tvValorDetalhes.text = String.format(Locale.getDefault(), "Valor Final: R$ %.2f", valorFinal)
    }

    companion object {
        private const val ARG_HISTORICO = "historico"

        fun newInstance(historico: HistoricoEstacionamento): ReciboDetalhesBottomSheet {
            return ReciboDetalhesBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_HISTORICO, historico)
                }
            }
        }
    }
}
