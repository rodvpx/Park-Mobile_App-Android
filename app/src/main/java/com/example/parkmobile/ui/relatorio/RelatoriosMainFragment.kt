package com.example.parkmobile.ui.relatorio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.parkmobile.R

class RelatoriosMainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_relatorios_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnConsultarRecibo: Button = view.findViewById(R.id.btn_consultar_recibo)
        val btnConsultarHistorico: Button = view.findViewById(R.id.btn_consultar_historico)

        btnConsultarRecibo.setOnClickListener {
            (activity as? RelatoriosActivity)?.loadFragment(
                ConsultarReciboFragment(),
                "Consultar Recibo",
                false
            )
        }

        btnConsultarHistorico.setOnClickListener {
            (activity as? RelatoriosActivity)?.loadFragment(
                ConsultarHistoricoFragment(),
                "Consultar Histórico",
                false
            )
        }
    }
}

