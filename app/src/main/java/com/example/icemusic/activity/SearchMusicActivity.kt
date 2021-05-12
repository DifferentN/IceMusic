package com.example.icemusic.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.icemusic.R

//@Route(path = "/iceMusic/SearchMusicActivity")
class SearchMusicActivity : AppCompatActivity() {
    companion object{
        val ARouterSearchMusicActivity = "/iceMusic/SearchMusicActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_music)
    }
}