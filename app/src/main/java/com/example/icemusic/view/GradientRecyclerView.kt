package com.example.icemusic.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.icemusic.R

class GradientRecyclerView(context: Context,attrs:AttributeSet?,defStyleAttr:Int): RecyclerView(context,attrs,defStyleAttr) {
    var TAG = "GradientRecyclerView"

    private var fadeWidth = 0f

    private var startFadeWidth = fadeWidth
    private var endFadeWidth = fadeWidth
    constructor(context: Context):this(context,null)

    constructor(context: Context,attrs: AttributeSet?):this(context,attrs,-1)

    init {
        var typedArray = context.obtainStyledAttributes(attrs, R.styleable.GradientRecyclerView)
        fadeWidth = typedArray.getDimension(R.styleable.GradientRecyclerView_fade_width,0f)
        typedArray.recycle()
    }

    override fun onDraw(c: Canvas?) {
        super.onDraw(c)
    }

    override fun onDrawForeground(canvas: Canvas?) {
        super.onDrawForeground(canvas)

        var foreColors:IntArray = intArrayOf(Color.WHITE,Color.TRANSPARENT)

        if(startFadeWidth>0){
            var startGradientDrawable = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,foreColors)
            startGradientDrawable.setBounds(0,0,startFadeWidth.toInt(),height)
            startGradientDrawable.draw(canvas!!)
        }

        if(endFadeWidth>0){
            var endGradientDrawable = GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT,foreColors)
            endGradientDrawable.setBounds((width-endFadeWidth).toInt(),0,width,height)
            endGradientDrawable.draw(canvas!!)
        }
    }

    fun enableStartFade(){
        if(startFadeWidth==fadeWidth){
            return
        }
        startFadeWidth = fadeWidth
        invalidate()
    }
    fun disableStartFade(){
        if(startFadeWidth!=0f){
            startFadeWidth = 0f
            invalidate()
        }
    }
    fun enableEndFade(){
        if(endFadeWidth==fadeWidth){
            return
        }
        endFadeWidth = fadeWidth
        invalidate()
    }
    fun disableEndFade(){
        if(endFadeWidth!=0f){
            endFadeWidth = 0f
            invalidate()
        }
    }
}