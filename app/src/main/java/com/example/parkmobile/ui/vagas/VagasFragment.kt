package com.example.parkmobile.ui.vagas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class VagasFragment : Fragment() {

    private val viewModel: VagasViewModel by viewModels { VagasViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vagas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvVagas = view.findViewById<RecyclerView>(R.id.rv_vagas)
        val fabAddVaga = view.findViewById<FloatingActionButton>(R.id.fab_add_vaga)

        val vagasAdapter = VagasAdapter { vaga ->
            // Ao clicar, abre a tela de edição
            AddEditVagaFragment.newInstance(vaga).show(childFragmentManager, "AddEditVagaFragment")
        }

        rvVagas.adapter = vagasAdapter
        rvVagas.layoutManager = GridLayoutManager(context, 2) // 2 colunas

        viewModel.vagas.observe(viewLifecycleOwner) { vagas ->
            vagasAdapter.submitList(vagas)
        }

        fabAddVaga.setOnClickListener {
            // Ao clicar no FAB, abre a tela de adição (sem passar vaga)
            AddEditVagaFragment.newInstance(null).show(childFragmentManager, "AddEditVagaFragment")
        }

        // Carrega as vagas quando a tela é criada
        viewModel.carregarVagas()
    }
}
