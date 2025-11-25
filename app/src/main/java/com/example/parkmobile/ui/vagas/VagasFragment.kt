package com.example.parkmobile.ui.vagas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.R
import com.example.parkmobile.data.model.Vaga
import com.example.parkmobile.data.repository.VagaRepository
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class VagasFragment : Fragment() {

    private lateinit var vagaRepository: VagaRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vagas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvVagas = view.findViewById<RecyclerView>(R.id.rv_vagas)
        val fabAddVaga = view.findViewById<FloatingActionButton>(R.id.fab_add_vaga)

        // Initialize repository and get data
        vagaRepository = VagaRepository()
        val vagas = vagaRepository.getVagas()

        rvVagas.adapter = VagasAdapter(vagas) { vaga ->
            val bottomSheet = EditVagaBottomSheetFragment.newInstance(vaga)
            bottomSheet.show(childFragmentManager, "EditVagaBottomSheetFragment")
        }

        fabAddVaga.setOnClickListener {
            val bottomSheet = AddVagaBottomSheetFragment()
            bottomSheet.show(childFragmentManager, "AddVagaBottomSheetFragment")
        }
    }

    class VagasAdapter(private val vagas: List<Vaga>, private val onItemClick: (Vaga) -> Unit) : RecyclerView.Adapter<VagasAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val vagaText: TextView = view.findViewById(R.id.tv_vaga)
            val cardVaga: MaterialCardView = view.findViewById(R.id.card_vaga)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_vagas, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val vaga = vagas[position]
            holder.vagaText.text = vaga.nome

            holder.itemView.setOnClickListener { onItemClick(vaga) }

            val context = holder.itemView.context
            val backgroundColor = when (vaga.status) {
                "disponivel" -> ContextCompat.getColor(context, R.color.vaga_disponivel)
                "ocupada" -> ContextCompat.getColor(context, R.color.vaga_ocupada)
                else -> ContextCompat.getColor(context, android.R.color.transparent)
            }

            holder.cardVaga.setCardBackgroundColor(backgroundColor)
        }

        override fun getItemCount() = vagas.size
    }
}
