package com.example.icemusic.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

/**
 * 此视图内部只能包含一个RecyclerView
 */
class OutsideLinearLayout(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    LinearLayout(context, attrs, defStyleAttr) {

    //记录手指按下时的坐标
    var mInitialTouchX: Float = -1f
    var mInitialTouchY: Float = -1f

    //判断是否需要在分发原MotionEvent事件之前，分发一个滑动距离为滑动阈值的事件
    var splitFlag = false

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, -1)

    constructor(context: Context?) : this(context, null)

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        ev?.let {
            if(it.action == MotionEvent.ACTION_DOWN){
                mInitialTouchX = it.x
                mInitialTouchY = it.y
                splitFlag = true
            }else if(it.action == MotionEvent.ACTION_MOVE){
                var recyclerView = searchRecyclerView(this)
                //获取ViewPager2的滑动阈值
                var touchSlop = getRecyclerViewTouchSlop(recyclerView!!)
                var isScroll = Math.abs(it.x - mInitialTouchX)>touchSlop||Math.abs(it.y - mInitialTouchY)>touchSlop
                //检查是否需要插入一个滑动距离为滑动阈值的触摸事件
                //需要满足的条件为：1、滑动距离里大于滑动阈值；2、自Down事件发生后，第一次大于滑动阈值
                if(isScroll&&splitFlag){
                    var newMotionEvent = MotionEvent.obtain(it)
                    var newX = it.x
                    if(Math.abs(it.x - mInitialTouchX)>touchSlop){
                        //使新MotionEvent的x与手指按下时的x之间的差为滑动阈值
                        if(it.x-mInitialTouchX>0){
                            newX = mInitialTouchX+touchSlop
                        }else newX = mInitialTouchX-touchSlop
                    }
                    var newY = it.y
                    if(Math.abs(it.y - mInitialTouchY)>touchSlop){
                        if(it.y-mInitialTouchY>0){
                            newY = mInitialTouchY+touchSlop
                        }else newY = mInitialTouchY-touchSlop
                    }
                    newMotionEvent.setLocation(newX, newY)
                    //发生新的MotionEvent
                    super.dispatchTouchEvent(newMotionEvent)
                    splitFlag = false
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 获取RecyclerView的滑动阈值
     * @param recyclerView RecyclerView
     * @return Int 滑动阈值
     */
    fun getRecyclerViewTouchSlop(recyclerView: RecyclerView): Int {
        var clazz = RecyclerView::class.java
        var field = clazz.getDeclaredField("mTouchSlop")
        field.isAccessible = true
        var touchSlop = field.get(recyclerView) as Int
        return touchSlop
    }

    /**
     * 获取ViewGroup中的RecyclerView
     * @param vg ViewGroup
     * @return RecyclerView?
     */
    fun searchRecyclerView(vg: ViewGroup):RecyclerView?{
        for(i in 0 until vg.childCount){
            var view = vg.getChildAt(i)
            if(view is RecyclerView){
                return view
            }else if(view is ViewGroup){
                var recyclerView = searchRecyclerView(view)
                if(recyclerView!=null){
                    return recyclerView
                }
            }
        }
        return null
    }
}