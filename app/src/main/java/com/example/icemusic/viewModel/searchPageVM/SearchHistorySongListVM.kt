package com.example.icemusic.viewModel.searchPageVM

import android.content.Context
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.icemusic.R
import com.example.icemusic.adapter.recyclerAdapter.base.BaseRecyclerViewAdapter
import com.example.icemusic.data.TypeEntryCellData
import com.example.icemusic.databinding.SearchHistorySongListBinding
import com.example.icemusic.db.MusicDatabaseInstance
import com.example.icemusic.db.entity.SearchHistorySong
import com.example.icemusic.itemDecoration.MediumHorizontalBlankDecoration
import com.example.icemusic.listener.StartEndFadeScrollListener
import com.example.icemusic.view.GradientRecyclerView
import com.example.icemusic.viewModel.BaseViewModel
import kotlinx.coroutines.*

class SearchHistorySongListVM : BaseViewModel() {
    val TAG = "SearchHistorySongListVM"

    init {
        layoutId = R.layout.search_history_song_list
    }

//    var searchHistorySongList = MutableLiveData<MutableList<SearchHistorySongCellVM>>()

    var dbSongList:MutableLiveData<MutableList<SearchHistorySong>> = MutableLiveData()

    override fun bindData(viewDataBinding: ViewDataBinding, lifecycleOwner: LifecycleOwner,viewModelStoreOwner: ViewModelStoreOwner) {
        viewDataBinding.takeIf { it is SearchHistorySongListBinding }?.let {
            var searchHistorySongListBinding = it as SearchHistorySongListBinding
            searchHistorySongListBinding.vm = this

            if(searchHistorySongListBinding.historySongRecyclerView.adapter==null){
                initRecyclerView(searchHistorySongListBinding.historySongRecyclerView,lifecycleOwner,viewModelStoreOwner)
            }
        }
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
                        if(SearchHistorySong::class.java.isAssignableFrom(elementClazz)){
                            dbSongList.value = data as MutableList<SearchHistorySong>
                        }
                    }
                }
            }
        }
    }

    fun initRecyclerView(recyclerView:GradientRecyclerView, lifecycleOwner: LifecycleOwner, viewModelStoreOwner: ViewModelStoreOwner){
        var adapter = BaseRecyclerViewAdapter(lifecycleOwner,
            viewModelStoreOwner,
            BaseRecyclerViewAdapter.FAKE_BASE_VIEW_MODEL_CREATOR)

        var linearLayoutManager = LinearLayoutManager(recyclerView.context)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        recyclerView.adapter = adapter
        recyclerView.layoutManager = linearLayoutManager

        var mediumBlankSize = recyclerView.resources.getDimension(R.dimen.search_history_song_item_medium_blank).toInt()
        var decoration = MediumHorizontalBlankDecoration(mediumBlankSize)

        recyclerView.addItemDecoration(decoration)

        var startEndFadeScrollListener = StartEndFadeScrollListener()
        recyclerView.addOnScrollListener(startEndFadeScrollListener)

        dbSongList?.observe(lifecycleOwner, Observer {
            adapter.dataList = it
            adapter.notifyDataSetChanged()
        })

        loadSearchHistorySongList(recyclerView.context.applicationContext,lifecycleOwner)
    }

    fun deleteSearchHistorySong(view:View){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                dbSongList?.let {
                    it.value?.let {
                        var db = MusicDatabaseInstance.getInstance(view.context.applicationContext)
                        db.searchHistorySongDao().deleteSearchHistorySong(it)
                    }
                }
            }
            dbSongList.value?.clear()
        }
    }

    fun loadSearchHistorySongList(applicationContext: Context, lifecycleOwner: LifecycleOwner){
        viewModelScope.launch {
            var db = MusicDatabaseInstance.getInstance(applicationContext)
            dbSongList.value = withContext(Dispatchers.IO){
                return@withContext db.searchHistorySongDao().getSearchHistorySong()
            }

        }


    }
}