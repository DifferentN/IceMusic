package com.example.icemusic.viewModel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.icemusic.R
import com.example.icemusic.data.TabCellData
import com.google.android.material.tabs.TabLayout

class TabLayoutViewModel: BaseViewModel(){
    val tabCellDataList = mutableListOf<TabCellData>()

    init{
        tabCellDataList.add(TabCellData("发现",state = ObservableBoolean(true), selectColor = 0xFF000000.toInt()))
        tabCellDataList.add(TabCellData("播客", selectColor = 0xFF888888.toInt()))
        tabCellDataList.add(TabCellData("我的", selectColor = 0xFF00FF00.toInt()))
        tabCellDataList.add(TabCellData("K歌", selectColor = 0xFFCCCCCC.toInt()))
        tabCellDataList.add(TabCellData("云村", selectColor = 0xFF0000FF.toInt()))
    }

    override fun bindData(viewDataBinding: ViewDataBinding, lifecycleOwner: LifecycleOwner) {

    }
    fun tabSelected(tab:TabLayout.Tab?){
        tab?.position?.let {
            tabCellDataList[it].state.set(true)
        }
    }
    fun tabUnSelected(tab:TabLayout.Tab?){
        tab?.position?.let {
            tabCellDataList[it].state.set(false)
        }
    }
}