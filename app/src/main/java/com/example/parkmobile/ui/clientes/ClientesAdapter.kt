package com.example.parkmobile.ui.clientes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.data.model.Cliente
import com.example.parkmobile.databinding.ItemResultClientesBinding

class ClientesAdapter(private val onItemClick: (Cliente) -> Unit) :
    ListAdapter<Cliente, ClientesAdapter.ClienteViewHolder>(ClienteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val binding = ItemResultClientesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClienteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        val cliente = getItem(position)
        holder.bind(cliente, onItemClick)
    }

    class ClienteViewHolder(private val binding: ItemResultClientesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cliente: Cliente, onItemClick: (Cliente) -> Unit) {
            binding.root.setOnClickListener { onItemClick(cliente) }

            binding.tvIdCliente.text = cliente.id
            binding.tvNomeCliente.text = cliente.nome
            binding.tvCpfCliente.text = cliente.cpf
        }
    }
}

class ClienteDiffCallback : DiffUtil.ItemCallback<Cliente>() {
    override fun areItemsTheSame(oldItem: Cliente, newItem: Cliente): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Cliente, newItem: Cliente): Boolean {
        return oldItem == newItem
    }
}
