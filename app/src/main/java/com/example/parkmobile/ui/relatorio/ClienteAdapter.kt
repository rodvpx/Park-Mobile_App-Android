package com.example.parkmobile.ui.relatorio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.R
import com.example.parkmobile.data.model.Cliente

class ClienteAdapter(
    private val clientes: List<Cliente>,
    private val onClienteClick: (Cliente) -> Unit
) : RecyclerView.Adapter<ClienteAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val id: TextView = view.findViewById(R.id.tv_id_cliente)
        val nome: TextView = view.findViewById(R.id.tv_nome_cliente)
        val cpf: TextView = view.findViewById(R.id.tv_cpf_cliente)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_result_clientes, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cliente = clientes[position]
        holder.id.text = cliente.id.toString()
        holder.nome.text = cliente.nome
        holder.cpf.text = cliente.cpf

        holder.itemView.setOnClickListener {
            onClienteClick(cliente)
        }
    }

    override fun getItemCount() = clientes.size
}
