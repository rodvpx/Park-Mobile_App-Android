package com.example.parkmobile.ui.relatorio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.parkmobile.R
import com.example.parkmobile.data.model.HistoricoItem
import com.google.android.material.textfield.TextInputEditText

class ConsultarReciboFragment : Fragment() {

    private lateinit var etNumeroRecibo: TextInputEditText
    private lateinit var btnBuscar: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_consultar_recibo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etNumeroRecibo = view.findViewById(R.id.et_numero_recibo)
        btnBuscar = view.findViewById(R.id.btn_buscar)

        btnBuscar.setOnClickListener {
            val numeroRecibo = etNumeroRecibo.text.toString()

            if (numeroRecibo.isBlank()) {
                Toast.makeText(requireContext(), "Digite o número do recibo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Simular dados do recibo (aqui você faria a busca real)
            val recibo = HistoricoItem(
                codigo = "#$numeroRecibo",
                cpf = "455.889.554-05",
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
            )

            // Navegar para a tela de resultado
            val fragment = ResultadoReciboFragment.newInstance(recibo)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
}
