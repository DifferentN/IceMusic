package com.example.icemusic.viewModel.findPageVM

import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.icemusic.R
import com.example.icemusic.adapter.recyclerAdapter.base.BaseRecyclerViewAdapter
import com.example.icemusic.data.AdvertData
import com.example.icemusic.data.RecommendSongCellData
import com.example.icemusic.databinding.RecommendSongListBinding
import com.example.icemusic.itemDecoration.StartEndDecoration
import com.example.icemusic.viewModel.BaseViewModel

class RecommendSongListViewModel: BaseViewModel() {

    val TAG = "RecommendSongListVM"

//    val recommendSongCellVMList: MutableLiveData<MutableList<RecommendSongCellViewModel>> by lazy{
//        MutableLiveData<MutableList<RecommendSongCellViewModel>>().also {
//            it.value = mutableListOf()
//        }
//    }

    val recommendSongDataList : MutableLiveData<MutableList<RecommendSongCellData>> by lazy {
        MutableLiveData<MutableList<RecommendSongCellData>>().also {
            it.value = mutableListOf()
        }
    }
    init {
        layoutId = R.layout.recommend_song_list
    }

    override fun <T> initialData(data: T) {
        data?.let {
            var dataClazz = it::class.java
            if(MutableList::class.java.isAssignableFrom(dataClazz)){
                var list = data as MutableList<*>
                if(!list.isEmpty()){
                    var element = list[0]
                    element?.let {
                        var elementClazz = it::class.java
                        if(RecommendSongCellData::class.java.isAssignableFrom(elementClazz)){
                            recommendSongDataList.value = data as MutableList<RecommendSongCellData>
                        }
                    }
                }
            }
        }
    }

    override fun bindData(viewDataBinding: ViewDataBinding, lifecycleOwner: LifecycleOwner, viewModelStoreOwner: ViewModelStoreOwner) {
        viewDataBinding.takeIf { it is RecommendSongListBinding }?.let {
            Log.i(TAG,"RecommendSongListViewModel bind data")
            var recommendSongListBinding = it as RecommendSongListBinding
            if(recommendSongListBinding.recommendSongRecyclerView.adapter!=null){
                return@let
            }
            var recyclerView = recommendSongListBinding.recommendSongRecyclerView
            var adapter = BaseRecyclerViewAdapter(lifecycleOwner,viewModelStoreOwner,BaseRecyclerViewAdapter.FAKE_BASE_VIEW_MODEL_CREATOR)
            recyclerView.adapter = adapter

            var layoutManager = LinearLayoutManager(recyclerView.context)
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            recyclerView.layoutManager = layoutManager

            var startBlankWidth = recommendSongListBinding.root.resources.getDimension(R.dimen.finding_page_start_blank_width).toInt()
            var endBlankWidth = recommendSongListBinding.root.resources.getDimension(R.dimen.finding_page_end_blank_width).toInt()
            recyclerView.addItemDecoration(StartEndDecoration(startBlankWidth, endBlankWidth))

            recommendSongDataList.observe(lifecycleOwner, Observer {
                adapter.dataList = it
                adapter.notifyDataSetChanged()
            })

        }
    }
}