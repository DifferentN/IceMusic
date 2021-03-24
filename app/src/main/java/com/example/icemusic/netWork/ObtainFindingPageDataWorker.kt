package com.example.icemusic.netWork

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.icemusic.data.*
import com.example.icemusic.viewModel.*
import com.example.icemusic.viewModel.findPageVM.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class ObtainFindingPageDataWorker {
    val TAG = "ObtainFindingPageData"

    suspend fun obtainFindingPageData(): FindingPageData {
        var doc:Document = Jsoup.connect("https://y.qq.com/").get()
        var elements = doc.body().getElementsByClass("event_list__pic")
        var adDataList = mutableListOf<AdvertData>()
        for (i in 1..elements.size){
            adDataList.add(AdvertData("",elements[i-1].attr("src")))
        }
        Log.i(TAG,"obtain typeEntryData")

        var typeEntryCellDataList = obtainTypeEntryData()

        var recommendSongCellDataList = obtainRecommendSongData()

        var personalSongList = obtainPersonalSongData()
        return FindingPageData(adDataList,typeEntryCellDataList,recommendSongCellDataList,personalSongList)
    }

    fun obtainTypeEntryData():MutableList<TypeEntryCellData>{
        var doc:Document = Jsoup.connect("https://www.kugou.com/").get()
        var radioLogos = doc.body().getElementsByClass("radioLogo")
        var radioKinds = doc.body().getElementsByClass("radioKind")
        var typeEntryCellDataList = mutableListOf<TypeEntryCellData>()
//        print(radioLogos)
        for(i in 1..radioLogos.size){
            var radioLogo = radioLogos[i-1]
            var image = radioLogo.child(0).attr("_src")
            var text = radioKinds[i-1].text()
            Log.i(TAG,image+"\n"+text)
            typeEntryCellDataList.add(TypeEntryCellData(image,text))
        }
        return typeEntryCellDataList
    }

    suspend fun obtainRecommendSongData():MutableList<RecommendSongCellData>{
        var doc:Document = Jsoup.connect("https://www.kugou.com").get()
        var recommendSongItemsul= doc.body().getElementsByClass("tabContent")[0].child(0)
//        print(recommendSongItemsul.toString())
        var recommendSongCellDataList = mutableListOf<RecommendSongCellData>()
        for(i in 0 until recommendSongItemsul.childrenSize()){
            var item = recommendSongItemsul.child(i)

            var imageUrl = item.child(0).child(1).attr("_src")
            var recommendSongTitle = item.child(1).attr("title")
            var playNum = item.getElementsByClass("number")[0].text()
            var recommendSongCellData = RecommendSongCellData(playNum,imageUrl,recommendSongTitle)
            recommendSongCellDataList.add(recommendSongCellData)
            Log.i(TAG,"recommend song: "+imageUrl)
//            print(imageUrl+"\n"+recommendSongTitle+"\n"+playNum)
            if(recommendSongTitle.length==0){
                break
            }
        }
        return recommendSongCellDataList
    }

    suspend fun obtainPersonalSongData():MutableList<PersonalSongCellData>{
        var doc:Document = Jsoup.connect("https://y.qq.com/?ADTAG=myqq#type=index").get()
        var songItemList = doc.getElementsByClass("songlist__item_box")
        var dataList = mutableListOf<PersonalSongCellData>()
        for(i in 0 until songItemList.size){
            var songItem = songItemList[i]
//            print(songItem.toString())
            var imageUrl = "https:"+songItem.getElementsByClass("songlist__pic")[0].attr("src")
            var title = songItem.getElementsByClass("songlist__song")[0].child(0).text()
            var author = songItem.getElementsByClass("c_tx_thin singer_name")[0].attr("title")
            var time = songItem.getElementsByClass("songlist__time c_tx_thin")[0].text()
            var persongSongData = PersonalSongCellData(imageUrl,title, author, time)
//            print(imageUrl+"\n"+title+"\n"+author+"\n"+time)
            dataList.add(persongSongData)
        }
        return dataList
    }

    suspend fun createViewModelList(owner: ViewModelStoreOwner): MutableList<BaseViewModel> {
        var findingPageData = obtainFindingPageData()

        var tempViewModelList = createViewModelListByData(findingPageData, owner)
        return tempViewModelList
    }

    suspend fun createViewModelListByData(findingPageData: FindingPageData, owner: ViewModelStoreOwner): MutableList<BaseViewModel> {
        var viewModelList = mutableListOf<BaseViewModel>()
        var viewModelProvider = ViewModelProvider(owner)
        findingPageData.adDataList?.let {
            var advertisementViewModel = viewModelProvider.get(AdvertisementViewModel::class.java)
            advertisementViewModel.replaceAdvertData(it)
            viewModelList.add(advertisementViewModel)
        }
        //添加类型链表ViewModels
        findingPageData.typeEntryCellDataList?.let {
            var typeEntryListViewModel = viewModelProvider.get(TypeEntryListViewModel::class.java)
            var typeEntryCellViewModelList = mutableListOf<TypeEntryCellViewModel>()
            for ( typeEntryCellData in it){
                var typeEntryCellViewModel = TypeEntryCellViewModel()
                typeEntryCellViewModel.typeEntryCellData = typeEntryCellData
                typeEntryCellViewModelList.add(typeEntryCellViewModel)
            }
            typeEntryListViewModel.typeEntryCellList.value = typeEntryCellViewModelList

            viewModelList.add(typeEntryListViewModel)
        }

        //添加推荐歌单ViewModels
        findingPageData.recommendSongCellList?.let {
            var recommendSongListViewModel = viewModelProvider.get(RecommendSongListViewModel::class.java)
            var recommendSongCellListVM = mutableListOf<RecommendSongCellViewModel>()
            for(recommendSongCellData in it){
                var vm = RecommendSongCellViewModel()
                vm.recommendSongCellData = recommendSongCellData
                recommendSongCellListVM.add(vm)
            }
            recommendSongListViewModel.recommendSongCellVMList.value = recommendSongCellListVM
            Log.i(TAG,"recommend song size: "+it.size)
            viewModelList.add(recommendSongListViewModel)
        }

        //添加私人定制
        findingPageData.personalSongCellList?.let {
            var personalSongListViewModel = viewModelProvider.get(PersonalSongListViewModel::class.java)
            var tripleViewModelList = mutableListOf<PersonalSongCellTripleViewModel>()
            for(i in 0 until it.size step 3){
                var tripleVM = PersonalSongCellTripleViewModel()
                var personalSongDataList = tripleVM.personalSongCellDataList.value
                personalSongDataList?.clear()
                for(j in 0 until 3){
                    personalSongDataList?.add(it[i+j])
                }
                tripleVM.personalSongCellDataList.value = personalSongDataList
                tripleViewModelList.add(tripleVM)
            }
            personalSongListViewModel.personalSongTripleList.value = tripleViewModelList
            viewModelList.add(personalSongListViewModel)
        }
        return viewModelList
    }

}