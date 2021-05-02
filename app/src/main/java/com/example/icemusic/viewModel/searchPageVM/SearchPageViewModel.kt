package com.example.icemusic.viewModel.searchPageVM


import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.*
import androidx.databinding.Observable.OnPropertyChangedCallback
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.example.icemusic.R
import com.example.icemusic.data.searchData.SearchSongHintData
import com.example.icemusic.viewModel.BaseViewModel
import kotlinx.coroutines.launch

class SearchPageViewModel: BaseViewModel(){

    val TAG = "SearchPageViewModel"

    val queryText:ObservableField<String> = ObservableField<String>("")


//    val hintWordVMList:MutableLiveData<MutableList<BaseViewModel>> by lazy {
//        MutableLiveData<MutableList<BaseViewModel>>().also {
//            it.value = mutableListOf()
//        }
//    }

    val hintWordDataList:MutableLiveData<MutableList<SearchSongHintData>> by lazy {
        MutableLiveData<MutableList<SearchSongHintData>>().also {
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
        var searchWord = queryText.get()?:""
        var dataList = mutableListOf<SearchSongHintData>()
        viewModelScope.launch {
            var firstHintData = SearchSongHintData("搜索“${searchWord}”")
            var hintData = SearchSongHintData(searchWord)
            dataList.add(firstHintData)
            dataList.add(hintData)
            hintWordDataList.value = dataList

            //显示提示词
            hintRecyclerViewVisible.set(View.VISIBLE)
        }
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

    fun onEditTextFocusChangeListener(view: View?,hashFocus:Boolean){
        view?.let {
            if(!hashFocus){
                var manager: InputMethodManager? = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                manager?.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
    }

    override fun bindData(viewDataBinding: ViewDataBinding, lifecycleOwner: LifecycleOwner,viewModelStoreOwner: ViewModelStoreOwner) {
        //放在了SearchMainPageFragment
    }


}