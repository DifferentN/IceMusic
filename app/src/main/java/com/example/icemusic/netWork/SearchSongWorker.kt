package com.example.icemusic.netWork

import android.util.Log
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.example.icemusic.data.LoadPageErrorData
import com.example.icemusic.data.searchData.searchResultData.SearchResultTabData
import com.example.icemusic.data.searchData.searchResultData.SearchResultTabType
import com.example.icemusic.data.searchData.searchResultData.SearchSingleSongData
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.BufferedReader

class SearchSongWorker {
    val TAG = "SearchSongWorker"

    private val CODE = "code"
    private val DATA = "data"
    private val RESULT = "result"
    private val SONGS = "songs"
    private val SONG_ID = "id"
    private val SONG_NAME = "name"
    private val ARTISTS = "artists"
    private val ARTIST_NAME = "name"
    private var ARTIST_IMAGE_URL = "img1v1Url"

    fun searchBySearchTabDataAndSongName(tabData: SearchResultTabData,songName:String):MutableList<out Any>{
        var res:MutableList<out Any>? = when(tabData.tabType){
            SearchResultTabType.SINGLE_SONG_TYPE ->{
                searchSongData(songName)
            }
            else->{
                //没有找到对应类型，先返回单曲数据
                searchSongData(songName)
            }
        }


        if(res==null){
            //未加载到数据，填充错误提示数据
            res = obtainLoadPageErrorData()
        }

        return res
    }

    /**
     * 返回一个数据加载失败的提示数据
     * @return MutableList<LoadPageErrorData>
     */
    fun obtainLoadPageErrorData():MutableList<LoadPageErrorData>{
        var list = mutableListOf<LoadPageErrorData>()
        list.add(LoadPageErrorData())
        return list
    }

    /**
     * 在网络请求失败和服务端未返回正确数据时，返回null
     * @param searchWord String
     * @return MutableList<SearchSingleSongData>?
     */
    fun searchSongData(searchWord:String):MutableList<SearchSingleSongData>?{
        var client = OkHttpClient()
        var request = Request.Builder()
            .url("https://v1.alapi.cn/api/music/search?keyword=${searchWord}&limit=30")
            .get()
            .build()
        var call = client.newCall(request)
        var resonse = call.execute()
        var resJSON:JSONObject? = null
        var res = if(resonse.code==200){
            //成功获取数据
            resonse.body?.let {
                var stream = it.charStream()
                var bufferReader:BufferedReader = BufferedReader(stream)
                var resStr = bufferReader.lineSequence().joinToString()
                resJSON = JSON.parseObject(resStr)
//            print(resJSON!!.toJSONString())
            }
            extractSingleSongDataList(resJSON).let {
                if(it.isEmpty()){
                    //网络请求成功，但是服务端未返回正确数据
                    null
                }else it
            }
        }else{
            //网络请求失败
            null
        }

        return res
    }
    private fun extractSingleSongDataList(jsonObject: JSONObject?):MutableList<SearchSingleSongData>{
        var dataList = mutableListOf<SearchSingleSongData>()
        jsonObject?.let {
            if(it[CODE]!=200){
                return dataList
            }
            var songJsonArray = it.getJSONObject(DATA).getJSONArray(SONGS)
            var size = Math.max(0,songJsonArray.size)
            for(i in 0 until size){
                var songJson = songJsonArray.getJSONObject(i)
                var aritsts = songJson.getJSONArray(ARTISTS)
                var artistName = aritsts.getJSONObject(0).getString(ARTIST_NAME)
                var imageUrl = aritsts.getJSONObject(0).getString(ARTIST_IMAGE_URL)

                var songData = SearchSingleSongData(songJson.getIntValue(SONG_ID),
                    songJson.getString(SONG_NAME),
                    artistName,
                    imageUrl)
                dataList.add(songData)
//                print(songData)
                Log.i(TAG,songData.toString())
            }
        }
        return dataList
    }

    fun obtainSongUrl(songId:Int):String{
        var client = OkHttpClient()
        var request = Request.Builder()
            .url("https://link.hhtjim.com/163/${songId}.mp3")
            .get()
            .build()
        var call = client.newCall(request)
        var resonse = call.execute()
        var songPlayUrl:String = resonse.request.url.toString()
        return songPlayUrl
    }

}