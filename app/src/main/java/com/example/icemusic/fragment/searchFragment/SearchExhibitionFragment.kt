package com.example.icemusic.fragment.searchFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.icemusic.adapter.recyclerAdapter.base.BaseRecyclerViewAdapter
import com.example.icemusic.databinding.SearchExhibitionBinding
import com.example.icemusic.viewModel.BaseViewModel
import com.example.icemusic.viewModel.searchPageVM.SearchHistorySongListVM

class SearchExhibitionFragment:Fragment() {

    lateinit var searchExhibitionBinding: SearchExhibitionBinding

    var vmList:MutableLiveData<MutableList<BaseViewModel>> = MutableLiveData()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchExhibitionBinding = SearchExhibitionBinding.inflate(inflater,container,false)
        return searchExhibitionBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var recyclerView = searchExhibitionBinding.searchExhibitionRecyclerView

        var adapter = BaseRecyclerViewAdapter(viewLifecycleOwner)
        recyclerView.adapter = adapter

        var linearManager = LinearLayoutManager(context)
        linearManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearManager

        vmList.observe(viewLifecycleOwner, Observer {
            adapter.viewModelList = it
            adapter.notifyDataSetChanged()
        })

        loadVMList()

    }

    fun loadVMList(){
        var vm = ViewModelProvider(this).get(SearchHistorySongListVM::class.java)
        var newVMList = mutableListOf<BaseViewModel>()
        newVMList.add(vm)

        vmList.value = newVMList
    }

}