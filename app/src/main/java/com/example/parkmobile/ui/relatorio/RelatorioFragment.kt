package com.example.parkmobile.ui.relatorio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.parkmobile.R

class RelatorioFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_relatorios, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnGerarRelatorio: Button = view.findViewById(R.id.btn_gerarRelatorio)
        btnGerarRelatorio.setOnClickListener {
            // Lógica para gerar o PDF
            Toast.makeText(requireContext(), "Gerando relatório em PDF...", Toast.LENGTH_SHORT).show()
        }
    }
}
