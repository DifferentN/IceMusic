package com.example.icemusic.listener

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class OnScrollListener: RecyclerView.OnScrollListener() {
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if(newState!=RecyclerView.SCROLL_STATE_IDLE){
            return
        }
        var layoutManager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        var pos = layoutManager.findFirstVisibleItemPosition()

        var firstView = layoutManager.findViewByPosition(pos)!!
        if(firstView.x+firstView.width>firstView.width/2){
            layoutManager.scrollToPositionWithOffset(pos,0)
            recyclerView.scrollToPosition(pos)
        }else {
            layoutManager.scrollToPositionWithOffset(pos+1,0)
        }
    }
}