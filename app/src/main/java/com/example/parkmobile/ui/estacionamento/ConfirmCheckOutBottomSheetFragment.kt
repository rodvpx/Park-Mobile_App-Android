package com.example.parkmobile.ui.estacionamento

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.parkmobile.data.model.ClienteVaga
import com.example.parkmobile.databinding.BottomSheetConfirmCheckoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.*

class ConfirmCheckOutBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetConfirmCheckoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EstacionamentoViewModel by activityViewModels {
        EstacionamentoViewModelFactory()
    }

    private var clienteVaga: ClienteVaga? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            clienteVaga = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(ARG_CLIENTE_VAGA, ClienteVaga::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.getParcelable(ARG_CLIENTE_VAGA)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetConfirmCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clienteVaga?.let {
            binding.tvConfirmPlaca.text = "Placa: ${it.placa}"
            binding.tvConfirmModeloMarca.text = "${it.modelo} - ${it.marca}"
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            binding.tvConfirmEntrada.text = "Entrada: ${sdf.format(it.dataEntrada)}"
        }

        binding.btnCancelar.setOnClickListener { dismiss() }

        binding.btnConfirmar.setOnClickListener {
            clienteVaga?.recibo?.let {
                viewModel.realizarCheckOut(it)
                // O observer no fragment pai cuidará de mostrar o Toast e atualizar a lista
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_CLIENTE_VAGA = "clienteVaga"

        fun newInstance(clienteVaga: ClienteVaga): ConfirmCheckOutBottomSheetFragment {
            val fragment = ConfirmCheckOutBottomSheetFragment()
            val args = Bundle()
            args.putParcelable(ARG_CLIENTE_VAGA, clienteVaga)
            fragment.arguments = args
            return fragment
        }
    }
}
