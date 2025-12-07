package com.example.parkmobile.ui.estacionamento

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.R
import com.example.parkmobile.data.model.ClienteVaga
import java.text.SimpleDateFormat
import java.util.*

class CheckOutAdapter(private val onCheckOutClick: (ClienteVaga) -> Unit) : ListAdapter<ClienteVaga, CheckOutAdapter.CheckOutViewHolder>(ClienteVagaDiffCallback()) {

    class CheckOutViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val tvPlaca: TextView = itemView.findViewById(R.id.tv_placa)
        private val tvModeloMarca: TextView = itemView.findViewById(R.id.tv_modelo_marca)
        private val tvDataEntrada: TextView = itemView.findViewById(R.id.tv_data_entrada)
        private val btnFazerCheckout: Button = itemView.findViewById(R.id.btn_fazer_checkout)

        fun bind(item: ClienteVaga, onCheckOutClick: (ClienteVaga) -> Unit) {
            tvPlaca.text = item.placa
            tvModeloMarca.text = "${item.modelo} - ${item.marca}"

            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            tvDataEntrada.text = sdf.format(item.dataEntrada)

            btnFazerCheckout.setOnClickListener { onCheckOutClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckOutViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_check_out, parent, false)
        return CheckOutViewHolder(view)
    }

    override fun onBindViewHolder(holder: CheckOutViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onCheckOutClick)
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
