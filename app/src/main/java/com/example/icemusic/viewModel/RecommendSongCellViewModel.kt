package com.example.icemusic.viewModel

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.example.icemusic.BR
import com.example.icemusic.R
import com.example.icemusic.data.RecommendSongCellData

class RecommendSongCellViewModel : BaseViewModel() {

    var recommendSongCellData:RecommendSongCellData? = null

    init {
        layoutId = R.layout.recommend_song_cell
    }

    override fun bindData(viewDataBinding: ViewDataBinding, lifecycleOwner: LifecycleOwner) {
        viewDataBinding.setVariable(BR.obj,this)
        viewDataBinding.executePendingBindings()
    }
}