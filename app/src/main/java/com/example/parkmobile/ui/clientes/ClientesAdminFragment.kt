package com.example.parkmobile.ui.clientes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.R
import com.example.parkmobile.data.model.Cliente
import com.example.parkmobile.data.repository.ClienteRepository

class ClientesAdminFragment : Fragment() {

    private lateinit var clienteRepository: ClienteRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clientes_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvClientes = view.findViewById<RecyclerView>(R.id.rv_clientes)

        // Initialize repository and get data
        clienteRepository = ClienteRepository()
        val clientes = clienteRepository.getClientesItems()

        rvClientes.adapter = ClientesAdapter(clientes)
    }

    class ClientesAdapter(private val clientes: List<Cliente>) : RecyclerView.Adapter<ClientesAdapter.ViewHolder>() {

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
        }

        override fun getItemCount() = clientes.size
    }
}
