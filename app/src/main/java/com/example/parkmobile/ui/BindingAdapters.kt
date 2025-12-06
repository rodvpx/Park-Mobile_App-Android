package com.example.parkmobile.ui

import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.parkmobile.R
import com.example.parkmobile.data.model.Vaga

@BindingAdapter("vagaStatusBackground")
fun bindVagaStatusBackground(cardView: CardView, status: String?) {
    val color = when (status) {
        Vaga.StatusVaga.LIVRE.name -> R.color.vaga_disponivel
        Vaga.StatusVaga.OCUPADA.name -> R.color.vaga_ocupada
        else -> android.R.color.darker_gray
    }
    cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.context, color))
}
