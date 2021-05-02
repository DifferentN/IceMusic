package com.example.icemusic.fragment.searchFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.icemusic.adapter.recyclerAdapter.base.BaseRecyclerViewAdapter
import com.example.icemusic.data.searchData.searchResultData.SearchResultTabData
import com.example.icemusic.databinding.SearchResultContentPageBinding
import com.example.icemusic.viewModel.searchPageVM.SearchResultContentVM
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchResultContentFragment():Fragment() {
    val TAG = "SearchResultContent"

    companion object{
        val SONG_NAME = "SONG_NAME"
        val SEARCH_RESULT_TAB = "SEARCH_RESULT_TAB"
    }

    lateinit var searchResultContentPageBinding:SearchResultContentPageBinding

    val searchResultContentVM:SearchResultContentVM by viewModels()

    var songName:String? = null
    var searchResultTabData:SearchResultTabData? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchResultContentPageBinding = SearchResultContentPageBinding.inflate(inflater,container,false)
        return searchResultContentPageBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var recyclerView = searchResultContentPageBinding.searchResultContentRecyclerView

        arguments?.let {
            songName = it.getString(SONG_NAME)?:""
            it.getParcelable<SearchResultTabData>(SEARCH_RESULT_TAB)?.let{
                searchResultTabData = it
            }
        }
        Log.i(TAG,"songName: $songName")
        Log.i(TAG,searchResultTabData?.tabTitle?:"no")

        //这里在创建ViewModel时，不使用vmStore，是否合适？
        var adapter = BaseRecyclerViewAdapter(this,
            this,
            BaseRecyclerViewAdapter.FAKE_BASE_VIEW_MODEL_CREATOR)
        recyclerView.adapter = adapter

        var linearLayoutManager = LinearLayoutManager(this.requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager

        searchResultContentVM.searchResultSongList.observe(viewLifecycleOwner, Observer {
            adapter.dataList = it
            adapter.notifyDataSetChanged()
        })

        //根据resultTabData和搜索词加载对应搜索结果页面中的内容
        searchResultContentVM.loadSearchResult(searchResultTabData,songName)

    }
}