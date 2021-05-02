package com.example.icemusic.adapter.recyclerAdapter.base

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.icemusic.dataViewModelMap.DataViewModelMapperRegistry
import com.example.icemusic.viewModel.BaseViewModel

class BaseRecyclerViewAdapter(private val lifecycleOwner:LifecycleOwner,
                              private val viewModelStoreOwner: ViewModelStoreOwner,
                              private val baseViewModelCreator: BaseViewModelCreator):
    RecyclerView.Adapter<BaseRecyclerViewAdapter.BaseFindingPageViewHolder>() {

    val TAG = "BaseRecyclerViewAdapter"

    companion object{
        var NORMAL_BASE_VIEW_MODEL_CREATOR = NormalBaseViewModelCreator()
        var FAKE_BASE_VIEW_MODEL_CREATOR = FakeBaseViewModeCreator()
    }

    private var viewModelList:MutableList<out BaseViewModel>? = null

    var dataList:MutableList<out Any>? = null
        set(value) {
            field = value
            transformToViewModel()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseFindingPageViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        //使用layoutId作为viewType,使用AdIndicatorViewBinding.inflate方法，会导致视图不显示
        var viewDataBinding:ViewDataBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent,false)
        return BaseFindingPageViewHolder(viewDataBinding)
    }

    override fun onBindViewHolder(holder: BaseFindingPageViewHolder, position: Int) {
        holder.bindData(viewModelList!![position],lifecycleOwner,viewModelStoreOwner)
    }

    override fun getItemCount(): Int {
        return viewModelList?.size?:0
    }

    override fun getItemViewType(position: Int): Int {
        return viewModelList!![position].layoutId
    }

    /**
     * 将dataList转化为ViewModelList
     * 转化规则：根据BaseViewModelCreator类型而定（在创建BaseViewModel时，可能使用viewModelStoreOwner，也可能不使用）
     * 1、元素是某个确定的数据类，转化为数据类对应的ViewModel
     * 2、元素是链表类型，根据链表内的数据类型转化为对应的ViewModel
     */
    private fun transformToViewModel(){
        var classLoader = Thread.currentThread().contextClassLoader
        var newViewModelList = mutableListOf<BaseViewModel>()
        dataList?.let {
            for(data in it){
                //根据数据元素的类型，获取不同类型的ViewModel
                var viewModelName = DataViewModelMapperRegistry.getViewModelClassNameForData(data)
                var clazz = classLoader.loadClass(viewModelName) as Class<BaseViewModel>
                var baseViewModel = baseViewModelCreator.createViewModel(clazz,viewModelStoreOwner)
                //使用加载好的Data初始化BaseViewModel中的数据
                baseViewModel.initialData(data)
                newViewModelList.add(baseViewModel)
            }
        }
        viewModelList = newViewModelList
    }


    class BaseFindingPageViewHolder(var viewDataBinding:ViewDataBinding):
        RecyclerView.ViewHolder(viewDataBinding.root) {

        fun bindData(baseViewModel: BaseViewModel, lifecycleOwner: LifecycleOwner,viewModelStoreOwner: ViewModelStoreOwner){
            baseViewModel.bindData(viewDataBinding,lifecycleOwner,viewModelStoreOwner)
            viewDataBinding.executePendingBindings()
        }
    }

}