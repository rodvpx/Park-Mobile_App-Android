package com.example.parkmobile.ui.estacionamento

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.R
import com.example.parkmobile.data.model.CheckOutItem

class CheckOutAdapter(private val items: List<CheckOutItem>, private val fragmentManager: FragmentManager) : RecyclerView.Adapter<CheckOutAdapter.CheckOutViewHolder>() {

    class CheckOutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nome: TextView = itemView.findViewById(R.id.tv_nome_cliente)
        val cpf: TextView = itemView.findViewById(R.id.tv_cpf_cliente)
        val placa: TextView = itemView.findViewById(R.id.tv_placa)
        val vaga: TextView = itemView.findViewById(R.id.tv_vaga)
        val entrada: TextView = itemView.findViewById(R.id.tv_entrada)
        val checkOutButton: Button = itemView.findViewById(R.id.btn_check_out)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckOutViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_check_out, parent, false)
        return CheckOutViewHolder(view)
    }

    override fun onBindViewHolder(holder: CheckOutViewHolder, position: Int) {
        val item = items[position]
        holder.nome.text = item.nome
        holder.cpf.text = "CPF: ${item.cpf}"
        holder.placa.text = "Placa: ${item.placa}"
        holder.vaga.text = "Vaga: ${item.vaga}"
        holder.entrada.text = "Entrada: ${item.entrada}"

        holder.checkOutButton.setOnClickListener {
            val bottomSheet = ConfirmCheckOutBottomSheetFragment.newInstance(item)
            bottomSheet.show(fragmentManager, "ConfirmCheckOutBottomSheetFragment")
        }
    }

    override fun getItemCount() = items.size
}