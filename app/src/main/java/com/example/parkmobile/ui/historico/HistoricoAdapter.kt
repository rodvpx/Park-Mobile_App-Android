package com.example.parkmobile.ui.historico

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.data.model.ClienteVaga
import com.example.parkmobile.databinding.ItemHistoricoBinding
import java.text.SimpleDateFormat
import java.util.Locale

class HistoricoAdapter(private val onItemClick: (ClienteVaga) -> Unit) :
    ListAdapter<ClienteVaga, HistoricoAdapter.HistoricoViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoricoViewHolder {
        val binding = ItemHistoricoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoricoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoricoViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onItemClick)
    }

    class HistoricoViewHolder(private val binding: ItemHistoricoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ClienteVaga, onItemClick: (ClienteVaga) -> Unit) {
            // Define o clique na view raiz do item
            binding.root.setOnClickListener { onItemClick(item) }

            // Preenche os dados manualmente usando os IDs do View Binding
            binding.tvPlaca.text = item.placa
            binding.tvRecibo.text = "Recibo: ${item.recibo}"

            val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            binding.tvDataEntrada.text = item.dataEntrada?.let { "Entrada: ${format.format(it)}" } ?: "Entrada: --"
            binding.tvDataSaida.text = item.dataSaida?.let { "Saída: ${format.format(it)}" } ?: "Saída: --"

            val valorSeguro = item.valor ?: 0.0
            binding.tvValor.text = String.format(Locale.getDefault(), "R$ %.2f", valorSeguro)
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
