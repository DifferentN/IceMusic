package com.example.icemusic

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.icemusic.adapter.fragmentAdapter.FindingFragmentAdapter
import com.example.icemusic.databinding.ActivityMainBinding
import com.example.icemusic.databinding.TabCellViewBinding
import com.example.icemusic.viewModel.TabLayoutViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : FragmentActivity() {
    private val TAG = "MainActivity";
    private lateinit var tabLayoutViewModel:TabLayoutViewModel
    private lateinit var findingFragmentAdapter:FindingFragmentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //create viewModel
        createViewModel()
        //create viewPager2 adapter
        createAdapter()

        binding.viewPager.adapter = findingFragmentAdapter

        //管理TabLayout和viewpager2
        TabLayoutMediator(binding.tabLayout,binding.viewPager){ tab: TabLayout.Tab, position: Int ->
            var tabCellViewBinding = TabCellViewBinding.inflate(layoutInflater)
            tabCellViewBinding.cellData = tabLayoutViewModel.tabCellDataList[position]
            tab.customView = tabCellViewBinding.root
        }.attach()

        binding.tabLayoutViewModel = tabLayoutViewModel
    }
    private fun createViewModel(){
        tabLayoutViewModel = ViewModelProvider(this).get(TabLayoutViewModel::class.java)
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