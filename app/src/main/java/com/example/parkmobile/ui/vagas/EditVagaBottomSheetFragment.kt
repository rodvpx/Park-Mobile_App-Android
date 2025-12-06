package com.example.parkmobile.ui.vagas

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.parkmobile.data.model.Vaga
import com.example.parkmobile.databinding.BottomSheetEditVagaBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditVagaBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetEditVagaBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VagasViewModel by activityViewModels()

    private var vaga: Vaga? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            vaga = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable("vaga", Vaga::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.getParcelable("vaga")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetEditVagaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vaga = vaga

        // Pre-seleciona o RadioButton correto
        if (vaga?.status == Vaga.StatusVaga.LIVRE.name) {
            binding.rbLivre.isChecked = true
        } else {
            binding.rbOcupado.isChecked = true
        }

        binding.btnSalvarVaga.setOnClickListener {
            vaga?.let {
                val novoCodigo = binding.etCodigoVaga.text.toString()
                val novoStatus = if (binding.rbLivre.isChecked) {
                    Vaga.StatusVaga.LIVRE
                } else {
                    Vaga.StatusVaga.OCUPADA
                }
                viewModel.updateVaga(it, novoCodigo, novoStatus)
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (message.isNotBlank()) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.dismiss.observe(viewLifecycleOwner) { shouldDismiss ->
            if (shouldDismiss) {
                dismiss()
                viewModel.onDismissed()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(vaga: Vaga): EditVagaBottomSheetFragment {
            val fragment = EditVagaBottomSheetFragment()
            val args = Bundle()
            args.putParcelable("vaga", vaga)
            fragment.arguments = args
            return fragment
        }
    }
}
