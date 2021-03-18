package com.example.icemusic.viewModel

import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.icemusic.R
import com.example.icemusic.adapter.recyclerAdapter.TypeEntryListAdapter
import com.example.icemusic.databinding.TypeEntryListBinding
import com.example.icemusic.itemDecoration.StartEndDecoration

class TypeEntryListViewModel:BaseViewModel() {
    val TAG = "TypeEntryListViewModel"

    val typeEntryCellList:MutableLiveData<MutableList<TypeEntryCellViewModel>> by lazy {
        MutableLiveData<MutableList<TypeEntryCellViewModel>>().also {
            it.value = mutableListOf<TypeEntryCellViewModel>()
        }
    }

    init {
        layoutId = R.layout.type_entry_list
    }

    override fun bindData(viewDataBinding: ViewDataBinding, lifecycleOwner: LifecycleOwner) {
        Log.i(TAG,"adapter")
        viewDataBinding.takeIf { it is  TypeEntryListBinding}.let {
            Log.i(TAG,"adapter")
            var typeEntryListBinding = it as TypeEntryListBinding
            if(typeEntryListBinding.typeEntryRecyclerView.adapter!=null){
                return@let
            }

            var startBlankWidth = typeEntryListBinding.root.resources.getDimension(R.dimen.finding_page_start_blank_width).toInt()
            var endBlankWidth = typeEntryListBinding.root.resources.getDimension(R.dimen.finding_page_end_blank_width).toInt()
            typeEntryListBinding.typeEntryRecyclerView.addItemDecoration(StartEndDecoration(startBlankWidth,endBlankWidth))

            var adapter = TypeEntryListAdapter(lifecycleOwner)
//            typeEntryListBinding.typeEntryRecyclerView.requestDisallowInterceptTouchEvent(true)
            typeEntryListBinding.typeEntryRecyclerView.adapter = adapter
            var layoutManager = LinearLayoutManager(viewDataBinding.root.context)
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            typeEntryListBinding.typeEntryRecyclerView.layoutManager = layoutManager
            typeEntryCellList.observe(lifecycleOwner, Observer {
                adapter.viewModelList = it
                adapter.notifyDataSetChanged()
            })
        }
    }
}