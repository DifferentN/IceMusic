package com.example.icemusic.viewModel.searchPageVM

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.example.icemusic.data.searchData.SearchSingleSongData
import com.example.icemusic.netWork.SearchSongWorker
import com.example.icemusic.viewModel.BaseViewModel

class SearchResultSingleSongVM(searchWord:String):SearchResultContentVM(searchWord) {

    override suspend fun onLoadViewModelList(): MutableList<BaseViewModel> {
        var singleSongDatas = loadSingleSongDataList()
        var singleSongVMs = mutableListOf<BaseViewModel>()
        for(data in singleSongDatas){
            var singleSongCellVM = SingleSongCellVM(data)
            singleSongVMs.add(singleSongCellVM)
        }
        return singleSongVMs
    }

    override fun bindData(viewDataBinding: ViewDataBinding, lifecycleOwner: LifecycleOwner) {

    }

    suspend fun loadSingleSongDataList():MutableList<SearchSingleSongData>{
        var searchWorker = SearchSongWorker()
        return searchWorker.searchSongData(searchWord)
    }
}