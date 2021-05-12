package com.example.icemusic.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.util.AttributeSet
import android.util.Log
import android.view.animation.LinearInterpolator
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import com.example.icemusic.R
import com.example.icemusic.musicPlayManager.musicClient.model.SongMetaData
import kotlin.math.min

@BindingMethods(
    value = [BindingMethod(
        type = ProcessPlayImageView::class,
        attribute = "isPlaying",
        method = "setIsPlaying"
        )]
)
class ProcessPlayImageView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    AppCompatImageView(context, attrs, defStyleAttr) {
    val TAG = "ProcessPlayImageView"

    private var isPlaying: Boolean = false
    private var playPercent = 0f
    private var px = 0f
    private var py = 0f
    private var radius = 0f
    private var strokeWidth = 0f
    private lateinit var arcRectF: RectF
    private var paint: Paint
    private var playDrawable: Drawable? = null
    private var pauseDrawable: Drawable? = null
    private lateinit var drawableRect: Rect

    private var animator: ValueAnimator? = null

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, -1)

    constructor(context: Context) : this(context, null)

    init {
        var typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProcessPlayImageView)
        typedArray.recycle()

        paint = Paint()
        playDrawable = ContextCompat.getDrawable(getContext(), R.drawable.play_icon)
        pauseDrawable = ContextCompat.getDrawable(getContext(), R.drawable.pause_icon)
        strokeWidth = resources.getDimension(R.dimen.processCircleStrokeWidth)

        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = strokeWidth

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        radius = min(width, height) / 2f
        px = width / 2f
        py = height / 2f
        Log.i(TAG,"radius $radius px $px py $py")
        arcRectF = RectF(
            px - radius + strokeWidth/2, py - radius + strokeWidth/2,
            px + radius - strokeWidth/2, py + radius - strokeWidth/2
        )

        drawableRect = Rect(
            (arcRectF.left + strokeWidth).toInt(), (arcRectF.top + strokeWidth).toInt(),
            (arcRectF.right - strokeWidth).toInt(), (arcRectF.bottom - strokeWidth).toInt()
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawColor(Color.WHITE)
        //画圆形进度条的背景圆环
        paint.color = Color.GRAY
        canvas?.drawCircle(px, py, radius-strokeWidth/2, paint)

        //画圆形进度条的真正进度圆环
        paint.color = Color.WHITE
        canvas?.drawArc(arcRectF, -90f, 360 * playPercent, false, paint)

        //画按钮
        if (!isPlaying) {
            playDrawable?.let {
                it.setBounds(drawableRect)
                it.draw(canvas!!)
            }

        } else {
            pauseDrawable?.let {
                it.bounds = drawableRect
                it.draw(canvas!!)
            }
        }
    }

    fun prepareAnimator(playProcess:Float,totalTime:Long){
        //获取动画的初始值
        var startPlayPercent = playProcess/totalTime
        animator = ValueAnimator.ofFloat(startPlayPercent,1f)
        animator?.apply {
            interpolator = LinearInterpolator()
            //将歌曲的剩余时间设置为动画的播放时间
            duration = totalTime - playProcess.toLong()
            addUpdateListener {
                playPercent = it.animatedValue as Float
                invalidate()
                Log.i(TAG,"playPercent: $playPercent")
            }
        }
        Log.i(TAG,"create animator playPercent $startPlayPercent")
    }

    fun setIsPlaying(newValue: Boolean) {
        if (newValue != isPlaying) {
            isPlaying = newValue
            animator?.let {
                if(!isPlaying){
                    it.pause()
                    it.cancel()
                }else{
                    //新建Animator，并start
//                    it.resume()
                }
            }
            invalidate()
        }
    }

    fun setSongMetaData(songMetaData:SongMetaData){
        val totalTime = songMetaData.songDuration
        prepareAnimator(songMetaData.playProcess,totalTime)
        animator?.start()
    }

}