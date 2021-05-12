package com.example.icemusic.view

import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.util.AttributeSet
import android.util.Log
import android.view.animation.*
import androidx.appcompat.widget.AppCompatImageView
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import com.example.icemusic.R
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@BindingMethods(
    value = [BindingMethod(
        type = RotateImageView::class,
        attribute = "imageUrl",
        method = "setImageUrl"
    ),
        BindingMethod(
            type = RotateImageView::class,
            attribute = "isPlaying",
            method = "setRotateState"
        )]
)
class RotateImageView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    AppCompatImageView(context, attrs, defStyleAttr) {

    val TAG = "RotateImageView"

    private var isPlaying = false
    private var imageUrl: String? = null
    private var myBackgroundDrawable: Drawable
    private var layerDrawable: LayerDrawable? = null

    private var rotateState = CREATE_STATE

    //目标图片的宽度占背景图片宽度的百分比
    private var targetDrawablePercent = 0.5f
    private var targetDrawableWidth = 0
    private var targetDrawableHeight = 0

    private var rotateAnimator: ObjectAnimator? = null
    private var rotateAnimationTime = 6000L

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, -1)

    constructor(context: Context) : this(context, null)

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RotateImageView)
        imageUrl = typedArray.getString(R.styleable.RotateImageView_imageUrl)
        Log.i(TAG, "imageUrl: $imageUrl")
        typedArray.recycle()
        myBackgroundDrawable = obtainLayerBackground()
    }


    override fun onDraw(canvas: Canvas?) {
        Log.i(TAG, "rotateState: $rotateState")
        super.onDraw(canvas)
        if(isPlaying){
            when(rotateState){
                CREATE_STATE->{
                    //对应情况：打开页面就播放歌曲；正在播放时切歌
                    createRotate()
                }
                READY_STATE->{
                    //动画，图片以准备好，开始进行旋转动画
                    launchRotate()
                }
                PAUSE_STATE->{
                    //从暂停中恢复播放歌曲
                    resumeRotate()
                }
                else->{
                    Log.e(TAG,"error rotateState ${stateInfoMap[rotateState]} as isPlaying State")
                }
            }
        }else{
            when(rotateState){
                CREATE_STATE->{
                    //打开页面，但是未播放歌曲；暂停时切歌
                    createRotate()
                }
                ROTATING_STATE->{
                    //播放时按暂停，或播放结束
                    pauseRotate()
                }
                else->{
                    Log.e(TAG,"error rotateState ${stateInfoMap[rotateState]} as notPlaying State")
                }
            }
        }
    }

    /**
     * 为旋转动画做准备，如加载图片
     */
    private fun createRotate() {
        if(rotateState!= CREATE_STATE){
            return
        }
        //避免重复加载图片
        rotateState = CREATING_STATE
        //准备动画
        initAnimationSet()
        //准备图片
        asyncInitTargetDrawable()
    }

    /**
     * 初始化RotateAnimator
     */
    private fun initAnimationSet() {
        //为start方法做准备
        if (rotateAnimator == null) {
            rotateAnimator = ObjectAnimator.ofFloat(this, "rotation", 0f, 360f)
            rotateAnimator!!.apply {
                duration = rotateAnimationTime
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.RESTART
                interpolator = LinearInterpolator()
            }
        } else {
            rotateAnimator?.apply {
                if (isPaused || isRunning || isStarted) {
                    cancel()
                }
            }
        }
    }

    /**
     * 异步加载图片
     */
    private fun asyncInitTargetDrawable() {
        targetDrawableWidth = (width * targetDrawablePercent).toInt()
        targetDrawableHeight = (height * targetDrawablePercent).toInt()
        loadLayerDrawable(targetDrawableWidth, targetDrawableHeight, resources)
    }

    /**
     * 启动旋转动画，一定要在READY_STATE状态调用此方法
     */
    private fun launchRotate() {
        if (rotateState == READY_STATE) {
            rotateAnimator?.start()
            rotateState = ROTATING_STATE
        } else {
            Log.e(
                TAG,
                "RotateImageView: not the time to launchRotate，currentState: ${stateInfoMap[rotateState]}"
            )
        }
    }

    /**
     * 暂停旋转
     */
    private fun pauseRotate() {
        if (rotateState == ROTATING_STATE) {
            rotateAnimator?.pause()
            rotateState = PAUSE_STATE
        } else {
            Log.e(
                TAG,
                "RotateImageView: not the time to launchRotate，currentState: ${stateInfoMap[rotateState]}"
            )
        }
    }

    /**
     * 恢复旋转动画
     */
    private fun resumeRotate() {
        if (rotateState == PAUSE_STATE) {
            rotateAnimator?.resume()
            rotateState = ROTATING_STATE
        } else {
            Log.e(
                TAG,
                "RotateImageView: not the time to launchRotate，currentState: ${stateInfoMap[rotateState]}"
            )
        }
    }

    /**
     * 取消旋转动画，抛弃当前的图片,将状态重置为CREATE_STATE
     */
    private fun clearRotate() {
        rotateAnimator?.cancel()
        layerDrawable = null

        //将要重新初始化Rotate
        rotateState = CREATE_STATE
    }


    private fun obtainLayerBackground(): Drawable {
        val ovalShape = OvalShape()
        val circleDrawable = ShapeDrawable(ovalShape)
        circleDrawable.paint.color = Color.Red.toArgb()
        circleDrawable.paint.style = Paint.Style.FILL
        return circleDrawable
    }

    private fun loadLayerDrawable(targetWidth: Int, targetHeight: Int, resources: Resources) {
        imageUrl?.let {
            CoroutineScope(Dispatchers.Main).launch {
                //获取目标图片
                var bitmap = loadTargetBitmap(targetWidth, targetHeight, it)
                bitmap = chipCircleBitmap(bitmap)
                //合成真正显示的图片，避免在主线程分配对象
                withContext(Dispatchers.Default) {
                    val targetDrawable = BitmapDrawable(resources, bitmap)
                    val drawables = arrayOf(myBackgroundDrawable, targetDrawable)
                    layerDrawable = LayerDrawable(drawables)
                    layerDrawable!!.setLayerInset(
                        1,
                        5,
                        5,
                        5,
                        5
                    )
                }
                background = layerDrawable
                //图片已经加载完成（动画也肯定初始化完成），进入就绪状态
                rotateState = READY_STATE
                //获取到图片，请求刷新
                invalidate()
            }
        }
    }

    //使用Picasso加载图片
    private suspend fun loadTargetBitmap(targetWidth: Int, targetHeight: Int, imageUrl: String): Bitmap? =
        withContext(Dispatchers.IO) {
            return@withContext Picasso.get().load(imageUrl).resize(targetWidth, targetHeight).get()
        }
    private fun chipCircleBitmap(srcBitmap: Bitmap?):Bitmap?{
        if(srcBitmap==null){
            Log.i(TAG,"srcBitmap is null")
            return srcBitmap
        }
        val width = srcBitmap.width
        val height = srcBitmap.height
        var radius = 0f
        var px = width/2f
        var py = height/2f
        radius = if(width<=height){
            width/2f
        }else height/2f

        var output = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888)
        var canvas = Canvas(output)
        var paint = Paint()
        paint.isAntiAlias = true

        canvas.drawARGB(0,0,0,0)

        paint.color = Color.White.toArgb()
        canvas.drawCircle(px,py,radius,paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(srcBitmap,0f,0f,paint)
        paint.xfermode = null

        srcBitmap.recycle()
        return output
    }

    fun setImageUrl(newUrl: String) {
        if (newUrl != imageUrl) {
            imageUrl = newUrl
            //重新初始化Rotate
            clearRotate()
            invalidate()
            Log.i(TAG,"new imageUrl: $imageUrl")
        }
    }

    fun setIsPlaying(newState: Boolean) {
        if (newState != isPlaying) {
            isPlaying = newState
            invalidate()
        }
    }

    companion object {
        const val CREATE_STATE = -1
        const val CREATING_STATE = 0
        const val READY_STATE = 1
        const val PAUSE_STATE = 2
        const val ROTATING_STATE = 3

        val stateInfoMap = mapOf<Int, String>(
            CREATE_STATE to "CREATE_STATE",
            CREATING_STATE to "CREATING_STATE",
            READY_STATE to "READY_STATE",
            PAUSE_STATE to "PAUSE_STATE",
            ROTATING_STATE to "ROTATING_STATE"
        )
    }
}
