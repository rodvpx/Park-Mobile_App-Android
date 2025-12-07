package com.example.parkmobile.ui.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.example.parkmobile.R
import com.google.android.material.card.MaterialCardView

class HeaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val headerTitle: TextView
    private val headerImage: ImageView

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.view_header, this, true)

        headerTitle = findViewById(R.id.header_title)
        headerImage = findViewById(R.id.header_image)

        cardElevation = 0f
        radius = resources.getDimension(R.dimen.default_corner_radius)
    }

    fun setHeaderText(text: String?) {
        headerTitle.text = text
    }

    fun setHeaderImage(drawable: Drawable?) {
        headerImage.setImageDrawable(drawable)
    }

    fun setHeaderImageResource(resId: Int) {
        headerImage.setImageResource(resId)
    }
}
