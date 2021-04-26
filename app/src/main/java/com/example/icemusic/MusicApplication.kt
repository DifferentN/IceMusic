package com.example.icemusic

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class MusicApplication :Application(){
    override fun onCreate() {
        //放到AppCompatActivity中会导致recreate Activity，进一步导致fragment detach，使依赖fragment生命周期的方法出bug:未解决
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate()

    }
}