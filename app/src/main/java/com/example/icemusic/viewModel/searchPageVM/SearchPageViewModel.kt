package com.example.icemusic.viewModel.searchPageVM


import android.util.Log
import android.view.View
import androidx.databinding.Observable
import androidx.databinding.Observable.OnPropertyChangedCallback
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.example.icemusic.R
import com.example.icemusic.viewModel.BaseViewModel
import org.jsoup.Connection

class SearchPageViewModel: BaseViewModel(){

    val TAG = "SearchPageViewModel"

    val queryText:ObservableField<String> = ObservableField<String>("")

    val hintWordVMList:MutableLiveData<MutableList<BaseViewModel>> by lazy {
        MutableLiveData<MutableList<BaseViewModel>>().also {
            it.value = mutableListOf()
        }
    }

    var hintRecyclerViewVisible:ObservableInt = ObservableInt(View.INVISIBLE)
    init {
        layoutId = R.layout.search_main_page
    }

    fun setCallBack(){
        queryText.addOnPropertyChangedCallback(object : OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                loadSearchHintWord()
            }
        })
    }

    fun loadSearchHintWord(){
        var firstVM: SearchHintHeadCellVM? = null
        if(hintWordVMList.value!!.size==0){
            firstVM = SearchHintHeadCellVM()
        }else firstVM = hintWordVMList.value!![0] as SearchHintHeadCellVM
        var searchHintVMList:MutableList<BaseViewModel> = mutableListOf()
        queryText.get()?.let {
            if(it.length==0){
                hintRecyclerViewVisible.set(View.INVISIBLE)
                return@let
            }
            firstVM.hintWord = "搜索“${it}”"
            searchHintVMList.add(firstVM)
            searchHintVMList.addAll(obtainNormalViewModel(it))
            hintRecyclerViewVisible.set(View.VISIBLE)
            Log.i(TAG,"text change")
        }
        hintWordVMList.value = searchHintVMList
    }

    fun obtainNormalViewModel(word:String):MutableList<SearchHintNormalCellVM>{
        var list = mutableListOf<SearchHintNormalCellVM>()
        var vmItem = SearchHintNormalCellVM()
        vmItem.hintWord = word
        list.add(vmItem)
        return list
    }

    fun invisibleHintList(){
        hintRecyclerViewVisible.set(View.INVISIBLE)
    }

    fun popBack(view:View){
        view.findNavController().popBackStack()
    }

    override fun bindData(viewDataBinding: ViewDataBinding, lifecycleOwner: LifecycleOwner) {
        //放在了SearchMainPageFragment
    }
}