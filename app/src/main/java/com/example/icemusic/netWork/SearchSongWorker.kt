package com.example.icemusic.netWork

import android.util.Log
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.example.icemusic.data.searchData.SearchSingleSongData
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.BufferedReader

class SearchSongWorker {
    val TAG = "SearchSongWorker"

    val CODE = "code"
    val RESULT = "result"
    val SONGS = "songs"
    val SONG_ID = "id"
    val SONG_NAME = "name"
    val ARTISTS = "artists"
    val ARTIST_NAME = "name"
    var ARTIST_IMAGE_URL = "img1v1Url"

    suspend fun searchSongData(searchWord:String):MutableList<SearchSingleSongData>{
        var client = OkHttpClient()
        var request = Request.Builder()
            .url("http://musicapi.leanapp.cn/search?keywords=${searchWord}")
            .get()
            .build()
        var call = client.newCall(request)
        var resonse = call.execute()
        var resJSON:JSONObject? = null
        resonse.body?.let {
            var stream = it.charStream()
            var bufferReader:BufferedReader = BufferedReader(stream)
            var resStr = bufferReader.lineSequence().joinToString()
            resJSON = JSON.parseObject(resStr)
//            print(resJSON!!.toJSONString())
        }
        Log.i(TAG,"url: ${resonse.request.url}")
        return extractSingleSongDataList(resJSON)
    }
    fun extractSingleSongDataList(jsonObject: JSONObject?):MutableList<SearchSingleSongData>{
        var dataList = mutableListOf<SearchSingleSongData>()
        jsonObject?.let {
            if(it[CODE]!=200){
                return dataList
            }
            var songJsonArray = it.getJSONObject(RESULT).getJSONArray(SONGS)
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