package com.example.parkmobile.ui.vagas

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.R
import com.example.parkmobile.data.model.Vaga
import com.google.android.material.card.MaterialCardView

class VagasAdapter(private val onItemClick: (Vaga) -> Unit) : ListAdapter<Vaga, VagasAdapter.VagaViewHolder>(VagaDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VagaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vagas, parent, false)
        return VagaViewHolder(view)
    }

    override fun onBindViewHolder(holder: VagaViewHolder, position: Int) {
        val vaga = getItem(position)
        holder.bind(vaga, onItemClick)
    }

    class VagaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardVaga: MaterialCardView = itemView.findViewById(R.id.card_vaga)
        private val tvVaga: TextView = itemView.findViewById(R.id.tv_vaga)

        fun bind(vaga: Vaga, onItemClick: (Vaga) -> Unit) {
            tvVaga.text = vaga.codigo
            itemView.setOnClickListener { onItemClick(vaga) }

            val backgroundColor = when (vaga.status) {
                "Livre" -> R.color.vaga_disponivel
                "Ocupada" -> R.color.vaga_ocupada
                else -> R.color.vaga_interditada
            }
            cardVaga.setCardBackgroundColor(ContextCompat.getColor(itemView.context, backgroundColor))
        }
    }
}

class VagaDiffCallback : DiffUtil.ItemCallback<Vaga>() {
    override fun areItemsTheSame(oldItem: Vaga, newItem: Vaga): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Vaga, newItem: Vaga): Boolean {
        return oldItem == newItem
    }
}
