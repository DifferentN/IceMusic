package com.example.icemusic.viewModel.searchPageVM

import android.content.Context
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.icemusic.R
import com.example.icemusic.adapter.recyclerAdapter.base.BaseRecyclerViewAdapter
import com.example.icemusic.databinding.SearchHistorySongListBinding
import com.example.icemusic.db.MusicDatabaseInstance
import com.example.icemusic.db.entity.SearchHistorySong
import com.example.icemusic.itemDecoration.MediumHorizontalBlankDecoration
import com.example.icemusic.listener.StartEndFadeScrollListener
import com.example.icemusic.view.GradientRecyclerView
import com.example.icemusic.viewModel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SearchHistorySongListVM : BaseViewModel() {
    val TAG = "SearchHistorySongListVM"

    init {
        layoutId = R.layout.search_history_song_list
    }

    var searchHistorySongList = MutableLiveData<MutableList<SearchHistorySongCellVM>>()

    var dbSongList:LiveData<List<SearchHistorySong>>? = null

    override fun bindData(viewDataBinding: ViewDataBinding, lifecycleOwner: LifecycleOwner) {
        viewDataBinding.takeIf { it is SearchHistorySongListBinding }?.let {
            var searchHistorySongListBinding = it as SearchHistorySongListBinding
            searchHistorySongListBinding.vm = this

            if(searchHistorySongListBinding.historySongRecyclerView.adapter==null){
                initRecyclerView(searchHistorySongListBinding.historySongRecyclerView,lifecycleOwner)
            }


        }
    }

    fun initRecyclerView(recyclerView:GradientRecyclerView, lifecycleOwner: LifecycleOwner){
        var adapter = BaseRecyclerViewAdapter(lifecycleOwner)

        var linearLayoutManager = LinearLayoutManager(recyclerView.context)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        recyclerView.adapter = adapter
        recyclerView.layoutManager = linearLayoutManager

        var mediumBlankSize = recyclerView.resources.getDimension(R.dimen.search_history_song_item_medium_blank).toInt()
        var decoration = MediumHorizontalBlankDecoration(mediumBlankSize)

        recyclerView.addItemDecoration(decoration)

        var startEndFadeScrollListener = StartEndFadeScrollListener()
        recyclerView.addOnScrollListener(startEndFadeScrollListener)

        searchHistorySongList.observe(lifecycleOwner, Observer {
            adapter.viewModelList = it
            adapter.notifyDataSetChanged()
        })

        loadSearchHistorySongList(recyclerView.context.applicationContext,lifecycleOwner)
    }

    fun deleteSearchHistorySong(view:View){
        GlobalScope.launch {
            dbSongList?.let {
                it.value?.let {
                    var db = MusicDatabaseInstance.getInstance(view.context.applicationContext)
                    db.searchHistorySongDao().deleteSearchHistorySong(it)
                }
            }
        }
        searchHistorySongList.value?.clear()
    }

    fun loadSearchHistorySongList(applicationContext: Context, lifecycleOwner: LifecycleOwner){
        var db = MusicDatabaseInstance.getInstance(applicationContext)
        dbSongList = db.searchHistorySongDao().getSearchHistorySong()
        var newHistoryList = mutableListOf<SearchHistorySongCellVM>()
        dbSongList?.observe(lifecycleOwner, Observer {
            for(song in it){
                var songCellVM = SearchHistorySongCellVM()
                songCellVM.songName = song.songName!!
                newHistoryList.add(songCellVM)
            }
            searchHistorySongList.value = newHistoryList
        })
    }
}