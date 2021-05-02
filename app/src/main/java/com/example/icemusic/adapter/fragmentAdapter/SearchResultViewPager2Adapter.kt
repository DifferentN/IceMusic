package com.example.icemusic.adapter.fragmentAdapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.icemusic.data.searchData.searchResultData.SearchResultTabData
import com.example.icemusic.fragment.searchFragment.SearchResultContentFragment
import com.example.icemusic.fragment.searchFragment.SearchResultFragment
import com.example.icemusic.viewModel.BaseViewModel
import com.example.icemusic.viewModel.searchPageVM.SearchResultContentVM

class SearchResultViewPager2Adapter(var searchWord:String,
                                    var tabDataList:MutableList<SearchResultTabData>?,
                                    fragment:Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return tabDataList?.size?:0
    }

    override fun createFragment(position: Int): Fragment {
        var fragment = SearchResultContentFragment()
        var arguments:Bundle = Bundle().apply {
            putString(SearchResultContentFragment.SONG_NAME,searchWord)
            putParcelable(SearchResultContentFragment.SEARCH_RESULT_TAB,tabDataList!![position])
        }
        fragment.arguments = arguments
        return fragment
    }

}