package com.example.icemusic.viewModel.searchPageVM

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import com.example.icemusic.R
import com.example.icemusic.databinding.SearchHistorySongCellBinding
import com.example.icemusic.databinding.SearchHistorySongListBinding
import com.example.icemusic.db.entity.SearchHistorySong
import com.example.icemusic.viewModel.BaseViewModel

class SearchHistorySongCellVM:BaseViewModel() {

    var songName = ""

    init {
        layoutId = R.layout.search_history_song_cell
    }

    override fun bindData(viewDataBinding: ViewDataBinding, lifecycleOwner: LifecycleOwner,viewModelStoreOwner: ViewModelStoreOwner) {
        viewDataBinding.takeIf { it is SearchHistorySongCellBinding }?.let {
            var searchHistorySongCellBinding = it as SearchHistorySongCellBinding

            searchHistorySongCellBinding.searchHistorySongNameTextView.text = songName

            searchHistorySongCellBinding.executePendingBindings()
        }
    }

    override fun <T> initialData(data: T) {
        data?.let {
            var dataClazz = it::class.java
            if(SearchHistorySong::class.java.isAssignableFrom(dataClazz)){
                songName = (it as SearchHistorySong).songName?:""
            }
        }
    }
}