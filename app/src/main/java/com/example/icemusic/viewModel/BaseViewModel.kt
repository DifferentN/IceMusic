package com.example.icemusic.viewModel

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.icemusic.R
import com.example.icemusic.adapter.recyclerAdapter.base.BaseRecyclerViewAdapter
import com.example.icemusic.itemDecoration.StartEndDecoration

open abstract class BaseViewModel : ViewModel() {
    open var layoutId:Int = -1
    abstract fun bindData(viewDataBinding: ViewDataBinding,lifecycleOwner: LifecycleOwner)

    /**
     * 以默认的方式绑定RecyclerView
     * @param recyclerView RecyclerView
     * @param lifecycleOwner LifecycleOwner
     * @param baseViewModelList MutableLiveData<MutableList<out BaseViewModel>>
     */
    open fun baseBindRecyclerView(
        recyclerView:RecyclerView,
        lifecycleOwner: LifecycleOwner,
        baseViewModelList: MutableLiveData<out MutableList<out BaseViewModel>>
    ){
        var adapter = BaseRecyclerViewAdapter(lifecycleOwner)
        recyclerView.adapter = adapter

        var layoutManager = LinearLayoutManager(recyclerView.context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = layoutManager

        var startBlankWidth = recyclerView.resources.getDimension(R.dimen.finding_page_start_blank_width).toInt()
        var endBlankWidth = recyclerView.resources.getDimension(R.dimen.finding_page_end_blank_width).toInt()
        recyclerView.addItemDecoration(StartEndDecoration(startBlankWidth, endBlankWidth))

        baseViewModelList.observe(lifecycleOwner, Observer {
            adapter.viewModelList = it
            adapter.notifyDataSetChanged()
        })
    }
}