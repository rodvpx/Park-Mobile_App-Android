package com.example.parkmobile.ui.relatorio

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.R
import com.example.parkmobile.data.model.HistoricoItem
import com.example.parkmobile.ui.historico.DetalhesBottomSheetFragment

class ResultadoHistoricoFragment : Fragment() {

    private var historicoList: ArrayList<HistoricoItem>? = null
    private lateinit var rvHistorico: RecyclerView
    private lateinit var adapter: HistoricoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            historicoList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelableArrayList(ARG_HISTORICO, HistoricoItem::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.getParcelableArrayList(ARG_HISTORICO)
            }
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
            adapter = HistoricoAdapter(list) { item ->
                val bottomSheet = DetalhesBottomSheetFragment.newInstance(item)
                bottomSheet.show(parentFragmentManager, "DetalhesBottomSheetFragment")
            }
            rvHistorico.adapter = adapter
        }
    }

    companion object {
        private const val ARG_HISTORICO = "historico"

        fun newInstance(historico: ArrayList<HistoricoItem>) =
            ResultadoHistoricoFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_HISTORICO, historico)
                }
            }
    }
}
