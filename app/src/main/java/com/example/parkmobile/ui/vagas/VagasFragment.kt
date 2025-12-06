package com.example.parkmobile.ui.vagas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.parkmobile.data.repository.VagaRepository
import com.example.parkmobile.databinding.FragmentVagasBinding
import com.google.firebase.firestore.FirebaseFirestore

class VagasFragment : Fragment() {

    private var _binding: FragmentVagasBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: VagasViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVagasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Configuração do ViewModel (sem Hilt)
        val firestore = FirebaseFirestore.getInstance()
        val vagaRepository = VagaRepository(firestore)
        val factory = VagasViewModelFactory(vagaRepository)
        viewModel = ViewModelProvider(this, factory)[VagasViewModel::class.java]

        // 2. Ligar o ViewModel e o LifecycleOwner ao DataBinding
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // 3. Configuração do RecyclerView
        val vagasAdapter = VagasAdapter { vaga ->
            val bottomSheet = EditVagaBottomSheetFragment.newInstance(vaga)
            bottomSheet.show(childFragmentManager, "EditVagaBottomSheetFragment")
        }
        binding.rvVagas.adapter = vagasAdapter

        // 4. Observar mudanças nos dados
        viewModel.vagas.observe(viewLifecycleOwner) { vagas ->
            vagasAdapter.submitList(vagas)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        // 5. Carregar os dados iniciais
        viewModel.carregarVagas()

        // 6. Configuração do FAB
        binding.fabAddVaga.setOnClickListener {
            val bottomSheet = AddVagaBottomSheetFragment()
            bottomSheet.show(childFragmentManager, "AddVagaBottomSheetFragment")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Evitar memory leaks
    }
}
