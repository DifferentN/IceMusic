package com.example.icemusic.fragment.searchFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.icemusic.adapter.recyclerAdapter.base.BaseRecyclerViewAdapter
import com.example.icemusic.data.searchData.searchResultData.SearchResultTabData
import com.example.icemusic.databinding.SearchResultContentPageBinding
import com.example.icemusic.viewModel.searchPageVM.SearchResultContentVM
import com.example.icemusic.viewModel.searchPageVM.ShareSongNameViewModel

class SearchResultContentFragment():Fragment() {
    val TAG = "SearchResultContent"

    companion object{
        val SEARCH_RESULT_TAB = "SEARCH_RESULT_TAB"
    }

    lateinit var searchResultContentPageBinding:SearchResultContentPageBinding

    val searchResultContentVM:SearchResultContentVM by viewModels()

    //共享搜索词，并观察搜索词的变化;将vmStoreOwner设置为Activity
    val sharedSongNameVM:ShareSongNameViewModel by viewModels({requireActivity()})

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

        //获取选中的共享搜歌曲名称
        songName = sharedSongNameVM.searchWord.value

        arguments?.let {
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
        //观察歌曲名称的变化
        sharedSongNameVM.searchWord.observe(viewLifecycleOwner, Observer {
            searchResultContentVM.loadSearchResult(searchResultTabData,it)
        })
    }

    override fun onStart() {
        Log.i(TAG,"SearchResultContentFragment ${this.hashCode()} onStart")
        super.onStart()
    }

    override fun onResume() {
        Log.i(TAG,"SearchResultContentFragment ${this.hashCode()} onResume")
        super.onResume()
    }
}
