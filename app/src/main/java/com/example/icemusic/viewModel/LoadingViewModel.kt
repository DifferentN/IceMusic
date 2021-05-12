package com.example.icemusic.viewModel

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import com.example.icemusic.R
import com.example.icemusic.databinding.LoadingViewBinding

class LoadingViewModel: BaseViewModel() {

    var loadingHintWord = "正在加载，请耐心等待！"
    init {
        layoutId = R.layout.loading_view
    }
    override fun bindData(
        viewDataBinding: ViewDataBinding,
        lifecycleOwner: LifecycleOwner,
        viewModelStoreOwner: ViewModelStoreOwner
    ) {
        viewDataBinding.takeIf { it is LoadingViewBinding }?.let {
            var loadingViewBinding = it as LoadingViewBinding
            loadingViewBinding.vm = this
        }
    }
}