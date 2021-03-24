package com.example.icemusic.viewModel.findPageVM

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.*
import com.example.icemusic.R
import com.example.icemusic.viewModel.BaseViewModel

class FindingPageViewModel: BaseViewModel() {

    val viewModeList: MutableLiveData<MutableList<BaseViewModel>> by lazy {
        MutableLiveData<MutableList<BaseViewModel>>().also {
            it.value= mutableListOf()
        }
    }

    init {
        layoutId = R.layout.finding_page
    }

    override fun bindData(viewDataBinding: ViewDataBinding, lifecycleOwner: LifecycleOwner) {

    }
}