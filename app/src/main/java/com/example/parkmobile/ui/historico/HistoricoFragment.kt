package com.example.parkmobile.ui.historico

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.R

class HistoricoFragment : Fragment() {

    private val viewModel: HistoricoViewModel by viewModels()
    private lateinit var historicoAdapter: HistoricoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_historico, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView(view)

        viewModel.historicoItems.observe(viewLifecycleOwner) { items ->
            historicoAdapter.updateData(items)
        }

        viewModel.carregarHistorico()
    }

    private fun setupRecyclerView(view: View) {
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewHistorico)
        recyclerView.layoutManager = LinearLayoutManager(context)
        historicoAdapter = HistoricoAdapter(emptyList()) { item ->
            val bottomSheet = DetalhesBottomSheetFragment.newInstance(item)
            bottomSheet.show(parentFragmentManager, "DetalhesBottomSheetFragment")
        }
        recyclerView.adapter = historicoAdapter
    }
}
