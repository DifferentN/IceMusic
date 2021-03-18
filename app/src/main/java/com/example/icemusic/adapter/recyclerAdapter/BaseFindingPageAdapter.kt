package com.example.icemusic.adapter.recyclerAdapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.icemusic.viewModel.BaseViewModel

class BaseFindingPageAdapter(val lifecycleOwner:LifecycleOwner):
    RecyclerView.Adapter<BaseFindingPageAdapter.BaseFindingPageViewHolder>() {

    var viewModelList:MutableList<out BaseViewModel>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseFindingPageViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        //使用layoutId作为viewType,使用AdIndicatorViewBinding.inflate方法，会导致视图不显示
        var viewDataBinding:ViewDataBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent,false)
        return BaseFindingPageViewHolder(viewDataBinding)
    }

    override fun onBindViewHolder(holder: BaseFindingPageViewHolder, position: Int) {
        holder.bindData(viewModelList!![position],lifecycleOwner)
    }

    override fun getItemCount(): Int {
        return viewModelList?.size?:0
    }

    override fun getItemViewType(position: Int): Int {
        return viewModelList!![position].layoutId
    }

    class BaseFindingPageViewHolder(var viewDataBinding:ViewDataBinding):
        RecyclerView.ViewHolder(viewDataBinding.root) {

        fun bindData(baseViewModel: BaseViewModel, lifecycleOwner: LifecycleOwner){
            baseViewModel.bindData(viewDataBinding,lifecycleOwner)
            viewDataBinding.executePendingBindings()
        }
    }
}