package com.example.icemusic.viewModel.findPageVM

import androidx.databinding.ObservableBoolean
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.example.icemusic.R
import com.example.icemusic.data.TabCellData
import com.example.icemusic.viewModel.BaseViewModel
import com.google.android.material.tabs.TabLayout

class TabLayoutViewModel: BaseViewModel(){
    val tabCellDataList = mutableListOf<TabCellData>()

    init{
        tabCellDataList.add(TabCellData("发现",state = ObservableBoolean(true),
           selectImage = R.drawable.finding_select,unSelectImage = R.drawable.finding_unselect))
        tabCellDataList.add(TabCellData("播客", selectImage = R.drawable.boke_select,
            unSelectImage = R.drawable.boke_unselect
        ))
        tabCellDataList.add(TabCellData("我的", selectImage = R.drawable.user_select,
            unSelectImage = R.drawable.user_unselect))
        tabCellDataList.add(TabCellData("K歌", selectImage = R.drawable.music_select,
            unSelectImage = R.drawable.music_unselect))
        tabCellDataList.add(TabCellData("云村", selectImage = R.drawable.user_group_select,
            unSelectImage = R.drawable.user_group_unselect))
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