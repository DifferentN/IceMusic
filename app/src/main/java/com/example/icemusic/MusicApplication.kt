package com.example.icemusic

import android.app.Application
import com.example.icemusic.db.MusicDatabaseInstance
import com.example.icemusic.netWork.MusicOkHttpClient
import com.example.icemusic.musicPlayManager.musicClient.MusicPlayManager

class MusicApplication :Application(){
    private val TAG = "MusicApplication"
    override fun onCreate() {
        //放到AppCompatActivity中会导致recreate Activity，进一步导致fragment detach，使依赖fragment生命周期的方法出bug:未解决
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate()

        //先实例化数据库
        MusicDatabaseInstance.getInstance(this)

//        if(BuildConfig.DEBUG){
//            ARouter.openLog()
//            ARouter.openDebug()
//        }
//        //初始化ARouter
//        ARouter.init(this)

        MusicOkHttpClient.initial(this)

        MusicPlayManager.init(this)

    }
}