package com.example.parkmobile.ui.recibo

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

class RecibosAdapter(
    private val onItemClick: (HistoricoEstacionamento) -> Unit
) : ListAdapter<HistoricoEstacionamento, RecibosAdapter.RecibosViewHolder>(RecibosDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecibosViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_historico, parent, false)
        return RecibosViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecibosViewHolder, position: Int) {
        val recibo = getItem(position)
        holder.bind(recibo)
    }

    inner class RecibosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

class RecibosDiffCallback : DiffUtil.ItemCallback<HistoricoEstacionamento>() {
    override fun areItemsTheSame(oldItem: HistoricoEstacionamento, newItem: HistoricoEstacionamento): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: HistoricoEstacionamento, newItem: HistoricoEstacionamento): Boolean {
        return oldItem == newItem
    }
}
