package com.example.parkmobile.ui.clientes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.R
import com.example.parkmobile.data.model.Cliente

class ClientesAdapter(private val onItemClick: (Cliente) -> Unit) :
    ListAdapter<Cliente, ClientesAdapter.ClienteViewHolder>(ClienteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_result_clientes, parent, false)
        return ClienteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        val cliente = getItem(position)
        holder.bind(cliente, onItemClick)
    }

    class ClienteViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val tvIdCliente: TextView = itemView.findViewById(R.id.tv_id_cliente)
        private val tvNomeCliente: TextView = itemView.findViewById(R.id.tv_nome_cliente)
        private val tvCpfCliente: TextView = itemView.findViewById(R.id.tv_cpf_cliente)

        fun bind(cliente: Cliente, onItemClick: (Cliente) -> Unit) {
            itemView.setOnClickListener { onItemClick(cliente) }

            tvIdCliente.text = cliente.id
            tvNomeCliente.text = cliente.nome
            tvCpfCliente.text = cliente.cpf
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
