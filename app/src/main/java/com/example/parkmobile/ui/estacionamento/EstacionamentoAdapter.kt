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
import com.example.parkmobile.data.model.HistoricoEstacionamento
import java.text.SimpleDateFormat
import java.util.Locale

class EstacionamentoAdapter(
    private val onCheckOutClick: (HistoricoEstacionamento) -> Unit
) : ListAdapter<HistoricoEstacionamento, EstacionamentoAdapter.EstacionamentoViewHolder>(EstacionamentoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstacionamentoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_check_out, parent, false)
        return EstacionamentoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EstacionamentoViewHolder, position: Int) {
        val historico = getItem(position)
        holder.bind(historico)
    }

    inner class EstacionamentoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // IDs corretos do layout item_check_out.xml
        private val tvPlacaVeiculo: TextView = itemView.findViewById(R.id.tv_placa_veiculo)
        private val tvModeloVeiculo: TextView = itemView.findViewById(R.id.tv_modelo_veiculo)
        private val tvHorarioCheckIn: TextView = itemView.findViewById(R.id.tv_horario_checkin)
        private val btnRealizarCheckOut: Button = itemView.findViewById(R.id.btn_realizar_checkout)

        fun bind(historico: HistoricoEstacionamento) {
            tvPlacaVeiculo.text = historico.placaVeiculo.uppercase()
            tvModeloVeiculo.text = "${historico.marcaVeiculo} ${historico.modeloVeiculo}"

            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val checkInDate = historico.checkIn?.let { sdf.format(it) } ?: "Data indisponível"
            tvHorarioCheckIn.text = "Check-in: $checkInDate"

            btnRealizarCheckOut.setOnClickListener {
                onCheckOutClick(historico)
            }
        }
    }
}

class EstacionamentoDiffCallback : DiffUtil.ItemCallback<HistoricoEstacionamento>() {
    override fun areItemsTheSame(oldItem: HistoricoEstacionamento, newItem: HistoricoEstacionamento): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: HistoricoEstacionamento, newItem: HistoricoEstacionamento): Boolean {
        return oldItem == newItem
    }
}
