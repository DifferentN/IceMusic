package com.example.icemusic.viewModel.searchPageVM

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.example.icemusic.R
import com.example.icemusic.viewModel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

abstract class SearchResultContentVM(var searchWord:String) : BaseViewModel() {

    val viewModelList:MutableLiveData<MutableList<BaseViewModel>> by lazy {
        MutableLiveData<MutableList<BaseViewModel>>().also {
            it.value = mutableListOf()
        }
    }

    init {
        layoutId = R.layout.search_result_content_page
    }

    suspend fun updateViewModeList(){
        var newVMList = onLoadViewModelList()
        runBlocking(Dispatchers.Main) {
            viewModelList.value = newVMList
        }
    }

    abstract suspend fun onLoadViewModelList():MutableList<BaseViewModel>
}