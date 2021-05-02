package com.example.icemusic.viewModel.searchPageVM

import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import com.example.icemusic.R
import com.example.icemusic.data.eventBus.PlaySongEvent
import com.example.icemusic.data.searchData.searchResultData.SearchSingleSongData
import com.example.icemusic.databinding.SearchResultSingleSongCellBinding
import com.example.icemusic.viewModel.BaseViewModel
import org.greenrobot.eventbus.EventBus

class SingleSongCellVM: BaseViewModel() {
    val TAG = "SingleSongCellVM"

    var singleSongData: SearchSingleSongData? = null
    init {
        layoutId = R.layout.search_result_single_song_cell
    }
    override fun bindData(viewDataBinding: ViewDataBinding, lifecycleOwner: LifecycleOwner,viewModelStoreOwner: ViewModelStoreOwner) {
        viewDataBinding.takeIf { it is SearchResultSingleSongCellBinding }?.let {
            var searchResultSingleSongCellBinding:SearchResultSingleSongCellBinding = it as SearchResultSingleSongCellBinding
            searchResultSingleSongCellBinding.songData = singleSongData
            searchResultSingleSongCellBinding.songVM = this

        }
    }

    override fun <T> initialData(data: T) {
        data?.let {
            var clazz = it::class.java
            if(SearchSingleSongData::class.java.isAssignableFrom(clazz)){
                singleSongData = data as SearchSingleSongData
            }
        }
    }

    fun playSong(view:View){
        EventBus.getDefault().post(PlaySongEvent(singleSongData!!))
    }

}