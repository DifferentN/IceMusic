package com.example.icemusic.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.example.icemusic.view.drawable.RefreshDrawable

class RefreshImageView(context: Context, attrs: AttributeSet?, defStyleAttr: Int): AppCompatImageView(context,attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, -1)

    constructor(context: Context) : this(context, null)

    init {
        background = RefreshDrawable()
    }
}