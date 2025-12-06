package com.example.parkmobile.ui.vagas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.parkmobile.data.model.Vaga
import com.example.parkmobile.databinding.BottomSheetAddVagaBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddVagaBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetAddVagaBinding? = null
    private val binding get() = _binding!!

    // Use activityViewModels() para compartilhar o ViewModel com o VagasFragment
    private val viewModel: VagasViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAddVagaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.btnCadastrarVaga.setOnClickListener {
            val codigo = binding.etCodigoVaga.text.toString()
            val status = if (binding.rbLivre.isChecked) {
                Vaga.StatusVaga.LIVRE
            } else {
                Vaga.StatusVaga.OCUPADA
            }
            viewModel.addVaga(codigo, status)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (message.isNotBlank()) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.dismiss.observe(viewLifecycleOwner) { shouldDismiss ->
            if (shouldDismiss) {
                dismiss()
                viewModel.onDismissed() // Reseta o estado para não fechar novamente
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
