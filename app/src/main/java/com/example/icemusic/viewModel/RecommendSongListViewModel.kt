package com.example.icemusic.viewModel

import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.icemusic.R
import com.example.icemusic.adapter.recyclerAdapter.BaseFindingPageAdapter
import com.example.icemusic.databinding.RecommendSongListBinding
import com.example.icemusic.itemDecoration.StartEndDecoration

class RecommendSongListViewModel: BaseViewModel() {

    val TAG = "RecommendSongListVM"

    val recommendSongCellVMList: MutableLiveData<MutableList<RecommendSongCellViewModel>> by lazy{
        MutableLiveData<MutableList<RecommendSongCellViewModel>>().also {
            it.value = mutableListOf()
        }
    }
    init {
        layoutId = R.layout.recommend_song_list
    }
    override fun bindData(viewDataBinding: ViewDataBinding, lifecycleOwner: LifecycleOwner) {
        viewDataBinding.takeIf { it is RecommendSongListBinding }?.let {
            Log.i(TAG,"RecommendSongListViewModel bind data")
            var recommendSongListBinding = it as RecommendSongListBinding
            if(recommendSongListBinding.recommendSongRecyclerView.adapter!=null){
                return@let
            }
            var recyclerView = recommendSongListBinding.recommendSongRecyclerView
            var adapter = BaseFindingPageAdapter(lifecycleOwner)
            recyclerView.adapter = adapter

            var layoutManager = LinearLayoutManager(recyclerView.context)
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            recyclerView.layoutManager = layoutManager

            var startBlankWidth = recommendSongListBinding.root.resources.getDimension(R.dimen.finding_page_start_blank_width).toInt()
            var endBlankWidth = recommendSongListBinding.root.resources.getDimension(R.dimen.finding_page_end_blank_width).toInt()
            recyclerView.addItemDecoration(StartEndDecoration(startBlankWidth, endBlankWidth))

            recommendSongCellVMList.observe(lifecycleOwner, Observer {
                adapter.viewModelList = it
                adapter.notifyDataSetChanged()
            })

        }
    }
}