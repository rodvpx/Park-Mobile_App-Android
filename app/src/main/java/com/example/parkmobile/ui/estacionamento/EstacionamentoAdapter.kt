package com.example.parkmobile.ui.estacionamento

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.data.model.ClienteVaga
import com.example.parkmobile.databinding.ItemCheckOutBinding
import java.text.SimpleDateFormat
import java.util.*

class EstacionamentoAdapter(private val onCheckOutClick: (ClienteVaga) -> Unit) : ListAdapter<ClienteVaga, EstacionamentoAdapter.EstacionamentoViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstacionamentoViewHolder {
        return EstacionamentoViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: EstacionamentoViewHolder, position: Int) {
        val clienteVaga = getItem(position)
        holder.bind(clienteVaga, onCheckOutClick)
    }

    class EstacionamentoViewHolder private constructor(private val binding: ItemCheckOutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clienteVaga: ClienteVaga, onCheckOutClick: (ClienteVaga) -> Unit) {
            binding.tvPlaca.text = clienteVaga.placa
            binding.tvModeloMarca.text = "${clienteVaga.modelo} - ${clienteVaga.marca}"

            // Formatar a data para exibição
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            binding.tvDataEntrada.text = sdf.format(clienteVaga.dataEntrada)

            binding.btnFazerCheckout.setOnClickListener { onCheckOutClick(clienteVaga) }
        }

        companion object {
            fun from(parent: ViewGroup): EstacionamentoViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemCheckOutBinding.inflate(layoutInflater, parent, false)
                return EstacionamentoViewHolder(binding)
            }
        }
    }

    private companion object DiffCallback : DiffUtil.ItemCallback<ClienteVaga>() {
        override fun areItemsTheSame(oldItem: ClienteVaga, newItem: ClienteVaga): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ClienteVaga, newItem: ClienteVaga): Boolean {
            return oldItem == newItem
        }
    }
}
