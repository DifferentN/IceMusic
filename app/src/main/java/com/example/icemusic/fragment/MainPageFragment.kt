package com.example.icemusic.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.icemusic.adapter.fragmentAdapter.FindingFragmentAdapter
import com.example.icemusic.databinding.MainPageBinding
import com.example.icemusic.databinding.TabCellViewBinding
import com.example.icemusic.viewModel.PlaySongBottomTabViewModel
import com.example.icemusic.viewModel.findPageVM.TabLayoutViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * APP的主页，包含一个Viewpager实现不同页面(fragment)的切换
 * @property tabLayoutViewModel TabLayoutViewModel
 * @property findingFragmentAdapter FindingFragmentAdapter
 * @property mainPageBinding MainPageBinding
 * @property playSongBottomViewModel PlaySongBottomTabViewModel
 */
class MainPageFragment :Fragment(){

    private lateinit var tabLayoutViewModel: TabLayoutViewModel
    private lateinit var findingFragmentAdapter:FindingFragmentAdapter
    private lateinit var mainPageBinding:MainPageBinding
    private lateinit var playSongBottomViewModel:PlaySongBottomTabViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mainPageBinding = MainPageBinding.inflate(inflater,container,false)

        return mainPageBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //create viewModel
        createViewModel()
        //create viewPager2 adapter
        createAdapter()

        mainPageBinding.viewPager.adapter = findingFragmentAdapter

        //管理TabLayout和viewpager2
        TabLayoutMediator(mainPageBinding.tabLayout,mainPageBinding.viewPager){ tab: TabLayout.Tab, position: Int ->
            var tabCellViewBinding = TabCellViewBinding.inflate(layoutInflater)
            tabCellViewBinding.cellData = tabLayoutViewModel.tabCellDataList[position]
            tab.customView = tabCellViewBinding.root
        }.attach()

        mainPageBinding.tabLayoutViewModel = tabLayoutViewModel

        mainPageBinding.playSongBottomVM = playSongBottomViewModel
        mainPageBinding.executePendingBindings()
    }

    private fun createViewModel(){
        tabLayoutViewModel = ViewModelProvider(this).get(TabLayoutViewModel::class.java)

        playSongBottomViewModel = ViewModelProvider(requireActivity()).get(PlaySongBottomTabViewModel::class.java)
    }
    private fun createAdapter(){
        var colorList = mutableListOf<Int>()
        colorList.add(0xFF000000.toInt())
        colorList.add(0xFF888888.toInt())
        colorList.add(0xFF00FF00.toInt())
        colorList.add(0xFFCCCCCC.toInt())
        colorList.add(0xFF0000FF.toInt())

        findingFragmentAdapter = FindingFragmentAdapter(colorList,this)
    }
}