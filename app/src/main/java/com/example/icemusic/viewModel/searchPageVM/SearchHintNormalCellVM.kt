package com.example.icemusic.viewModel.searchPageVM

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import com.example.icemusic.BR
import com.example.icemusic.R
import com.example.icemusic.data.eventBus.SearchHintEvent
import com.example.icemusic.data.searchData.SearchSongHintData
import com.example.icemusic.databinding.SearchHintNormalCellBinding
import com.example.icemusic.viewModel.BaseViewModel
import org.greenrobot.eventbus.EventBus

class SearchHintNormalCellVM : BaseViewModel() {
    var hintWord:String = ""

    init {
        layoutId = R.layout.search_hint_normal_cell
    }

    override fun bindData(viewDataBinding: ViewDataBinding, lifecycleOwner: LifecycleOwner,viewModelStoreOwner: ViewModelStoreOwner) {
        viewDataBinding.takeIf { it is SearchHintNormalCellBinding}?.let {
            var binding:SearchHintNormalCellBinding = it as SearchHintNormalCellBinding
            binding.vm = this
            it.setVariable(BR.obj,hintWord)
            it.executePendingBindings()
        }
    }

    override fun <T> initialData(data: T) {
        data?.let {
            var clazz = it::class.java
            if(SearchSongHintData::class.java.isAssignableFrom(clazz)){
                hintWord = (data as SearchSongHintData).hintWord
            }
        }
    }

    /**
     * 处理点击提示搜索词事件
     * @param view View
     */
    fun sendSearchHintWord(view: View){
        //接收者为SearchMainPageFragment
        EventBus.getDefault().post(SearchHintEvent(hintWord))

        //目的：使EditText失去焦点，回收软键盘
        view.rootView.requestFocus()
    }
}