package com.example.parkmobile.ui.relatorio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.R
import com.example.parkmobile.data.model.HistoricoItem
class HistoricoAdapter(
    private val historicoList: List<HistoricoItem>,
    private val onDetalhesClick: (HistoricoItem) -> Unit
) : RecyclerView.Adapter<HistoricoAdapter.HistoricoViewHolder>() {

    class HistoricoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCodigo: TextView = itemView.findViewById(R.id.textViewCodigo)
        val tvVaga: TextView = itemView.findViewById(R.id.textViewVaga)
        val tvEntrada: TextView = itemView.findViewById(R.id.textViewEntrada)
        val tvSaida: TextView = itemView.findViewById(R.id.textViewSaida)
        val btnDetalhes: TextView = itemView.findViewById(R.id.buttonDetalhes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoricoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_historico, parent, false)
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

