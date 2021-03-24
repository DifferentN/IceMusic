package com.example.icemusic.itemDecoration

import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StartMatchEndDecoration(val startWidth: Int) : StartEndDecoration(startWidth, 0) {
    override var TAG = "StartMatchEnd"

    override fun setEndBlank(outRect: Rect, view: View) {
        var endOffset = 0
        (view.parent as ViewGroup).takeIf { it is RecyclerView }?.let {
            var linearLayoutManager = (it as RecyclerView).layoutManager as LinearLayoutManager
            var pos = linearLayoutManager.findFirstVisibleItemPosition()
            var firstView = linearLayoutManager.findViewByPosition(pos)
            var firstViewWidth = 0
            var parentWidth = it.width
            firstView?.let {
                if (it.x < 0) {
                    firstViewWidth = it.width
                    endOffset = parentWidth - firstViewWidth - view.marginStart - view.marginEnd
                }
            }
        }
//        Log.i(TAG,"endOffset: ${endOffset}")
//        endOffset = 0
        outRect.right = endOffset
    }
}