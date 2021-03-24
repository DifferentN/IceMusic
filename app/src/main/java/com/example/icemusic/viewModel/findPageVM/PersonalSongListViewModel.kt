package com.example.icemusic.viewModel.findPageVM

import androidx.databinding.ViewDataBinding
import androidx.databinding.adapters.ListenerUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.icemusic.R
import com.example.icemusic.adapter.recyclerAdapter.base.BaseRecyclerViewAdapter
import com.example.icemusic.databinding.PersonalSongListBinding
import com.example.icemusic.itemDecoration.StartMatchEndDecoration
import com.example.icemusic.listener.OnScrollListener
import com.example.icemusic.viewModel.BaseViewModel

class PersonalSongListViewModel: BaseViewModel() {
    val TAG = "PersonalSongListVM"

    val personalSongTripleList : MutableLiveData<MutableList<PersonalSongCellTripleViewModel>> by lazy {
        MutableLiveData<MutableList<PersonalSongCellTripleViewModel>>().also {
            it.value = mutableListOf()
        }
    }

    init {
        layoutId = R.layout.personal_song_list
    }

    override fun bindData(viewDataBinding: ViewDataBinding, lifecycleOwner: LifecycleOwner) {
        viewDataBinding.takeIf { it is PersonalSongListBinding }.also {
            var personalSongListBinding = it as PersonalSongListBinding
            if(personalSongListBinding.personalSongRecyclerView.adapter==null){
                baseBindRecyclerView(personalSongListBinding.personalSongRecyclerView,lifecycleOwner,personalSongTripleList)

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

    override fun baseBindRecyclerView(
        recyclerView: RecyclerView,
        lifecycleOwner: LifecycleOwner,
        baseViewModelList: MutableLiveData<out MutableList<out BaseViewModel>>
    ) {
        var adapter = BaseRecyclerViewAdapter(lifecycleOwner)
        recyclerView.adapter = adapter

        var layoutManager = LinearLayoutManager(recyclerView.context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = layoutManager

        var startBlankWidth = recyclerView.resources.getDimension(R.dimen.finding_page_start_blank_width).toInt()
        var endBlankWidth = recyclerView.resources.getDimension(R.dimen.finding_page_end_blank_width).toInt()
        recyclerView.addItemDecoration(StartMatchEndDecoration(startBlankWidth))

        baseViewModelList.observe(lifecycleOwner, Observer {
            adapter.viewModelList = it
            adapter.notifyDataSetChanged()
        })
    }
}