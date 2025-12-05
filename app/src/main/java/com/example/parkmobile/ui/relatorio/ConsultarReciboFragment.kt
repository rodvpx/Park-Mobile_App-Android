package com.example.parkmobile.ui.relatorio

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.R
import com.example.parkmobile.data.model.HistoricoItem
import com.example.parkmobile.ui.historico.DetalhesBottomSheetFragment
import com.google.android.material.textfield.TextInputEditText

class ConsultarReciboFragment : Fragment() {

    private lateinit var etNumeroRecibo: TextInputEditText
    private lateinit var btnBuscar: Button
    private lateinit var rvRecibos: RecyclerView
    private lateinit var adapter: ReciboAdapter
    private val todosRecibos = mutableListOf<HistoricoItem>()
    private val recibosEncontrados = mutableListOf<HistoricoItem>()

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
        rvRecibos = view.findViewById(R.id.rv_recibos_resultado)

        // Configurar RecyclerView
        rvRecibos.layoutManager = LinearLayoutManager(requireContext())
        adapter = ReciboAdapter(recibosEncontrados) { recibo ->
            val bottomSheet = DetalhesBottomSheetFragment.newInstance(recibo)
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        }
        rvRecibos.adapter = adapter

        // Carrega a lista inicial e exibe
        carregarEExibirRecibosIniciais()

        btnBuscar.setOnClickListener {
            buscarRecibos()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun carregarEExibirRecibosIniciais() {
        // Simula o carregamento de uma lista completa de recibos (substitua por sua lógica de repositório)
        todosRecibos.clear()
        todosRecibos.addAll(
            listOf(
                HistoricoItem("#12345", "11122233344", "A-01", "10:00", "11:30", "1h 30m", "ABC-1234", "Marca A", "Modelo X", "Azul", "R$ 15,00", "R$ 0,00", "R$ 15,00"),
                HistoricoItem("#67890", "22233344455", "B-02", "14:00", "15:00", "1h 0m", "DEF-5678", "Marca B", "Modelo Y", "Preto", "R$ 10,00", "R$ 2,00", "R$ 8,00"),
                HistoricoItem("#11223", "33344455566", "C-03", "18:30", "20:00", "1h 30m", "GHI-9012", "Marca C", "Modelo Z", "Branco", "R$ 15,00", "R$ 5,00", "R$ 10,00")
            )
        )

        // Exibe todos os recibos inicialmente
        recibosEncontrados.clear()
        recibosEncontrados.addAll(todosRecibos)
        adapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun buscarRecibos() {
        val numeroRecibo = etNumeroRecibo.text.toString().trim()

        val recibosFiltrados = if (numeroRecibo.isBlank()) {
            todosRecibos // Se a busca estiver vazia, mostra todos
        } else {
            todosRecibos.filter { it.codigo.contains(numeroRecibo, ignoreCase = true) }
        }

        recibosEncontrados.clear()
        recibosEncontrados.addAll(recibosFiltrados)
        adapter.notifyDataSetChanged()

        if (recibosEncontrados.isEmpty() && numeroRecibo.isNotBlank()) {
            Toast.makeText(requireContext(), "Nenhum recibo encontrado", Toast.LENGTH_SHORT).show()
        }
    }
}
