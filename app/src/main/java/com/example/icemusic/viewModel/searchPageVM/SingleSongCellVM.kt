package com.example.icemusic.viewModel.searchPageVM

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.example.icemusic.R
import com.example.icemusic.data.eventBus.PlaySongEvent
import com.example.icemusic.data.searchData.SearchSingleSongData
import com.example.icemusic.databinding.SearchResultSingleSongCellBinding
import com.example.icemusic.viewModel.BaseViewModel
import org.greenrobot.eventbus.EventBus

class SingleSongCellVM(var singleSongData: SearchSingleSongData): BaseViewModel() {

    init {
        layoutId = R.layout.search_result_single_song_cell
    }
    override fun bindData(viewDataBinding: ViewDataBinding, lifecycleOwner: LifecycleOwner) {
        viewDataBinding.takeIf { it is SearchResultSingleSongCellBinding }?.let {
            var searchResultSingleSongCellBinding:SearchResultSingleSongCellBinding = it as SearchResultSingleSongCellBinding
            searchResultSingleSongCellBinding.songData = singleSongData
            searchResultSingleSongCellBinding.songVM = this
        }
    }

    fun playSong(view:View){
        EventBus.getDefault().post(PlaySongEvent(singleSongData))
    }

}