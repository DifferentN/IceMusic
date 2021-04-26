package com.example.icemusic.musicService

import okhttp3.*
import java.io.BufferedReader
import java.io.IOException

class PlayMusicService {
    fun obtainMusic(){
        var url = "https://link.hhtjim.com/163/5146554.mp3"
//        url = "https://www.baidu.com/"
        url = "http://jsap.attakids.com/?url=https://v.qq.com/x/cover/mzc00200kqecmvk.html"
        var client = OkHttpClient()
        var request = Request.Builder()
            .url(url)
            .get()
            .build()
        var call = client.newCall(request)
        var response = call.execute()
        print("code: "+response.code)
        print("message: "+response.message)
        print("url: ${response.request.url}")
//                print("body: "+response.body.toString())
        var bufferedReader = BufferedReader(response.body!!.charStream())
//                print("line: "+bufferedReader.readLine())
        var str = bufferedReader.lineSequence().joinToString()
        print(str)
    }
}