package com.example.icemusic.dataViewModelMap

import android.util.Log
import com.example.icemusic.data.*
import com.example.icemusic.data.searchData.SearchSongHintData
import com.example.icemusic.data.searchData.searchResultData.SearchSingleSongData
import com.example.icemusic.db.entity.SearchHistorySong
import com.example.icemusic.viewModel.BackupViewModel
import com.example.icemusic.viewModel.LoadPageErrorVM
import com.example.icemusic.viewModel.findPageVM.*
import com.example.icemusic.viewModel.searchPageVM.SearchHintNormalCellVM
import com.example.icemusic.viewModel.searchPageVM.SearchHistorySongCellVM
import com.example.icemusic.viewModel.searchPageVM.SearchHistorySongListVM
import com.example.icemusic.viewModel.searchPageVM.SingleSongCellVM

class DataViewModelMapperRegistry {
    companion object{
        val TAG = "DataViewModelMapper"
        //单个Data对应的ViewModel
        private var dataViewModelMap = mapOf<String,String>(
            FindingPageData::class.java.canonicalName to FindingPageViewModel::class.java.canonicalName,
            TypeEntryCellData::class.java.canonicalName to TypeEntryCellViewModel::class.java.canonicalName,
            //搜索历史ViewModel
            SearchHistorySong::class.java.canonicalName to SearchHistorySongCellVM::class.java.canonicalName,
            //个人定制视图ViewModel
            PersonalSongCellData::class.java.canonicalName to PersonalSongCellViewModel::class.java.canonicalName,
            PersonalSongTripleCellData::class.java.canonicalName to PersonalSongCellTripleViewModel::class.java.canonicalName,
            //推荐歌曲视图ViewModel
            RecommendSongCellData::class.java.canonicalName to RecommendSongCellViewModel::class.java.canonicalName,
            //搜索提示词的ViewModel
            SearchSongHintData::class.java.canonicalName to SearchHintNormalCellVM::class.java.canonicalName,
            //搜索单曲的ViewModel
            SearchSingleSongData::class.java.canonicalName to SingleSongCellVM::class.java.canonicalName,
            //加载失败ViewModel
            LoadPageErrorData::class.java.canonicalName to LoadPageErrorVM::class.java.canonicalName

        )
        //data链表对应的ViewModel ??是否以后再修改
        private var listDataViewModelMap = mapOf<String,String>(
            AdvertData::class.java.canonicalName to AdvertisementViewModel::class.java.canonicalName,
            TypeEntryCellData::class.java.canonicalName to TypeEntryListViewModel::class.java.canonicalName,

            PersonalSongCellData::class.java.canonicalName to PersonalSongListViewModel::class.java.canonicalName,
            SearchHistorySong::class.java.canonicalName to SearchHistorySongListVM::class.java.canonicalName,
            //个人定制视图ViewModel
            PersonalSongTripleCellData::class.java.canonicalName to PersonalSongListViewModel::class.java.canonicalName,
            //推荐歌曲视图ViewModel
            RecommendSongCellData::class.java.canonicalName to RecommendSongListViewModel::class.java.canonicalName
        )

        /**
         * 获取data对应的ViewModel
         * @param value Any
         * @return String
         */
        fun getViewModelClassNameForData(value:Any):String{
            var dataClazz = value::class.java
            var viewModelName:String? = null
            if(List::class.java.isAssignableFrom(dataClazz)){
                var listData:List<*> = value as List<*>
                if(!listData.isEmpty()){
                    viewModelName = getViewModelClassNameFormListData(listData[0] as Any)
                }
            }else{
                viewModelName = dataViewModelMap[dataClazz.canonicalName]
            }

            //在未找到与Data对应的ViewModel时，兜底的ViewModel
            viewModelName = viewModelName?:BackupViewModel::class.java.canonicalName

            return viewModelName!!
        }

        /**
         * 获取data链表对应的ViewModel
         * @param value Any
         * @return String?
         */
        private fun getViewModelClassNameFormListData(value: Any):String?{
            var dataClazz = value::class.java
            return listDataViewModelMap[dataClazz.canonicalName]
        }
    }

}