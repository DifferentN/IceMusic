package com.example.icemusic.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.icemusic.util.ViewUtil

/**
 * 用来封装RecyclerView,其内部只能含有1个RecyclerView
 */
class SmallLinearLayout(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    LinearLayout(context, attrs, defStyleAttr) {
    val TAG = "SmallLinearLayout"

    //记录视图中RecyclerView的滑动阈值，注：普通的RecyclerView与ViewPager2内部自带的RecyclerView的滑动阈值不同
    var recyclerViewSlop = -1

    constructor(context: Context):this(context,null)

    constructor(context: Context,attributeSet: AttributeSet?):this(context,attributeSet!!,-1)



    override fun onDraw(canvas: Canvas?) {
        Log.i(TAG,TAG+" onDraw")

        super.onDraw(canvas)
    }

    override fun draw(canvas: Canvas?) {
        Log.i(TAG,TAG+" draw")
//        if(recyclerView==null){
//            Log.i(TAG,"child recycler View is null")
//        }else Log.i(TAG,"child recycler View is not null")
        super.draw(canvas)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        Log.i(TAG,TAG+" dispatchDraw")
        super.dispatchDraw(canvas)
    }

    private fun setRecyclerViewTouchSlop(recyclerView: RecyclerView, touchSlop: Int) {
        var clazz = RecyclerView::class.java
        var field = clazz.getDeclaredField("mTouchSlop")
        field.isAccessible = true
        field.set(recyclerView,touchSlop)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if(ev!!.action == MotionEvent.ACTION_DOWN){
            var recyclerView = ViewUtil.searchRecyclerView(this)
            recyclerView?.let {
                if(recyclerViewSlop==-1){
                    recyclerViewSlop = getRecyclerViewTouchSlop(it)
                }
                setRecyclerViewTouchSlop(it,recyclerViewSlop-1)
                var slop = getRecyclerViewTouchSlop(recyclerView)
                Log.i(TAG,"slop: "+slop)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    fun getRecyclerViewTouchSlop(recyclerView: RecyclerView): Int {
        var clazz = RecyclerView::class.java
        var field = clazz.getDeclaredField("mTouchSlop")
        field.isAccessible = true
        var touchSlop = field.get(recyclerView) as Int
        return touchSlop
    }
}