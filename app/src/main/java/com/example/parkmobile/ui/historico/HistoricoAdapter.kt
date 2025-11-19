package com.example.parkmobile.ui.historico

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.R
import com.example.parkmobile.data.model.HistoricoItem

class HistoricoAdapter(
    private var historicoList: List<HistoricoItem>,
    private val onItemClick: (HistoricoItem) -> Unit
) : RecyclerView.Adapter<HistoricoAdapter.HistoricoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoricoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_historico, parent, false)
        return HistoricoViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoricoViewHolder, position: Int) {
        val item = historicoList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = historicoList.size

    fun updateData(newData: List<HistoricoItem>) {
        historicoList = newData
        notifyDataSetChanged()
    }

    inner class HistoricoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewCodigo: TextView = itemView.findViewById(R.id.textViewCodigo)
        private val textViewVaga: TextView = itemView.findViewById(R.id.textViewVaga)
        private val textViewEntrada: TextView = itemView.findViewById(R.id.textViewEntrada)
        private val textViewSaida: TextView = itemView.findViewById(R.id.textViewSaida)

        init {
            itemView.setOnClickListener {
                onItemClick(historicoList[adapterPosition])
            }
        }

        fun bind(item: HistoricoItem) {
            textViewCodigo.text = item.codigo
            textViewVaga.text = item.vaga
            textViewEntrada.text = item.entrada
            textViewSaida.text = item.saida
        }
    }
}
