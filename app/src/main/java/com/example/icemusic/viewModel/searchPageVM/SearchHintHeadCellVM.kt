package com.example.icemusic.viewModel.searchPageVM

import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import com.example.icemusic.BR
import com.example.icemusic.R
import com.example.icemusic.data.eventBus.SearchHintEvent
import com.example.icemusic.databinding.SearchHintHeadCellBinding
import com.example.icemusic.viewModel.BaseViewModel
import org.greenrobot.eventbus.EventBus

class SearchHintHeadCellVM : BaseViewModel() {
    val TAG = "SearchHintHeadCellVM"

    var hintWord:String = ""

    init {
        layoutId = R.layout.search_hint_head_cell
    }
    override fun bindData(viewDataBinding: ViewDataBinding, lifecycleOwner: LifecycleOwner,viewModelStoreOwner: ViewModelStoreOwner) {
        viewDataBinding.takeIf { it is SearchHintHeadCellBinding }?.let {
            var binding:SearchHintHeadCellBinding = it as SearchHintHeadCellBinding
            binding.vm = this
            it.setVariable(BR.obj,hintWord)
            it.executePendingBindings()
        }
    }

    fun sendSearchHintWord(view:View){
        EventBus.getDefault().post(SearchHintEvent(hintWord))
    }
}