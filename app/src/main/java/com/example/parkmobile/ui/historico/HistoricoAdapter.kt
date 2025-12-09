package com.example.parkmobile.ui.historico

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.R
import com.example.parkmobile.data.model.HistoricoEstacionamento
import java.text.SimpleDateFormat
import java.util.Locale

class HistoricoAdapter(
    private val onItemClick: (HistoricoEstacionamento) -> Unit
) : ListAdapter<HistoricoEstacionamento, HistoricoAdapter.HistoricoViewHolder>(HistoricoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoricoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_historico, parent, false)
        return HistoricoViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoricoViewHolder, position: Int) {
        val historico = getItem(position)
        holder.bind(historico)
    }

    inner class HistoricoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvReciboNumero: TextView = itemView.findViewById(R.id.tv_recibo_numero)
        private val tvPlaca: TextView = itemView.findViewById(R.id.tv_placa_veiculo_historico)
        private val tvData: TextView = itemView.findViewById(R.id.tv_data_checkout)

        fun bind(historico: HistoricoEstacionamento) {
            tvReciboNumero.text = "Recibo: ${historico.recibo}"
            tvPlaca.text = "Placa: ${historico.placaVeiculo}"
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            tvData.text = "Data: ${historico.checkOut?.let { sdf.format(it) } ?: "N/A"}"

            itemView.setOnClickListener {
                onItemClick(historico)
            }
        }
    }
}

class HistoricoDiffCallback : DiffUtil.ItemCallback<HistoricoEstacionamento>() {
    override fun areItemsTheSame(oldItem: HistoricoEstacionamento, newItem: HistoricoEstacionamento): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: HistoricoEstacionamento, newItem: HistoricoEstacionamento): Boolean {
        return oldItem == newItem
    }
}
