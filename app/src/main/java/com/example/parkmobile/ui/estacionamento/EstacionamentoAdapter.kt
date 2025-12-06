package com.example.parkmobile.ui.estacionamento

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.data.model.ClienteVaga
import com.example.parkmobile.databinding.ItemCheckOutBinding

class EstacionamentoAdapter(private val onCheckOutClick: (ClienteVaga) -> Unit) : ListAdapter<ClienteVaga, EstacionamentoAdapter.EstacionamentoViewHolder>(ClienteVagaDiffCallback()) {

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
            binding.clienteVaga = clienteVaga
            binding.clickListener = View.OnClickListener { onCheckOutClick(clienteVaga) }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): EstacionamentoViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemCheckOutBinding.inflate(layoutInflater, parent, false)
                return EstacionamentoViewHolder(binding)
            }
        }
    }
}

class ClienteVagaDiffCallback : DiffUtil.ItemCallback<ClienteVaga>() {
    override fun areItemsTheSame(oldItem: ClienteVaga, newItem: ClienteVaga): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ClienteVaga, newItem: ClienteVaga): Boolean {
        return oldItem == newItem
    }
}
