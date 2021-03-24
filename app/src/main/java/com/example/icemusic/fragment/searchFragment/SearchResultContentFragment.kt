package com.example.icemusic.fragment.searchFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.icemusic.adapter.recyclerAdapter.base.BaseRecyclerViewAdapter
import com.example.icemusic.databinding.SearchResultContentPageBinding
import com.example.icemusic.viewModel.searchPageVM.SearchResultContentVM
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchResultContentFragment(var searchResultContentVM:SearchResultContentVM):Fragment() {

    lateinit var searchResultContentPageBinding:SearchResultContentPageBinding

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

        GlobalScope.launch {
            searchResultContentVM.updateViewModelList()
        }

        var adapter = BaseRecyclerViewAdapter(this)
        recyclerView.adapter = adapter

        var linearLayoutManager = LinearLayoutManager(this.requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager

        searchResultContentVM.viewModelList.observe(viewLifecycleOwner, Observer {
            adapter.viewModelList = it
            adapter.notifyDataSetChanged()
        })

    }
}