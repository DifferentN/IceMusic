package com.example.icemusic.viewModel

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.example.icemusic.R
import com.example.icemusic.data.eventBus.PlaySongEvent
import com.example.icemusic.data.searchData.SearchSingleSongData
import org.greenrobot.eventbus.EventBus

class PlaySongBottomTabViewModel:BaseViewModel() {

    val songData: ObservableField<SearchSingleSongData> by lazy {
        ObservableField<SearchSingleSongData>().also {
            it.set(SearchSingleSongData())
        }
    }

    //表示是否播放歌曲 true:播放 false：暂停
    var playSwitchFlag:ObservableBoolean = ObservableBoolean(false)

    init {
        layoutId = R.layout.play_song_bottom_tab
    }

    override fun bindData(viewDataBinding: ViewDataBinding, lifecycleOwner: LifecycleOwner) {
        //放到MainActivity
    }

    fun switchSong(view:View){
        if(playSwitchFlag.get()){
            playSwitchFlag.set(false)
        }else playSwitchFlag.set(true)
        EventBus.getDefault().post(PlaySongEvent(songData.get()!!))
    }
}