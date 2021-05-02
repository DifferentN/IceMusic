package com.example.icemusic.viewModel.searchPageVM

import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import com.example.icemusic.R
import com.example.icemusic.data.searchData.searchResultData.SearchResultTabData
import com.example.icemusic.data.searchData.searchResultData.SearchSingleSongData
import com.example.icemusic.netWork.SearchSongWorker
import com.example.icemusic.viewModel.BaseViewModel
import kotlinx.coroutines.*

class SearchResultContentVM : BaseViewModel() {
    val TAG = "SearchResultContentVM"

//    val viewModelList:MutableLiveData<MutableList<BaseViewModel>> by lazy {
//        MutableLiveData<MutableList<BaseViewModel>>().also {
//            it.value = mutableListOf()
//        }
//    }

    //保存不同搜索结果页面中的数据
    val searchResultSongList : MutableLiveData<MutableList<out Any>> by lazy {
        MutableLiveData<MutableList<out Any>>().also {
            it.value = mutableListOf<BaseViewModel>()
        }
    }

    init {
        layoutId = R.layout.search_result_content_page
    }

    fun loadSearchResult(resultTabData:SearchResultTabData?,songName:String?){
        if(resultTabData!=null&&songName!=null){
            viewModelScope.launch {
                var newSearchSongResultList = obtainSearchResult(resultTabData,songName)
                searchResultSongList.value = newSearchSongResultList
            }
        }
    }

    private suspend fun obtainSearchResult(resultTabData:SearchResultTabData,songName: String) : MutableList<out Any> = withContext(Dispatchers.IO){
        var searchWorker = SearchSongWorker()
        var result = searchWorker.searchBySearchTabDataAndSongName(resultTabData,songName)
        return@withContext result
    }

    override fun bindData(
        viewDataBinding: ViewDataBinding,
        lifecycleOwner: LifecycleOwner,
        viewModelStoreOwner: ViewModelStoreOwner
    ) {
        //放在了SearchResultContentFragment中
    }
}