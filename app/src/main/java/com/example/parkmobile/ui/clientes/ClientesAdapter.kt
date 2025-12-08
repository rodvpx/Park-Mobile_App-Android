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

class ClientesAdapter(private val onItemClicked: (Cliente) -> Unit) :
    ListAdapter<Cliente, ClientesAdapter.ClienteViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_result_clientes, parent, false)
        return ClienteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        val cliente = getItem(position)
        holder.bind(cliente)
        holder.itemView.setOnClickListener { onItemClicked(cliente) }
    }

    class ClienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nomeTextView: TextView = itemView.findViewById(R.id.tv_nome_cliente)
        private val cpfTextView: TextView = itemView.findViewById(R.id.tv_cpf_cliente)
        private val idTextView: TextView = itemView.findViewById(R.id.tv_id_cliente)

        fun bind(cliente: Cliente) {
            nomeTextView.text = cliente.nome
            cpfTextView.text = formatarCpf(cliente.cpf)
            idTextView.text = cliente.id
        }

        private fun formatarCpf(cpf: String): String {
            return if (cpf.length == 11) {
                cpf.replaceFirst(Regex("(\\d{3})(\\d{3})(\\d{3})(\\d{2})"), "$1.$2.$3-$4")
            } else {
                cpf
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Cliente>() {
        override fun areItemsTheSame(oldItem: Cliente, newItem: Cliente): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Cliente, newItem: Cliente): Boolean {
            return oldItem == newItem
        }
    }
}
