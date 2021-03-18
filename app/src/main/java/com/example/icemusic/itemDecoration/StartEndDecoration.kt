package com.example.icemusic.itemDecoration

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView

class StartEndDecoration(val startBlankWidth:Int, val endBlankWidth:Int) : RecyclerView.ItemDecoration() {
    val TAG = "StartEndDecoration"

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
//        Log.i(TAG,"pos: ${parent.getChildAdapterPosition(view)} itemCount: ${parent.adapter!!.itemCount}")
        if(parent.getChildAdapterPosition(view)==0){
            setStartBlank(outRect,view)
        }else if(parent.getChildAdapterPosition(view)==parent.adapter!!.itemCount-1){
            setEndBlank(outRect,view)
        }
    }
    fun setStartBlank(outRect: Rect,view: View){
        var startOffset = startBlankWidth - view.marginStart
        outRect.left = startOffset
//        Log.i(TAG,"left startOffset: "+startOffset)
    }
    fun setEndBlank(outRect: Rect,view: View){
        var endOffset = endBlankWidth - view.marginEnd
        outRect.right = endOffset
//        Log.i(TAG,"right endOffset: "+endOffset)
    }
}