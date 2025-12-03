package com.example.parkmobile.ui.relatorio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.R
import com.example.parkmobile.data.model.HistoricoItem

class HistoricoRelatorioAdapter(
    private val historicoList: List<HistoricoItem>,
    private val onDetalhesClick: (HistoricoItem) -> Unit
) : RecyclerView.Adapter<HistoricoRelatorioAdapter.HistoricoViewHolder>() {

    class HistoricoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCodigo: TextView = itemView.findViewById(R.id.tv_codigo)
        val tvVaga: TextView = itemView.findViewById(R.id.tv_vaga)
        val tvEntrada: TextView = itemView.findViewById(R.id.tv_entrada)
        val tvSaida: TextView = itemView.findViewById(R.id.tv_saida)
        val btnDetalhes: Button = itemView.findViewById(R.id.btn_detalhes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoricoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_historico_card, parent, false)
        return HistoricoViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoricoViewHolder, position: Int) {
        val item = historicoList[position]

        holder.tvCodigo.text = item.codigo
        holder.tvVaga.text = item.vaga
        holder.tvEntrada.text = item.entrada
        holder.tvSaida.text = item.saida

        holder.btnDetalhes.setOnClickListener {
            onDetalhesClick(item)
        }
    }

    override fun getItemCount() = historicoList.size
}

