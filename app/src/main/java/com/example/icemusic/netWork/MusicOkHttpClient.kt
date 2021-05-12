package com.example.icemusic.netWork

import android.content.Context
import android.util.Log
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File

class MusicOkHttpClient {
    companion object{
        var TAG = "MusicOkHttpClient"
        private var file: File? = null

        fun initial(context: Context){
            file = context.externalCacheDir
        }

        val client:OkHttpClient by lazy {
            if(file==null){
                throw ExceptionInInitializerError("okhttp的缓存文件尚未初始化")
            }
            OkHttpClient.Builder()
                .cache(Cache(File(file,"okHttpCache"),10*1024*1024))
                .build()
        }
    }

}