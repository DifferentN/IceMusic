package com.example.icemusic.viewModel

import android.graphics.Color
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.icemusic.BR
import com.example.icemusic.R
import com.example.icemusic.adapter.recyclerAdapter.AdPageAdapter
import com.example.icemusic.adapter.recyclerAdapter.AdIndicatorAdapter
import com.example.icemusic.data.AdIndicatorData
import com.example.icemusic.data.AdvertData
import com.example.icemusic.databinding.AdPageListBinding
import kotlinx.coroutines.*

class AdvertisementViewModel : BaseViewModel() {
    val TAG = "AdvertisementViewModel"

    //广告数据
    val adDataList: MutableLiveData<MutableList<AdvertData>> by lazy {
        MutableLiveData<MutableList<AdvertData>>().also {
            it.value = loadAdDataList()
        }
    }

    //表示广告是否被选中的状态
    var adIndicatorDataList: MutableList<AdIndicatorData> = mutableListOf()

    //上次选中的页面索引
    var lastSelectedPagePosition = -1
    init {
        layoutId = R.layout.ad_page_list
    }

    private fun loadAdDataList(): MutableList<AdvertData> {
        var datas = mutableListOf<AdvertData>()
        datas.add(AdvertData("ad1", null, color = Color.BLUE.toInt()))
        datas.add(AdvertData("ad2", null, color = Color.GRAY.toInt()))
//        datas.add(AdvertData("ad3", null,color = Color.BLUE.toInt()))
//        datas.add(AdvertData("ad4", null))

        adIndicatorDataList.add(AdIndicatorData())
        adIndicatorDataList.add(AdIndicatorData())
//        adIndicatorDataList.add(AdIndicatorData())
//        adIndicatorDataList.add(AdIndicatorData())
        return datas
    }

    fun replaceAdvertData(newadDataList:MutableList<AdvertData>){
        //更新广告指示器的数量
        adDataList.value?.let {
            if(newadDataList.size - it.size>0){
                var num = newadDataList.size - it.size
                for(i in 1..num){
                    adIndicatorDataList.add(AdIndicatorData())
                }
            }else if(newadDataList.size - it.size<0){
                var num = it.size - newadDataList.size
                for(i in 1..num){
                    adIndicatorDataList.removeAt(0)
                }
            }
        }
        //替换广告
        adDataList.value = newadDataList
    }

    override fun bindData(viewDataBinding: ViewDataBinding, lifecycleOwner: LifecycleOwner) {
        viewDataBinding.takeIf { it is AdPageListBinding }?.let {
            var adPageListBinding = it as AdPageListBinding
            if (lifecycleOwner !is Fragment) {
                return
            }
            var fragment = lifecycleOwner as Fragment
            //设置广告viewpager的适配器
            if(adPageListBinding.adViewPager2.adapter==null){
                var adPageAdapter = AdPageAdapter()
                adDataList.observe(fragment, Observer {
                    adPageAdapter.adDataList = it
                    adPageAdapter.notifyDataSetChanged()
                })
                adPageListBinding.adViewPager2.adapter = adPageAdapter

                //循环展示广告
                loopShowAd(adPageListBinding.adViewPager2)
            }

            if(adPageListBinding.adIndicatorGroup.adapter == null){
                var adIndicatorAdapter = AdIndicatorAdapter()
                adIndicatorAdapter.adIndicatorDataList = adIndicatorDataList
                adPageListBinding.adIndicatorGroup.adapter = adIndicatorAdapter
                var linearLayoutManager = LinearLayoutManager(fragment.requireContext())
                linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                adPageListBinding.adIndicatorGroup.layoutManager = linearLayoutManager
            }

            adPageListBinding.setVariable(BR.obj,this)
            adPageListBinding.executePendingBindings()

            adPageListBinding.adViewPager2.let {
                it.currentItem = it.adapter!!.itemCount/2
            }
        }
    }


    fun loopShowAd(viewPager2: ViewPager2) {
        GlobalScope.launch(Dispatchers.Default) {
            while (true) {
                delay(3000)
                GlobalScope.launch(Dispatchers.Main) {
                    if (viewPager2.currentItem >= (viewPager2.adapter?.itemCount ?: 0)-1) {
                        viewPager2.currentItem = 0
                    } else viewPager2.currentItem = viewPager2.currentItem + 1
                }
            }

        }
    }

    fun onPageSelected(position:Int){
        var curPos = position%adIndicatorDataList.size
        if(lastSelectedPagePosition>=0){
            adIndicatorDataList[lastSelectedPagePosition].state.set(false)
        }
        adIndicatorDataList[curPos].state.set(true)
        lastSelectedPagePosition = curPos
    }

}