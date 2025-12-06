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

class CheckOutAdapter(private val onCheckOutClick: (ClienteVaga) -> Unit) : ListAdapter<ClienteVaga, CheckOutAdapter.CheckOutViewHolder>(ClienteVagaDiffCallback()) {

    class CheckOutViewHolder private constructor(private val binding: ItemCheckOutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ClienteVaga, onCheckOutClick: (ClienteVaga) -> Unit) {
            binding.tvPlaca.text = item.placa
            binding.tvModeloMarca.text = "${item.modelo} - ${item.marca}"

            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            binding.tvDataEntrada.text = sdf.format(item.dataEntrada)

            binding.btnFazerCheckout.setOnClickListener { onCheckOutClick(item) }
        }

        companion object {
            fun from(parent: ViewGroup): CheckOutViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemCheckOutBinding.inflate(layoutInflater, parent, false)
                return CheckOutViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckOutViewHolder {
        return CheckOutViewHolder.from(parent)
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
