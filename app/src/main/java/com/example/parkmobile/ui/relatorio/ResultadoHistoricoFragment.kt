package com.example.parkmobile.ui.relatorio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.R
import com.example.parkmobile.data.model.HistoricoItem

class ResultadoHistoricoFragment : Fragment() {

    private var historicoList: ArrayList<HistoricoItem>? = null
    private lateinit var rvHistorico: RecyclerView
    private lateinit var adapter: HistoricoRelatorioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            @Suppress("DEPRECATION")
            historicoList = it.getSerializable(ARG_HISTORICO) as? ArrayList<HistoricoItem>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_resultado_historico, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvHistorico = view.findViewById(R.id.rv_historico)
        rvHistorico.layoutManager = LinearLayoutManager(requireContext())

        historicoList?.let { list ->
            adapter = HistoricoRelatorioAdapter(list) { item ->
                // Quando clicar em "Detalhes", navegar para a tela de detalhes
                val fragment = ResultadoReciboFragment.newInstance(item)
                (activity as? RelatoriosActivity)?.loadFragment(
                    fragment,
                    "Detalhes do Recibo",
                    false
                )
            }
            rvHistorico.adapter = adapter
        }
    }

    companion object {
        private const val ARG_HISTORICO = "historico"

        fun newInstance(historico: ArrayList<HistoricoItem>) =
            ResultadoHistoricoFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_HISTORICO, historico)
                }
            }
    }
}

