package com.example.icemusic.viewModel

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import com.example.icemusic.R

class BackupViewModel:BaseViewModel() {
    init {
        layoutId = R.layout.backup
    }

    override fun bindData(
        viewDataBinding: ViewDataBinding,
        lifecycleOwner: LifecycleOwner,
        viewModelStoreOwner: ViewModelStoreOwner
    ) {
        //兜底的ViewModel
    }
}