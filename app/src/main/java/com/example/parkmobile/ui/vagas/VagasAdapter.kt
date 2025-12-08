package com.example.parkmobile.ui.vagas

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.R
import com.example.parkmobile.data.model.Vaga

class VagasAdapter(private val onItemClicked: (Vaga) -> Unit) :
    ListAdapter<Vaga, VagasAdapter.VagaViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VagaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vagas, parent, false)
        return VagaViewHolder(view)
    }

    override fun onBindViewHolder(holder: VagaViewHolder, position: Int) {
        val vaga = getItem(position)
        holder.bind(vaga)
        holder.itemView.setOnClickListener { onItemClicked(vaga) }
    }

    class VagaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val codigoTextView: TextView = itemView.findViewById(R.id.tv_vaga)
        private val cardView: CardView = itemView.findViewById(R.id.card_vaga)

        fun bind(vaga: Vaga) {
            codigoTextView.text = vaga.codigo
            val context = itemView.context
            val color = if (vaga.status == Vaga.StatusVaga.OCUPADA.name) {
                ContextCompat.getColor(context, R.color.vaga_ocupada)
            } else {
                ContextCompat.getColor(context, R.color.vaga_disponivel)
            }
            cardView.setCardBackgroundColor(color)
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Vaga>() {
        override fun areItemsTheSame(oldItem: Vaga, newItem: Vaga): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Vaga, newItem: Vaga): Boolean {
            return oldItem == newItem
        }
    }
}
