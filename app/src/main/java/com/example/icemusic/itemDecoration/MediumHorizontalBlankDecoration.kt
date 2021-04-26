package com.example.icemusic.itemDecoration

import android.graphics.Rect
import android.view.View
import androidx.core.view.marginStart
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.icemusic.viewModel.BaseViewModel

class MediumHorizontalBlankDecoration(var mediumBlankSize:Int): RecyclerView.ItemDecoration()  {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        if(parent.getChildAdapterPosition(view)==0){
            return
        }

        var startOffset = mediumBlankSize - view.marginStart
        outRect.left = startOffset
    }
}