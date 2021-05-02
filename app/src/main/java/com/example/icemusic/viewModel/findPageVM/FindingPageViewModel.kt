package com.example.icemusic.viewModel.findPageVM

import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.*
import com.example.icemusic.R
import com.example.icemusic.netWork.ObtainFindingPageDataWorker
import com.example.icemusic.viewModel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FindingPageViewModel: BaseViewModel() {
    val TAG = "FindingPageViewModel"
//    val viewModeList: MutableLiveData<MutableList<BaseViewModel>> by lazy {
//        MutableLiveData<MutableList<BaseViewModel>>().also {
//            it.value= mutableListOf()
//        }
//    }

    val dataList:MutableLiveData<MutableList<Any>> by lazy {
        MutableLiveData<MutableList<Any>>().also {
            it.value = mutableListOf()
        }
    }

    init {
        layoutId = R.layout.finding_page
    }

    override fun bindData(viewDataBinding: ViewDataBinding, lifecycleOwner: LifecycleOwner,viewModelStoreOwner: ViewModelStoreOwner) {
        //放到了FindingFragment中
    }

    override fun loadData() {
        loadFindingPageData()
    }

    private fun loadFindingPageData(){
        viewModelScope.launch {
            var findingPageDataList = loadFindingPageDataFromNet()
            Log.i(TAG,"load finding page data finish")
            dataList.value = findingPageDataList
        }
    }

    private suspend fun loadFindingPageDataFromNet():MutableList<Any> = withContext(Dispatchers.IO){
        var res = mutableListOf<Any>()

        var worker = ObtainFindingPageDataWorker()
        var findingPageData = worker.obtainFindingPageData()

        findingPageData.adDataList?.let {
            if(!it.isEmpty()){
                res.add(it)
            }
        }

        findingPageData.typeEntryCellDataList?.let {
            if(!it.isEmpty()){
                res.add(it)
            }

        }

        findingPageData.recommendSongCellList?.let {
            if(!it.isEmpty()){
                res.add(it)
            }

        }

        findingPageData.personalSongCellList?.let {
            if(!it.isEmpty()){
                res.add(it)
            }

        }
        return@withContext res
    }
}