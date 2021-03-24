package com.example.icemusic.adapter.fragmentAdapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.icemusic.fragment.searchFragment.SearchResultContentFragment
import com.example.icemusic.viewModel.BaseViewModel
import com.example.icemusic.viewModel.searchPageVM.SearchResultContentVM

class SearchResultViewPager2Adapter(var searchWord:String,
                                    var resultContentViewModelList:MutableList<SearchResultContentVM>,
                                    fragment:Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return resultContentViewModelList.size
    }

    override fun createFragment(position: Int): Fragment {
        var fragment = SearchResultContentFragment(resultContentViewModelList[position])
        return fragment
    }

}