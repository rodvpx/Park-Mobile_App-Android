package com.example.parkmobile

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import com.example.parkmobile.databinding.ViewHeaderBinding
import com.google.android.material.card.MaterialCardView

class HeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) { // HERDA DE MaterialCardView

    // Usa ViewBinding para acessar as views do layout interno de forma segura
    private val binding: ViewHeaderBinding

    init {
        // Infla o layout 'view_header.xml' e o anexa a este componente
        val inflater = LayoutInflater.from(context)
        binding = ViewHeaderBinding.inflate(inflater, this)

        // Estilos padrão que você já usa
        cardElevation = 0f
        radius = resources.getDimension(R.dimen.default_corner_radius) // Crie um dimen para 16dp
    }

    // Função pública para definir o texto do título
    fun setHeaderText(text: String?) {
        binding.headerTitle.text = text
    }

    // Função pública para definir a imagem de fundo
    fun setHeaderImage(drawable: Drawable?) {
        binding.headerImage.setImageDrawable(drawable)
    }

    // Função para definir a imagem a partir de um ID de recurso
    fun setHeaderImageResource(resId: Int) {
        binding.headerImage.setImageResource(resId)
    }
}
