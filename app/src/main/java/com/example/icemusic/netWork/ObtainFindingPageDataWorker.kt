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

    fun obtainFindingPageData(): FindingPageData {
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

    fun obtainRecommendSongData():MutableList<RecommendSongCellData>{
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

    fun obtainPersonalSongData():MutableList<PersonalSongTripleCellData>{
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

        var tripleCellDataList = mutableListOf<PersonalSongTripleCellData>()
        for(i in 0 until dataList.size step 3){
            var tripleList = mutableListOf<PersonalSongCellData>()
            for(j in 0 until 3){
                tripleList.add(dataList[i+j])
            }
            tripleCellDataList.add(PersonalSongTripleCellData(tripleList))
        }
        return tripleCellDataList
    }

}