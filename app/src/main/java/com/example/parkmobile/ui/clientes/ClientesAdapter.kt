package com.example.parkmobile.ui.clientes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.R
import com.example.parkmobile.data.model.Cliente

class ClientesAdapter(private val items: List<Cliente>) : RecyclerView.Adapter<ClientesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_result_clientes, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvId.text = item.id.toString()
        holder.tvNome.text = item.nome
        holder.tvCpf.text = item.cpf
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvId: TextView = view.findViewById(R.id.tv_id_cliente)
        val tvNome: TextView = view.findViewById(R.id.tv_nome_cliente)
        val tvCpf: TextView = view.findViewById(R.id.tv_cpf_cliente)
    }

}