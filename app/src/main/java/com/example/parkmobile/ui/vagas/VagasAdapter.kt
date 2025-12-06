package com.example.parkmobile.ui.vagas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.data.model.Vaga
import com.example.parkmobile.databinding.ItemVagasBinding

class VagasAdapter(private val onItemClick: (Vaga) -> Unit) : ListAdapter<Vaga, VagasAdapter.VagaViewHolder>(VagaDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VagaViewHolder {
        return VagaViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: VagaViewHolder, position: Int) {
        val vaga = getItem(position)
        holder.bind(vaga, onItemClick)
    }

    class VagaViewHolder private constructor(private val binding: ItemVagasBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(vaga: Vaga, onItemClick: (Vaga) -> Unit) {
            binding.vaga = vaga
            binding.root.setOnClickListener { onItemClick(vaga) }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): VagaViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemVagasBinding.inflate(layoutInflater, parent, false)
                return VagaViewHolder(binding)
            }
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
