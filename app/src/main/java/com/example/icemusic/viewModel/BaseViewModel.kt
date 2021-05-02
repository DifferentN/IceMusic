package com.example.icemusic.viewModel

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.icemusic.R
import com.example.icemusic.adapter.recyclerAdapter.base.BaseRecyclerViewAdapter
import com.example.icemusic.itemDecoration.StartEndDecoration
import kotlinx.coroutines.launch

open abstract class BaseViewModel : ViewModel() {
    open var layoutId:Int = -1
    abstract fun bindData(viewDataBinding: ViewDataBinding,lifecycleOwner: LifecycleOwner,viewModelStoreOwner: ViewModelStoreOwner)

    /**
     * 在BaseViewModel创建后，使用已经得到的Data初始化BaseViewModel中的data
     * @param data T
     */
    open fun<T> initialData(data:T){
    }

    open fun loadData(){}
}