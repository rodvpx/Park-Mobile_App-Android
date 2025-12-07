package com.example.parkmobile.ui.historico

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.R
import com.example.parkmobile.data.model.ClienteVaga
import java.text.SimpleDateFormat
import java.util.Locale

class HistoricoAdapter(private val onItemClick: (ClienteVaga) -> Unit) :
    ListAdapter<ClienteVaga, HistoricoAdapter.HistoricoViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoricoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_historico, parent, false)
        return HistoricoViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoricoViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onItemClick)
    }

    class HistoricoViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val tvPlaca: TextView = itemView.findViewById(R.id.tv_placa)
        private val tvRecibo: TextView = itemView.findViewById(R.id.tv_recibo)
        private val tvDataEntrada: TextView = itemView.findViewById(R.id.tv_data_entrada)
        private val tvDataSaida: TextView = itemView.findViewById(R.id.tv_data_saida)
        private val tvValor: TextView = itemView.findViewById(R.id.tv_valor)

        fun bind(item: ClienteVaga, onItemClick: (ClienteVaga) -> Unit) {
            // Define o clique na view raiz do item
            itemView.setOnClickListener { onItemClick(item) }

            // Preenche os dados manualmente usando os IDs
            tvPlaca.text = item.placa
            tvRecibo.text = "Recibo: ${item.recibo}"

            val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            tvDataEntrada.text = item.dataEntrada?.let { "Entrada: ${format.format(it)}" } ?: "Entrada: --"
            tvDataSaida.text = item.dataSaida?.let { "Saída: ${format.format(it)}" } ?: "Saída: --"

            val valorSeguro = item.valor ?: 0.0
            tvValor.text = String.format(Locale.getDefault(), "R$ %.2f", valorSeguro)
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ClienteVaga>() {
        override fun areItemsTheSame(oldItem: ClienteVaga, newItem: ClienteVaga): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ClienteVaga, newItem: ClienteVaga): Boolean {
            return oldItem == newItem
        }
    }
}
