package com.example.icemusic.viewModel.findPageVM

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import com.example.icemusic.BR
import com.example.icemusic.R
import com.example.icemusic.data.TypeEntryCellData
import com.example.icemusic.viewModel.BaseViewModel

class TypeEntryCellViewModel : BaseViewModel() {

    var typeEntryCellData: TypeEntryCellData? = null

    init {
        layoutId = R.layout.type_entry_cell
    }

    override fun bindData(viewDataBinding: ViewDataBinding, lifecycleOwner: LifecycleOwner,viewModelStoreOwner: ViewModelStoreOwner) {
        viewDataBinding.setVariable(BR.obj,typeEntryCellData)
        viewDataBinding.executePendingBindings()
    }
}