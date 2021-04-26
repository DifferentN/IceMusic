package com.example.icemusic.fragment.searchFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.icemusic.adapter.fragmentAdapter.SearchResultViewPager2Adapter
import com.example.icemusic.data.eventBus.SearchChangeEvent
import com.example.icemusic.data.searchData.SearchResultTabData
import com.example.icemusic.databinding.SearchResultBinding
import com.example.icemusic.databinding.SearchResultTabLayoutCellBinding
import com.example.icemusic.databinding.TabCellViewBinding
import com.example.icemusic.db.MusicDatabaseInstance
import com.example.icemusic.db.entity.SearchHistorySong
import com.example.icemusic.viewModel.searchPageVM.SearchResultContentVM
import com.example.icemusic.viewModel.searchPageVM.SearchResultSingleSongVM
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class SearchResultFragment:Fragment() {

    lateinit var searchResultBinding:SearchResultBinding

    val args:SearchResultFragmentArgs by navArgs<SearchResultFragmentArgs>()

    var tabDataList :MutableList<SearchResultTabData>? = null
    lateinit var searchResultContentVMList :MutableList<SearchResultContentVM>
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

        var adapter = SearchResultViewPager2Adapter(searchWord,searchResultContentVMList,this)
        searchResultBinding.searchResultViewPager2.adapter = adapter

        TabLayoutMediator(searchResultBinding.searchResultTabLayout,searchResultBinding.searchResultViewPager2){ tab: TabLayout.Tab, position: Int ->
            var binding = SearchResultTabLayoutCellBinding.inflate(inflater)
            binding.obj = tabDataList!!.get(position)
            tab.customView = binding.root
        }.attach()
    }

    fun createTabData(searchWord:String){
        tabDataList = mutableListOf()
        tabDataList?.apply {
            add(SearchResultTabData("单曲"))
            add(SearchResultTabData("单曲"))
            add(SearchResultTabData("单曲"))
            add(SearchResultTabData("单曲"))
            add(SearchResultTabData("单曲"))
            add(SearchResultTabData("单曲"))
            add(SearchResultTabData("单曲"))
        }

        searchResultContentVMList = mutableListOf()
        searchResultContentVMList.apply {
            add(SearchResultSingleSongVM(searchWord))
            add(SearchResultSingleSongVM(searchWord))
            add(SearchResultSingleSongVM(searchWord))
            add(SearchResultSingleSongVM(searchWord))
            add(SearchResultSingleSongVM(searchWord))
            add(SearchResultSingleSongVM(searchWord))
            add(SearchResultSingleSongVM(searchWord))
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onHandleSearchChange(searchChangeEvent:SearchChangeEvent){
        var viewPager2 = searchResultBinding.searchResultViewPager2
        var searchResultContentVM = searchResultContentVMList[viewPager2.currentItem]
        GlobalScope.launch {
            searchResultContentVM.updateViewModelList(searchChangeEvent.searchWord)
        }

    }
}