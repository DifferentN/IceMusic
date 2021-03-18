package com.example.icemusic.adapter.recyclerAdapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.icemusic.BR
import com.example.icemusic.R
import com.example.icemusic.data.AdIndicatorData
import com.example.icemusic.databinding.AdIndicatorViewBinding

class AdIndicatorAdapter : RecyclerView.Adapter<AdIndicatorAdapter.AdIndicatorViewHolder>() {

    companion object  {
        val TAG = "AdIndicatorAdapter"
    }

    var adIndicatorDataList: MutableList<AdIndicatorData>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdIndicatorViewHolder {
        var inflater = LayoutInflater.from(parent.context)
        //使用AdIndicatorViewBinding.inflate方法，会导致视图不显示
        var binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.ad_indicator_view,parent,false)
        return AdIndicatorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdIndicatorViewHolder, position: Int) {
        adIndicatorDataList?.let {
            holder.bind(it[position])
        }
    }

    override fun getItemCount(): Int {
        return adIndicatorDataList?.size?:0
    }

    class AdIndicatorViewHolder(var binding:ViewDataBinding) :RecyclerView.ViewHolder(binding.root){
        fun bind(adIndicatorData:AdIndicatorData){
            binding.setVariable(BR.obj,adIndicatorData)
            binding.executePendingBindings()
        }
    }
}