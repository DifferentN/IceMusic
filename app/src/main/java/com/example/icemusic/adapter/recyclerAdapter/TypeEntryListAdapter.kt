package com.example.icemusic.adapter.recyclerAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.icemusic.BR
import com.example.icemusic.R
import com.example.icemusic.data.TypeEntryCellData
import com.example.icemusic.viewModel.BaseViewModel
import com.example.icemusic.viewModel.TypeEntryCellViewModel

class TypeEntryListAdapter(var lifecycleOwner: LifecycleOwner): RecyclerView.Adapter<TypeEntryListAdapter.TypeEntryListViewHolder>(){

    var viewModelList:MutableList<out BaseViewModel>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeEntryListViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        var binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, R.layout.type_entry_cell,parent,false)
        var viewHolder = TypeEntryListViewHolder(binding)
        return viewHolder
    }

    override fun onBindViewHolder(holder: TypeEntryListViewHolder, position: Int) {
        holder.bindData(viewModelList!![position],lifecycleOwner)
    }

    override fun getItemCount(): Int {
        return viewModelList?.size?:0
    }

    class TypeEntryListViewHolder(val binding:ViewDataBinding):RecyclerView.ViewHolder(binding.root){
        fun bindData(viewModel:BaseViewModel,lifecycleOwner: LifecycleOwner){
            viewModel.bindData(binding,lifecycleOwner)
        }

    }

}