package com.example.icemusic.viewModel.findPageVM

import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.icemusic.R
import com.example.icemusic.adapter.recyclerAdapter.TypeEntryListAdapter
import com.example.icemusic.adapter.recyclerAdapter.base.BaseRecyclerViewAdapter
import com.example.icemusic.data.RecommendSongCellData
import com.example.icemusic.data.TypeEntryCellData
import com.example.icemusic.databinding.TypeEntryListBinding
import com.example.icemusic.itemDecoration.StartEndDecoration
import com.example.icemusic.viewModel.BaseViewModel

class TypeEntryListViewModel: BaseViewModel() {
    val TAG = "TypeEntryListViewModel"

//    val typeEntryCellList:MutableLiveData<MutableList<TypeEntryCellViewModel>> by lazy {
//        MutableLiveData<MutableList<TypeEntryCellViewModel>>().also {
//            it.value = mutableListOf<TypeEntryCellViewModel>()
//        }
//    }

    val typeEntryDataList:MutableLiveData<MutableList<TypeEntryCellData>> by lazy {
        MutableLiveData<MutableList<TypeEntryCellData>>().also {
            it.value = mutableListOf<TypeEntryCellData>()
        }
    }

    init {
        layoutId = R.layout.type_entry_list
    }

    override fun bindData(viewDataBinding: ViewDataBinding, lifecycleOwner: LifecycleOwner,viewModelStoreOwner: ViewModelStoreOwner) {
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

            var adapter = BaseRecyclerViewAdapter(lifecycleOwner,
                viewModelStoreOwner,
                BaseRecyclerViewAdapter.FAKE_BASE_VIEW_MODEL_CREATOR)
//            typeEntryListBinding.typeEntryRecyclerView.requestDisallowInterceptTouchEvent(true)
            typeEntryListBinding.typeEntryRecyclerView.adapter = adapter
            var layoutManager = LinearLayoutManager(viewDataBinding.root.context)
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            typeEntryListBinding.typeEntryRecyclerView.layoutManager = layoutManager
            typeEntryDataList.observe(lifecycleOwner, Observer {
                adapter.dataList = it
                adapter.notifyDataSetChanged()
            })
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
                        if(TypeEntryCellData::class.java.isAssignableFrom(elementClazz)){
                            typeEntryDataList.value = data as MutableList<TypeEntryCellData>
                        }
                    }
                }
            }
        }
    }
}