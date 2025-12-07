package com.example.parkmobile.ui.vagas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.R
import com.example.parkmobile.data.repository.VagaRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class VagasFragment : Fragment() {

    private lateinit var viewModel: VagasViewModel

    private lateinit var rvVagas: RecyclerView
    private lateinit var fabAddVaga: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vagas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvVagas = view.findViewById(R.id.rv_vagas)
        fabAddVaga = view.findViewById(R.id.fab_add_vaga)

        // 1. Configuração do ViewModel (sem Hilt)
        val firestore = FirebaseFirestore.getInstance()
        val vagaRepository = VagaRepository(firestore)
        val factory = VagasViewModelFactory(vagaRepository)
        viewModel = ViewModelProvider(this, factory)[VagasViewModel::class.java]

        // 2. Configuração do RecyclerView
        val vagasAdapter = VagasAdapter { vaga ->
            val bottomSheet = EditVagaBottomSheetFragment.newInstance(vaga)
            bottomSheet.show(childFragmentManager, "EditVagaBottomSheetFragment")
        }
        rvVagas.adapter = vagasAdapter

        // 3. Observar mudanças nos dados
        viewModel.vagas.observe(viewLifecycleOwner) { vagas ->
            vagasAdapter.submitList(vagas)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        // 4. Carregar os dados iniciais
        viewModel.carregarVagas()

        // 5. Configuração do FAB
        fabAddVaga.setOnClickListener {
            val bottomSheet = AddVagaBottomSheetFragment()
            bottomSheet.show(childFragmentManager, "AddVagaBottomSheetFragment")
        }
    }
}
