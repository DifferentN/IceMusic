package com.example.icemusic.netWork

import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.Parcelable
import android.util.Log
import android.webkit.WebSettings
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.example.icemusic.data.LoadPageErrorData
import com.example.icemusic.data.searchData.searchResultData.SearchResultTabData
import com.example.icemusic.data.searchData.searchResultData.SearchResultTabType
import com.example.icemusic.data.searchData.searchResultData.SearchSingleSongData
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.BufferedReader
import java.net.SocketException
import java.net.SocketTimeoutException


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
    private val AUTHOR = "author"

    fun searchBySearchTabDataAndSongName(tabData: SearchResultTabData,songName:String):MutableList<out Parcelable>{
        var res:MutableList<out Parcelable>? = when(tabData.tabType){
            SearchResultTabType.SINGLE_SONG_TYPE ->{
                Log.i(TAG,"when SINGLE_SONG_TYPE")
                searchSongData(songName)

            }
            else->{
                Log.i(TAG,"when else")
                //没有找到对应类型，先返回单曲数据
                searchSongData(songName)
            }
        }


        if(res==null){
            //未加载到数据，填充错误提示数据
            res = obtainLoadPageErrorData()
//            Log.i(TAG,"搜索歌曲数据加载失败")
        }

        return res
    }

    /**
     * 返回一个数据加载失败的提示数据
     * @return MutableList<LoadPageErrorData>
     */
    private fun obtainLoadPageErrorData():MutableList<LoadPageErrorData>{
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
        var url1 = "http://api.guaqb.cn/music/music/?input=${searchWord}&filter=name&type=163&limit=30"
        var url2 = "https://v1.alapi.cn/api/music/search?keyword=${searchWord}&limit=30"
        //先使用guaqb网址请求数据，再使用v1请求数据
        var res:MutableList<SearchSingleSongData>? = null
        //catch网络异常，避免使主线程崩溃
        try{
            res = searchSongDataFromDifUrl(url1) { extractSingleSongDataListFromguaqb(it) }
        }catch (e:SocketTimeoutException){
            Log.e(TAG,"SocketTimeoutException: "+e.message.toString())
        }catch (e:SocketException){
            Log.e(TAG,"SocketException: "+e.message.toString())
        }catch (e:Exception){
            Log.e(TAG,"Exception: "+e.message.toString())
        }
        if(res==null){
            try{
                res = searchSongDataFromDifUrl(url2) { extractSingleSongDataListFromV1alapi(it) }
            }catch (e:Exception){
                Log.e(TAG,"Exception: "+e.message.toString())
            }

        }
        return res
    }
    private fun searchSongDataFromDifUrl(url:String,processJSONFun:(JSONObject?)->MutableList<SearchSingleSongData>):MutableList<SearchSingleSongData>?{
        var request = Request.Builder()
            .url(url)
            .get()
            .build()
        var call = client.newCall(request)
        var response = call.execute()
        var resJSON:JSONObject? = null
        print("response code ${response.code}")
        Log.i(TAG,"response code ${response.code}")
        var res = if(response.code==200){
            //成功获取数据
            response.body?.let {
                var stream = it.charStream()
                var bufferReader:BufferedReader = BufferedReader(stream)
                var resStr = bufferReader.lineSequence().joinToString()
                resJSON = JSON.parseObject(resStr)
                print(resJSON!!.toJSONString())
                Log.i(TAG,"cache: ${response.cacheResponse}")
                response.headers.forEach {
                    Log.i(TAG,"name: ${it.first} value: ${it.second}")
                }
            }

            processJSONFun(resJSON).let {
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
    private fun extractSingleSongDataListFromV1alapi(jsonObject: JSONObject?):MutableList<SearchSingleSongData>{
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
//                Log.i(TAG,songData.toString())
            }
        }
        return dataList
    }

    private fun extractSingleSongDataListFromguaqb(jsonObject: JSONObject?):MutableList<SearchSingleSongData>{
        var dataList = mutableListOf<SearchSingleSongData>()
        jsonObject?.let {
            if(it[CODE]!=200){
                return dataList
            }
            var songJsonArray = it.getJSONArray(DATA)
            var size = Math.max(0,songJsonArray.size)
            for(i in 0 until size){
                var songJson = songJsonArray.getJSONObject(i)
                var artistName = songJson.getString(AUTHOR)
                var imageUrl = songJson.getString("pic")

                var songData = SearchSingleSongData(songJson.getIntValue("songid"),
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

    fun getUserAgent(context: Context): String? {
        var userAgent = ""
        userAgent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                WebSettings.getDefaultUserAgent(context)
            } catch (e: Exception) {
                System.getProperty("http.agent")
            }
        } else {
            System.getProperty("http.agent")
        }
        val sb = StringBuffer()
        var i = 0
        val length = userAgent.length
        while (i < length) {
            val c = userAgent[i]
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", c.toInt()))
            } else {
                sb.append(c)
            }
            i++
        }
        return sb.toString()
    }

    companion object{
        const val userAgent = "Mozilla/5.0 (Linux; Android 10; V1938CT Build/QP1A.190711.020; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/78.0.3904.96 Mobile Safari/537.36"
        val client:OkHttpClient by lazy {
            MusicOkHttpClient.client
        }
    }
}