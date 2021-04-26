package com.example.icemusic.listener

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.icemusic.view.GradientRecyclerView

class StartEndFadeScrollListener:RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        var layoutManager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        var count = layoutManager.itemCount
        if(count==0){
            return
        }
        var firstPos = layoutManager.findFirstCompletelyVisibleItemPosition()
        var lastPos = layoutManager.findLastCompletelyVisibleItemPosition()


        var gradientRecyclerView = recyclerView as GradientRecyclerView
        if(firstPos==0){
            gradientRecyclerView.disableStartFade()
        }else gradientRecyclerView.enableStartFade()

        if(lastPos==count-1){
            gradientRecyclerView.disableEndFade()
        }else gradientRecyclerView.enableEndFade()
    }
}