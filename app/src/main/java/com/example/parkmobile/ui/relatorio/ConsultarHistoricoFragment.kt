package com.example.parkmobile.ui.relatorio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.parkmobile.R
import com.example.parkmobile.data.model.HistoricoItem
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.button.MaterialButton

class ConsultarHistoricoFragment : Fragment() {

    private lateinit var etNumeroCpf: TextInputEditText
    private lateinit var btnBuscar: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_consultar_historico, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etNumeroCpf = view.findViewById(R.id.et_numero_cpf)
        btnBuscar = view.findViewById(R.id.btn_buscar)

        btnBuscar.setOnClickListener {
            val numeroCpf = etNumeroCpf.text.toString()

            if (numeroCpf.isBlank()) {
                Toast.makeText(requireContext(), "Digite o número do CPF", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val historico = listOf(
                HistoricoItem(
                    codigo = "#1525626",
                    cpf = numeroCpf,
                    vaga = "A-05",
                    entrada = "04/10/25 - 15:00hrs",
                    saida = "04/10/2025 - 16:30hrs",
                    tempo = "1h 30min",
                    placa = "ABC3456",
                    marca = "Mitsubishi",
                    modelo = "L200",
                    cor = "Branco",
                    valor = "R$ 40,00",
                    desconto = "R$ 05,00",
                    total = "R$ 45,00"
                ),
                HistoricoItem(
                    codigo = "#000000",
                    cpf = numeroCpf,
                    vaga = "A-05",
                    entrada = "00/00/00 - 00:00hrs",
                    saida = "00/00/00 - 00:00hrs",
                    tempo = "0h 00min",
                    placa = "XYZ9876",
                    marca = "Toyota",
                    modelo = "Corolla",
                    cor = "Prata",
                    valor = "R$ 30,00",
                    desconto = "R$ 00,00",
                    total = "R$ 30,00"
                )
            )

            val fragment = ResultadoHistoricoFragment.newInstance(ArrayList(historico))

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
}
