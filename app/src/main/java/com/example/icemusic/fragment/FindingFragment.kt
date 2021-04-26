package com.example.icemusic.fragment

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.icemusic.R
import com.example.icemusic.adapter.recyclerAdapter.FindingPageRecyclerViewAdapter
import com.example.icemusic.databinding.FindingPageBinding
import com.example.icemusic.netWork.ObtainFindingPageDataWorker
import com.example.icemusic.viewModel.findPageVM.FindingPageViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FindingFragment() : Fragment() {

    val TAG = "FindingFragment"

    lateinit var fragmentBinding: FindingPageBinding

    val findingPageViewModel: FindingPageViewModel by lazy{
        var findingPageViewModel = createViewModel()
        loadPageViewMode(this,findingPageViewModel)
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
        var findingPageRecyclerViewAdapter = FindingPageRecyclerViewAdapter(this)
        fragmentBinding.findingPageRecyclerView.adapter = findingPageRecyclerViewAdapter
        val linearLayoutManager = LinearLayoutManager(this.context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        fragmentBinding.findingPageRecyclerView.layoutManager = linearLayoutManager

        findingPageViewModel.viewModeList.observe(viewLifecycleOwner, Observer {
            findingPageRecyclerViewAdapter.viewModelList = it
            findingPageRecyclerViewAdapter.notifyDataSetChanged()
            Log.i(TAG,"data change")
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

    fun loadPageViewMode(owner: ViewModelStoreOwner,findingPageViewModel: FindingPageViewModel){
        GlobalScope.launch {
            var worker = ObtainFindingPageDataWorker()
            var findingPageData = worker.obtainFindingPageData()
            launch (Dispatchers.Main){
                var viewModelList = worker.createViewModelListByData(findingPageData,owner)
                Log.i(TAG,""+viewModelList.size)
                findingPageViewModel.viewModeList.value = viewModelList
            }
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