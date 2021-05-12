package com.example.icemusic.viewModel.searchPageVM

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * 保存并共享搜索词，在搜索词改变时，使多个fragment都可以感知到
 * fragment包含SearchResultContentFragment,SearchMainPageFragment
 * @property searchWord MutableLiveData<String>
 */
class ShareSongNameViewModel :ViewModel(){
    val searchWord = MutableLiveData<String>()
}