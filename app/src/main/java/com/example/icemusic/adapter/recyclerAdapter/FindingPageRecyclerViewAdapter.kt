package com.example.icemusic.adapter.recyclerAdapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.icemusic.viewModel.BaseViewModel

class FindingPageRecyclerViewAdapter(var lifecycleOwner:LifecycleOwner) :RecyclerView.Adapter<FindingPageRecyclerViewAdapter.ViewHolder>(){
    val TAG = "FindingPageAdapter"

    var viewModelList: MutableList<BaseViewModel>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
//        Log.i(TAG,""+viewType)
        //使用layoutId作为viewType
        var viewDataBinding:ViewDataBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent,false)
        Log.i(TAG,viewDataBinding::class.java.name)
        return ViewHolder(viewDataBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(viewModelList!![position],lifecycleOwner)
        Log.i(TAG,"bind view")
    }

    override fun getItemCount(): Int {
        Log.i(TAG,"size: "+viewModelList?.size)
        return viewModelList?.size?:0
    }

    override fun getItemViewType(position: Int): Int {
        return viewModelList!![position].layoutId
    }

    class ViewHolder(val viewDataBinding:ViewDataBinding): RecyclerView.ViewHolder(viewDataBinding.root) {
        fun bind(baseViewModel:BaseViewModel,lifecycleOwner: LifecycleOwner){
            baseViewModel.bindData(viewDataBinding,lifecycleOwner)
            viewDataBinding.executePendingBindings()
        }
    }
}
