package com.example.margajaya.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.margajaya.R

class RegistrasiButton : AppCompatButton {

    private lateinit var enabledBackground: Drawable
    private lateinit var disabledBackground: Drawable
    private var txtColor : Int = 0

    constructor(context: Context) : super(context) {

        init()

    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        init()

    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

        init()

    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        background = if (isEnabled) enabledBackground else disabledBackground
        text = if (isEnabled) "Sign Up" else "Isi dulu"
        textSize = 18f
        setTextColor(txtColor)

    }

    private fun init(){
        txtColor = ContextCompat.getColor(context, android.R.color.background_light)
        enabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button_enable) as Drawable
        disabledBackground = ContextCompat.getDrawable(context,R.drawable.bg_button_disable) as Drawable
    }
}