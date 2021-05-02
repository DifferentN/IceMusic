package com.example.icemusic.viewModel.findPageVM

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import com.example.icemusic.BR
import com.example.icemusic.R
import com.example.icemusic.data.RecommendSongCellData
import com.example.icemusic.viewModel.BaseViewModel

class RecommendSongCellViewModel : BaseViewModel() {

    var recommendSongCellData:RecommendSongCellData? = null

    init {
        layoutId = R.layout.recommend_song_cell
    }

    override fun bindData(viewDataBinding: ViewDataBinding, lifecycleOwner: LifecycleOwner,viewModelStoreOwner: ViewModelStoreOwner) {
        viewDataBinding.setVariable(BR.obj,this)
        viewDataBinding.executePendingBindings()
    }

    override fun <T> initialData(data: T) {
        data?.let {
            var clazz = it::class.java
            if(RecommendSongCellData::class.java.isAssignableFrom(clazz)){
                recommendSongCellData = data as RecommendSongCellData
            }
        }


    }
}