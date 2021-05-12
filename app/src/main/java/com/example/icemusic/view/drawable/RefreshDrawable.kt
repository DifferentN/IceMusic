package com.example.icemusic.view.drawable

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import androidx.core.animation.addListener
import androidx.core.animation.doOnRepeat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.Continuation
import kotlin.math.min

class RefreshDrawable: Drawable() {
    val TAG = "RefreshDrawable"

    val START_TYPE = 1
    val END_TYPE = 0
    val paint:Paint
    var px = 0f
    var py = 0f
    var radius = 0f
    var lastFastValue = 0f
    var startAngle = 30f
    var lastSlowValue = 0f
    var endAngle = 0f
    var animatorSet:AnimatorSet? = null
    var animationTime = 500L
    var animatorType = START_TYPE
    var arcRectF = RectF()
    var arcStrokeWidth = 6f
    init {
        paint = Paint()
        paint.isAntiAlias = true
        paint.color = Color.RED
        paint.strokeWidth = arcStrokeWidth
        paint.style = Paint.Style.STROKE
    }
    override fun draw(canvas: Canvas) {
        //动画会出现问题
        canvas.drawArc(arcRectF,endAngle,(startAngle-endAngle+360)%360,false,paint)

    }

    override fun setAlpha(alpha: Int) {

    }

    override fun setColorFilter(colorFilter: ColorFilter?) {

    }

    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE;
    }

    override fun onBoundsChange(bounds: Rect?) {
        super.onBoundsChange(bounds)
        bounds?.let {
            if(it.width()<=0||it.height()<=0){
                //清除valueAnimator
                animatorSet?.let {
                    it.cancel()
                }
                animatorSet = null
                return
            }
            arcRectF = RectF(bounds.left+arcStrokeWidth,
                bounds.top+arcStrokeWidth,
                bounds.right-arcStrokeWidth,
                bounds.bottom-arcStrokeWidth)
            //边界发生变化，先取消之前的动画，再开始新的动画
            animatorSet?.let {
                it.cancel()
            }
            animatorSet = createAnimatorSet(it.width(),it.height())
            animatorSet?.start()
        }
    }
    private fun createAnimatorSet(width:Int,height:Int):AnimatorSet{
        val fastAnimator = ValueAnimator.ofFloat(0f,200f)
        fastAnimator.interpolator = AccelerateDecelerateInterpolator()
        fastAnimator.repeatCount = ValueAnimator.INFINITE
        fastAnimator.repeatMode = ValueAnimator.RESTART
        fastAnimator.duration = animationTime
        fastAnimator.addUpdateListener {
            var value:Float = it.animatedValue as Float
            var tempValue = value - lastFastValue
            if(tempValue<0){
                return@addUpdateListener
            }
            lastFastValue = value
            startAngle = (startAngle+tempValue)%360
            invalidateSelf()
        }
        fastAnimator.addListener {
            lastFastValue = 0f
        }
        fastAnimator.start()
        CoroutineScope(Dispatchers.Main).launch {
            delay(animationTime/3)
            val slowAnimator = ValueAnimator.ofFloat(0f,200f)
            slowAnimator.interpolator = AccelerateDecelerateInterpolator()
            slowAnimator.repeatCount = ValueAnimator.INFINITE
            slowAnimator.repeatMode = ValueAnimator.RESTART
            slowAnimator.duration = animationTime
            slowAnimator.addUpdateListener {
                var value = it.animatedValue as Float
                var tempValue = value - lastSlowValue
                if(tempValue<0){
                    return@addUpdateListener
                }
                lastSlowValue = value
                endAngle = (endAngle+tempValue)%360
            }
            slowAnimator.addListener {
                lastSlowValue = 0f
            }
            slowAnimator.start()
        }


        animatorSet = AnimatorSet()

        return animatorSet!!

    }
}