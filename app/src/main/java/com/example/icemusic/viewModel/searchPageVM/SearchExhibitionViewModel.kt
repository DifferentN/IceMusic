package com.example.icemusic.viewModel.searchPageVM

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import com.example.icemusic.R
import com.example.icemusic.db.MusicDatabaseInstance
import com.example.icemusic.viewModel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchExhibitionViewModel : BaseViewModel() {

    val exhibitionDataList:MutableLiveData<MutableList<out Any>> by lazy {
        MutableLiveData<MutableList<out Any>>().also {
            it.value = mutableListOf()
        }
    }

    init {
        layoutId = R.layout.search_exhibition
    }

    override fun bindData(
        viewDataBinding: ViewDataBinding,
        lifecycleOwner: LifecycleOwner,
        viewModelStoreOwner: ViewModelStoreOwner
    ) {
        //放到了SearchExhibitionFragment中
    }

    override fun loadData() {
    }

    fun loadDataWithContext(context:Context){
        var dataList = mutableListOf<Any>()
        viewModelScope.launch {
            //加载历史搜索词
            var searchHistoryList = withContext(Dispatchers.IO){
                var db = MusicDatabaseInstance.getInstance(context.applicationContext)
                var historyList = db.searchHistorySongDao().getSearchHistorySong()
                return@withContext historyList
            }
            searchHistoryList?.let {
                dataList.add(it)
            }

            //加载其他数据
            exhibitionDataList.value = dataList
        }
    }
}