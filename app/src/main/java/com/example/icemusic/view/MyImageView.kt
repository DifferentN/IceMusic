package com.example.icemusic.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageView

class MyImageView(context: Context,attributeSet: AttributeSet,defStyleAttr:Int) :androidx.appcompat.widget.AppCompatImageView(context,attributeSet,defStyleAttr){
    val TAG = "MyImageView"

    constructor(context: Context):this(context,null)

    constructor(context: Context,attributeSet: AttributeSet?):this(context,attributeSet!!,-1)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Log.i(TAG,"onDraw")
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(event)
    }
}