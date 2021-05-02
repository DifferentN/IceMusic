package com.example.icemusic.adapter.recyclerAdapter.base

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.icemusic.dataViewModelMap.DataViewModelMapperRegistry
import com.example.icemusic.viewModel.BaseViewModel

interface BaseViewModelCreator {
    val TAG: String
        get() = "BaseViewModelCreator"

    fun createViewModel(clazz:Class<out BaseViewModel>,owner:ViewModelStoreOwner):BaseViewModel
}
class NormalBaseViewModelCreator:BaseViewModelCreator{

    /**
     * 使用ViewModelStoreOwner创建BaseViewModel，即ViewModel的正常创建
     * @param clazz Class<BaseViewModel>
     * @param owner ViewModelStoreOwner
     */
    override fun createViewModel(clazz: Class<out BaseViewModel>, owner: ViewModelStoreOwner):BaseViewModel {

        var viewModelProvider = ViewModelProvider(owner)
        var baseViewModel:BaseViewModel = viewModelProvider.get(clazz)
        return baseViewModel
    }
}

class FakeBaseViewModeCreator:BaseViewModelCreator{
    /**
     * 不使用ViewModelStoreOwner创建，相当于创建一个含有用户逻辑处理的Data类
     * @param clazz Class<BaseViewModel>
     * @param owner ViewModelStoreOwner
     */
    override fun createViewModel(clazz: Class<out BaseViewModel>, owner: ViewModelStoreOwner):BaseViewModel {
        var baseViewModel = clazz.newInstance()
        return baseViewModel
    }

}
