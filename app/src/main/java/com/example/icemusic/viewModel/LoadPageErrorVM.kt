package com.example.icemusic.viewModel

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import com.example.icemusic.R

class LoadPageErrorVM :BaseViewModel(){

    init {
        layoutId = R.layout.error_page
    }

    override fun bindData(
        viewDataBinding: ViewDataBinding,
        lifecycleOwner: LifecycleOwner,
        viewModelStoreOwner: ViewModelStoreOwner
    ) {

    }
}