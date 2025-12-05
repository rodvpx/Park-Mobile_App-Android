package com.example.parkmobile.ui.relatorio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.R
import com.example.parkmobile.data.model.HistoricoItem

class ReciboAdapter(
    private val recibos: List<HistoricoItem>,
    private val onReciboClick: (HistoricoItem) -> Unit
) : RecyclerView.Adapter<ReciboAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val codigo: TextView = view.findViewById(R.id.textViewCodigo)
        val vaga: TextView = view.findViewById(R.id.textViewVaga)
        val entrada: TextView = view.findViewById(R.id.textViewEntrada)
        val saida: TextView = view.findViewById(R.id.textViewSaida)
        val detalhes: TextView = view.findViewById(R.id.buttonDetalhes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_historico, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recibo = recibos[position]
        holder.codigo.text = recibo.codigo
        holder.vaga.text = recibo.vaga
        holder.entrada.text = recibo.entrada
        holder.saida.text = recibo.saida

        holder.detalhes.setOnClickListener {
            onReciboClick(recibo)
        }
    }

    override fun getItemCount() = recibos.size
}
