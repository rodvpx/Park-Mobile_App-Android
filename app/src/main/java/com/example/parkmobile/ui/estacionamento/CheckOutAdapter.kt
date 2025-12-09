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
import com.example.parkmobile.data.model.HistoricoEstacionamentoDetalhado
import java.text.SimpleDateFormat
import java.util.Locale

class CheckOutAdapter(
    private val onCheckOutClick: (HistoricoEstacionamento) -> Unit
) : ListAdapter<HistoricoEstacionamentoDetalhado, CheckOutAdapter.CheckOutViewHolder>(CheckOutDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckOutViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_check_out, parent, false)
        return CheckOutViewHolder(view)
    }

    override fun onBindViewHolder(holder: CheckOutViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class CheckOutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvPlacaVeiculo: TextView = itemView.findViewById(R.id.tv_placa_veiculo)
        private val tvModeloVeiculo: TextView = itemView.findViewById(R.id.tv_modelo_veiculo)
        private val tvVagaCodigo: TextView = itemView.findViewById(R.id.tv_vaga_codigo)
        private val tvNomeCliente: TextView = itemView.findViewById(R.id.tv_nome_cliente)
        private val tvCpfCliente: TextView = itemView.findViewById(R.id.tv_cpf_cliente)
        private val tvHorarioCheckIn: TextView = itemView.findViewById(R.id.tv_horario_checkin)
        private val btnRealizarCheckOut: Button = itemView.findViewById(R.id.btn_realizar_checkout)

        fun bind(detalhado: HistoricoEstacionamentoDetalhado) {
            val historico = detalhado.historico
            tvPlacaVeiculo.text = historico.placaVeiculo
            tvModeloVeiculo.text = "${historico.marcaVeiculo} ${historico.modeloVeiculo}"
            tvVagaCodigo.text = "Vaga: ${detalhado.vaga?.codigo ?: "N/A"}"
            tvNomeCliente.text = detalhado.cliente?.nome ?: "Cliente não encontrado"
            tvCpfCliente.text = detalhado.cliente?.cpf ?: "CPF não encontrado"

            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val checkInDate = historico.checkIn?.let { sdf.format(it) } ?: "N/A"
            tvHorarioCheckIn.text = "Check-in: $checkInDate"

            btnRealizarCheckOut.setOnClickListener {
                onCheckOutClick(historico)
            }
        }
    }
}

class CheckOutDiffCallback : DiffUtil.ItemCallback<HistoricoEstacionamentoDetalhado>() {
    override fun areItemsTheSame(oldItem: HistoricoEstacionamentoDetalhado, newItem: HistoricoEstacionamentoDetalhado): Boolean {
        return oldItem.historico.id == newItem.historico.id
    }

    override fun areContentsTheSame(oldItem: HistoricoEstacionamentoDetalhado, newItem: HistoricoEstacionamentoDetalhado): Boolean {
        return oldItem == newItem
    }
}
