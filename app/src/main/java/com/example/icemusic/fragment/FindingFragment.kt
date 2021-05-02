package com.example.icemusic.fragment

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.icemusic.R
import com.example.icemusic.adapter.recyclerAdapter.FindingPageRecyclerViewAdapter
import com.example.icemusic.adapter.recyclerAdapter.base.BaseRecyclerViewAdapter
import com.example.icemusic.dataViewModelMap.DataViewModelMapperRegistry
import com.example.icemusic.databinding.FindingPageBinding
import com.example.icemusic.netWork.ObtainFindingPageDataWorker
import com.example.icemusic.viewModel.BaseViewModel
import com.example.icemusic.viewModel.findPageVM.FindingPageViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * 发现页面包含一个RecycleView，通过给RecyclerView的Adapter发送不同的ViewModel来加载不同的视图
 * @property TAG String
 * @property fragmentBinding FindingPageBinding
 * @property findingPageViewModel FindingPageViewModel
 */
class FindingFragment() : Fragment() {

    val TAG = "FindingFragment"

    lateinit var fragmentBinding: FindingPageBinding

    val findingPageViewModel: FindingPageViewModel by lazy{
        var findingPageViewModel = createViewModel()
        //初始化加载数据
        findingPageViewModel.loadData()
        findingPageViewModel
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.finding_page,container,false)
        fragmentBinding = FindingPageBinding.bind(rootView)
        return rootView
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.takeIf { it.containsKey(COLOR_OBJECT) }?.apply {
            var bg = ColorDrawable(getInt(COLOR_OBJECT))
            fragmentBinding.findingImageView.background = bg
            fragmentBinding.imageUrl = "https://y.gtimg.cn/music/photo_new/T002R90x90M000004fsy4H2TEPPK_1.jpg?max_age=2592000"
        }
        var findingPageRecyclerViewAdapter = BaseRecyclerViewAdapter(viewLifecycleOwner,
            this,
            BaseRecyclerViewAdapter.NORMAL_BASE_VIEW_MODEL_CREATOR)
        fragmentBinding.findingPageRecyclerView.adapter = findingPageRecyclerViewAdapter
        val linearLayoutManager = LinearLayoutManager(this.context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        fragmentBinding.findingPageRecyclerView.layoutManager = linearLayoutManager

        findingPageViewModel.dataList.observe(viewLifecycleOwner, Observer {
            findingPageRecyclerViewAdapter.dataList = it
            findingPageRecyclerViewAdapter.notifyDataSetChanged()
        })
//        loadPageViewMode(this)
        addSearchView(fragmentBinding.findingHeadTab.headMediumParent)

        fragmentBinding.findingHeadTab.headMediumParent.setOnClickListener {
            val action = MainPageFragmentDirections.actionMainPageFragmentToNavSearchMusic()
            it.findNavController().navigate(action)
        }
    }
    fun createViewModel(): FindingPageViewModel {
        var viewModelProvider = ViewModelProvider(this)
        var findingViewModel = viewModelProvider.get(FindingPageViewModel::class.java)
//        findingViewModel.viewModeList.value?.add(viewModelProvider.get(AdvertisementViewModel::class.java))
        return findingViewModel
    }

    /**
     * 废弃
     * @param owner ViewModelStoreOwner
     * @param findingPageViewModel FindingPageViewModel
     */
    fun loadPageViewMode(owner: ViewModelStoreOwner,findingPageViewModel: FindingPageViewModel){
        GlobalScope.launch {
            var worker = ObtainFindingPageDataWorker()
            var findingPageData = worker.obtainFindingPageData()
            //错误，不能在子线程中使用owner，其可能为null
//            launch (Dispatchers.Main){
//                var viewModelList = worker.createViewModelListByData(findingPageData,owner)
//                Log.i(TAG,""+viewModelList.size)
//                findingPageViewModel.viewModeList.value = viewModelList
//            }
        }
    }

    fun addSearchView(mediumLayout:LinearLayout){
        var layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT)
        var layoutInflater = LayoutInflater.from(this.requireContext())
        var searchTab = layoutInflater.inflate(R.layout.search_tab,mediumLayout,false)
        searchTab.layoutParams = layoutParams
        mediumLayout.addView(searchTab)
    }

    companion object{
        val COLOR_OBJECT = "object"
    }
}