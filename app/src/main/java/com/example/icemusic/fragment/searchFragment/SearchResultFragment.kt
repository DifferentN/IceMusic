package com.example.icemusic.fragment.searchFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.navArgs
import com.example.icemusic.adapter.fragmentAdapter.SearchResultViewPager2Adapter
import com.example.icemusic.data.eventBus.SearchChangeEvent
import com.example.icemusic.data.searchData.searchResultData.SearchResultTabData
import com.example.icemusic.data.searchData.searchResultData.SearchResultTabType
import com.example.icemusic.databinding.SearchResultBinding
import com.example.icemusic.databinding.SearchResultTabLayoutCellBinding
import com.example.icemusic.viewModel.searchPageVM.SearchResultContentVM
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class SearchResultFragment:Fragment() {
    val TAG = "SearchResultFragment"

    lateinit var searchResultBinding:SearchResultBinding

    val args:SearchResultFragmentArgs by navArgs<SearchResultFragmentArgs>()

    val tabDataList :MutableLiveData<MutableList<SearchResultTabData>> by lazy {
        MutableLiveData<MutableList<SearchResultTabData>>().also {
            it.value = mutableListOf<SearchResultTabData>()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchResultBinding = SearchResultBinding.inflate(inflater,container,false)
        return searchResultBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventBus.getDefault().register(this)

        var inflater = LayoutInflater.from(this.requireContext())

        var searchWord = args.searchWord

        createTabData(searchWord)

        var adapter = SearchResultViewPager2Adapter(searchWord,tabDataList.value,this)
        searchResultBinding.searchResultViewPager2.adapter = adapter

        TabLayoutMediator(searchResultBinding.searchResultTabLayout,searchResultBinding.searchResultViewPager2){ tab: TabLayout.Tab, position: Int ->
            var binding = SearchResultTabLayoutCellBinding.inflate(inflater)
            binding.obj = tabDataList.value!!.get(position)
            tab.customView = binding.root
        }.attach()
        Log.i(TAG,"onViewCreated SearchResultFragment")
    }

    fun createTabData(searchWord:String){
        var newTabDataList = mutableListOf<SearchResultTabData>()
        newTabDataList?.apply {
            add(SearchResultTabData("单曲",SearchResultTabType.SINGLE_SONG_TYPE))
            add(SearchResultTabData("单曲",SearchResultTabType.SINGLE_SONG_TYPE))
            add(SearchResultTabData("单曲",SearchResultTabType.SINGLE_SONG_TYPE))
            add(SearchResultTabData("单曲",SearchResultTabType.SINGLE_SONG_TYPE))
            add(SearchResultTabData("单曲",SearchResultTabType.SINGLE_SONG_TYPE))
            add(SearchResultTabData("单曲",SearchResultTabType.SINGLE_SONG_TYPE))
            add(SearchResultTabData("单曲",SearchResultTabType.SINGLE_SONG_TYPE))
        }
        tabDataList.value = newTabDataList
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onHandleSearchChange(searchChangeEvent:SearchChangeEvent){
        var viewPager2 = searchResultBinding.searchResultViewPager2
        var resultTabData = tabDataList.value!![viewPager2.currentItem]
    }
}