package com.example.icemusic.fragment.searchFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withCreated
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.icemusic.R
import com.example.icemusic.adapter.recyclerAdapter.base.BaseRecyclerViewAdapter
import com.example.icemusic.data.eventBus.SearchChangeEvent
import com.example.icemusic.data.eventBus.SearchHintEvent
import com.example.icemusic.databinding.SearchMainPageBinding
import com.example.icemusic.db.MusicDatabaseInstance
import com.example.icemusic.db.entity.SearchHistorySong
import com.example.icemusic.viewModel.searchPageVM.SearchPageViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jsoup.Connection

class SearchMainPageFragment:Fragment() {

    var TAG = "SearchMainPageFragment"

    lateinit var binding : SearchMainPageBinding

    lateinit var searchPageViewModel:SearchPageViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SearchMainPageBinding.inflate(inflater,container,false)
        EventBus.getDefault().register(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchPageViewModel = ViewModelProvider(this).get(SearchPageViewModel::class.java)
        binding.setVariable(BR.obj,searchPageViewModel)

        //监听搜索词的变化
        searchPageViewModel.setCallBack()

        //设置提示列表RecyclerView
        var hintRecyclerView = binding.searchPageHintListRecyclerView
        var adapter = BaseRecyclerViewAdapter(viewLifecycleOwner,
            this,
            BaseRecyclerViewAdapter.FAKE_BASE_VIEW_MODEL_CREATOR)

        var linearLayoutManager = LinearLayoutManager(this.requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL

        hintRecyclerView.layoutManager = linearLayoutManager

        hintRecyclerView.adapter = adapter

        searchPageViewModel.hintWordDataList.observe(viewLifecycleOwner, Observer {
            adapter.dataList = it
            adapter.notifyDataSetChanged()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

    /**
     * 接收来自SearchHintNormalCellVM的事件
     * @param searchHintEvent SearchHintEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onHandleSearchHintEvent(searchHintEvent:SearchHintEvent){
        searchPageViewModel.invisibleHintList()
        var searchWord = reformSearchWord(searchHintEvent.searchWord)
        var curFragmentLabel:String = binding.searchMusicPageFragment.findNavController().currentDestination!!.label.toString()
        if(curFragmentLabel==resources.getString(R.string.nav_label_search_exhibition_fragment)){
            var action = SearchExhibitionFragmentDirections.actionSearchExhibitionFragmentToSearchResultFragment(searchWord)
            binding.searchMusicPageFragment.findNavController().navigate(action)
        }else{
            //处理者为SearchResultFragment
            EventBus.getDefault().post(SearchChangeEvent(searchWord))
        }

        //添加搜索记录到数据库
        lifecycleScope.launch {
            withContext(Dispatchers.IO){
                var db = MusicDatabaseInstance.getInstance(requireContext().applicationContext)
                var searchSong = SearchHistorySong(searchWord,-1)
                db.searchHistorySongDao().insertSearchHistorySong(searchSong)
                Log.i(TAG,"add search word")
            }
        }

    }

    private fun reformSearchWord(searchWord:String):String{
        var start = searchWord.indexOf("“")
        if(start<0){
            return searchWord
        }else {
            var end = searchWord.indexOf("”",start)
            return searchWord.substring(start+1,end)
        }
    }

}