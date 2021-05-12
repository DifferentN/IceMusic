package com.example.icemusic.viewModel.searchPageVM

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.*
import com.example.icemusic.R
import com.example.icemusic.data.LoadingPageData
import com.example.icemusic.data.searchData.searchResultData.SearchResultTabData
import com.example.icemusic.data.searchData.searchResultData.SearchSingleSongData
import com.example.icemusic.netWork.SearchSongWorker
import com.example.icemusic.viewModel.BaseViewModel
import kotlinx.coroutines.*

class SearchResultContentVM(private val savedStateHandle: SavedStateHandle) : BaseViewModel() {
    private val TAG = "SearchResultContentVM"

    private val SAVE_VALUE_LIST = "SAVE_VALUE_LIST"
    private val TAB_TYPE = "TAB_TYPE"
    private val SONG_NAME = "SONG_NAME"
    private var lastTabType:Int? = -1
    private var lastSongName:String? = ""

    //保存不同搜索结果页面中的数据
    val searchResultSongList : MutableLiveData<MutableList<out Parcelable>> by lazy {
        MutableLiveData<MutableList<out Parcelable>>().also {
            it.value = mutableListOf<Parcelable>()
        }
    }

    init {
        layoutId = R.layout.search_result_content_page
        //待测试：saveStateHandler存储数据能力的上限
        lastSongName = savedStateHandle[SONG_NAME]
        lastTabType = savedStateHandle[TAB_TYPE]
        Log.i(TAG,"read $lastSongName $lastTabType")
    }

    fun loadSearchResult(resultTabData:SearchResultTabData?,songName:String?){
        if(resultTabData==null||songName==null){
            return
        }
        //与上次加载时使用的数据相同，不在重新加载，使用SavedStateHandle中的数据
        if(resultTabData.tabType==lastTabType&&songName==lastSongName){
            searchResultSongList.value = savedStateHandle.get<MutableList<Parcelable>>(SAVE_VALUE_LIST)
            return
        }
        lastTabType = resultTabData.tabType
        lastSongName = songName
        //保存新的tabType和songName
        savedStateHandle.set(TAB_TYPE,lastTabType)
        savedStateHandle[SONG_NAME] = lastSongName

        viewModelScope.launch {
            //先使用“加载数据中的data”
            var loadingData = mutableListOf<Parcelable>()
            loadingData.add(LoadingPageData())
            searchResultSongList.value = loadingData
            //加载数据
            var newSearchSongResultList = obtainSearchResult(resultTabData,songName)
            searchResultSongList.value = newSearchSongResultList
            //保存搜索结果
            savedStateHandle[SAVE_VALUE_LIST] = searchResultSongList.value
        }
    }

    private suspend fun obtainSearchResult(resultTabData:SearchResultTabData,songName: String) : MutableList<out Parcelable> = withContext(Dispatchers.IO){
        var searchWorker = SearchSongWorker()
        Log.i(TAG,"thread is ${Thread.currentThread().id} search Name: $songName")
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