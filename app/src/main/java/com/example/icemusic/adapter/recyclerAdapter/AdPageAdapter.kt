package com.example.icemusic.adapter.recyclerAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.example.icemusic.BR
import com.example.icemusic.R
import com.example.icemusic.data.AdvertData
import com.example.icemusic.databinding.SingleAdPageBinding

/**
 * 不使用viewPager，因为viewPager的视图预加载会导致，在视图数量少于3时，视图不显示
 */
class AdPageAdapter : RecyclerView.Adapter<AdPageAdapter.PageViewHolder>() {
    val TAG = "AdPageAdapter"

    val max = Int.MAX_VALUE

    var adDataList: MutableList<AdvertData>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        var inflater = LayoutInflater.from(parent.context)
        //不能直接使用SingleAdPageBinding.inflate
        var binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.single_ad_page,parent,false)
        return PageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        adDataList?.takeIf { adDataList!!.size>0 }.let {
            var pos = position%adDataList!!.size
            holder.bind(adDataList!![pos])
        }
    }

    override fun getItemCount(): Int {
        return max
    }



    class PageViewHolder(val binding:ViewDataBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(advertData: AdvertData){
            binding.setVariable(BR.obj,advertData)
            binding.executePendingBindings()
        }
    }
}