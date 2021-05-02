package com.example.icemusic.viewModel.findPageVM

import androidx.databinding.ViewDataBinding
import androidx.databinding.adapters.ListenerUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.icemusic.R
import com.example.icemusic.adapter.recyclerAdapter.base.BaseRecyclerViewAdapter
import com.example.icemusic.data.PersonalSongTripleCellData
import com.example.icemusic.data.RecommendSongCellData
import com.example.icemusic.databinding.PersonalSongListBinding
import com.example.icemusic.itemDecoration.StartMatchEndDecoration
import com.example.icemusic.listener.OnScrollListener
import com.example.icemusic.viewModel.BaseViewModel

class PersonalSongListViewModel: BaseViewModel() {
    val TAG = "PersonalSongListVM"

//    val personalSongTripleList : MutableLiveData<MutableList<PersonalSongCellTripleViewModel>> by lazy {
//        MutableLiveData<MutableList<PersonalSongCellTripleViewModel>>().also {
//            it.value = mutableListOf()
//        }
//    }

    val personalSongTripleDataList: MutableLiveData<MutableList<PersonalSongTripleCellData>> by lazy {
        MutableLiveData<MutableList<PersonalSongTripleCellData>>()
    }

    init {
        layoutId = R.layout.personal_song_list
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
                        if(PersonalSongTripleCellData::class.java.isAssignableFrom(elementClazz)){
                            personalSongTripleDataList.value = data as MutableList<PersonalSongTripleCellData>
                        }
                    }
                }
            }
        }
    }

    override fun bindData(viewDataBinding: ViewDataBinding, lifecycleOwner: LifecycleOwner, viewModelStoreOwner: ViewModelStoreOwner) {
        viewDataBinding.takeIf { it is PersonalSongListBinding }.also {
            var personalSongListBinding = it as PersonalSongListBinding
            if(personalSongListBinding.personalSongRecyclerView.adapter==null){
                bindRecyclerView(personalSongListBinding.personalSongRecyclerView,lifecycleOwner,viewModelStoreOwner)

                var recyclerView = personalSongListBinding.personalSongRecyclerView
                var newListener = OnScrollListener()
                var oldListener = ListenerUtil.trackListener(recyclerView,newListener,R.id.onScrollStateChanged)
                if (oldListener != null) {
                    recyclerView.removeOnScrollListener(oldListener)
                }
                if (newListener != null) {
                    recyclerView.addOnScrollListener(newListener)
                }
            }
        }
    }

    fun bindRecyclerView(
        recyclerView: RecyclerView,
        lifecycleOwner: LifecycleOwner,
        viewModelStoreOwner: ViewModelStoreOwner
    ) {
        var adapter = BaseRecyclerViewAdapter(lifecycleOwner,
            viewModelStoreOwner,
            BaseRecyclerViewAdapter.FAKE_BASE_VIEW_MODEL_CREATOR)
        recyclerView.adapter = adapter

        var layoutManager = LinearLayoutManager(recyclerView.context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = layoutManager

        var startBlankWidth = recyclerView.resources.getDimension(R.dimen.finding_page_start_blank_width).toInt()
        var endBlankWidth = recyclerView.resources.getDimension(R.dimen.finding_page_end_blank_width).toInt()
        recyclerView.addItemDecoration(StartMatchEndDecoration(startBlankWidth))

        personalSongTripleDataList.observe(lifecycleOwner, Observer {
            adapter.dataList = it
            adapter.notifyDataSetChanged()
        })
    }
}