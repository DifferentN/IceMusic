package com.example.icemusic.viewModel.searchPageVM

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.example.icemusic.BR
import com.example.icemusic.R
import com.example.icemusic.data.eventBus.SearchHintEvent
import com.example.icemusic.databinding.SearchHintNormalCellBinding
import com.example.icemusic.viewModel.BaseViewModel
import org.greenrobot.eventbus.EventBus

class SearchHintNormalCellVM : BaseViewModel() {
    var hintWord:String = ""

    init {
        layoutId = R.layout.search_hint_normal_cell
    }

    override fun bindData(viewDataBinding: ViewDataBinding, lifecycleOwner: LifecycleOwner) {
        viewDataBinding.takeIf { it is SearchHintNormalCellBinding}?.let {
            var binding:SearchHintNormalCellBinding = it as SearchHintNormalCellBinding
            binding.vm = this
            it.setVariable(BR.obj,hintWord)
            it.executePendingBindings()
        }
    }

    fun sendSearchHintWord(view: View){
        EventBus.getDefault().post(SearchHintEvent(hintWord))
    }
}