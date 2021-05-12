package com.example.icemusic.viewModel

import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.View
import androidx.databinding.*
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import com.example.icemusic.R
import com.example.icemusic.data.eventBus.PlaySongEvent
import com.example.icemusic.data.searchData.searchResultData.SearchSingleSongData
import com.example.icemusic.musicPlayManager.musicClient.MusicControllerCallback
import com.example.icemusic.musicPlayManager.musicClient.MusicPlayManager
import com.example.icemusic.musicPlayManager.musicClient.model.SongMetaData
import com.example.icemusic.musicPlayManager.musicService.MusicPlayer.Companion.MEDIA_DURATION
import org.greenrobot.eventbus.EventBus
import java.lang.ref.WeakReference

/**
 * 设置为APP整个生命周期的单实例，不使用vmStoreOwner
 * @property songData ObservableField<SearchSingleSongData>
 * @property playSwitchFlag ObservableBoolean
 */
class PlaySongBottomTabViewModel:BaseViewModel() {

    val songData: ObservableField<SearchSingleSongData> by lazy {
        ObservableField<SearchSingleSongData>().also {
            it.set(SearchSingleSongData())
        }
    }

    //表示是否播放歌曲 true:播放 false：暂停
    var playSwitchFlag:ObservableBoolean = ObservableBoolean(false)

    val songMetaData:ObservableField<SongMetaData> by lazy {
        ObservableField<SongMetaData>()
    }

    var musicPlayManager:MusicPlayManager
    var musicControllerCallback:VMMusicControllerCallback
    init {
        layoutId = R.layout.play_song_bottom_tab
        musicPlayManager = MusicPlayManager.instance

        musicControllerCallback = VMMusicControllerCallback(this)

        //注册音频变化时的回调
        musicPlayManager.registerMusicControllerCallback(musicControllerCallback)
    }

    override fun bindData(viewDataBinding: ViewDataBinding, lifecycleOwner: LifecycleOwner,viewModelStoreOwner: ViewModelStoreOwner) {
        //放到MainActivity
    }

    fun switchSong(view:View){
        if(playSwitchFlag.get()){
//            playSwitchFlag.set(false)
            musicPlayManager.pause()
        }else {
//            playSwitchFlag.set(true)
            if(musicPlayManager.getCurrentMediaId()==null){
                //首次播放
                musicPlayManager.playMusic(songData.get()!!)
            }else {
                //恢复播放
                musicPlayManager.play()
            }
        }
//        EventBus.getDefault().post(PlaySongEvent(songData.get()!!))
    }

    override fun onCleared() {
        super.onCleared()

        //清楚MusicPlayManager对VM的引用，避免内存泄露---策略2
        musicPlayManager.unregisterMusicControllerCallback(musicControllerCallback)
    }

    companion object{
        class VMMusicControllerCallback(vm:PlaySongBottomTabViewModel):MusicControllerCallback{
            val TAG = "VMMusicControllerCallb"
            //MusicPlayManager可能会持有VM，且MusicPlayManager生命周期较长，避免内存泄露---策略1
            var vm:WeakReference<PlaySongBottomTabViewModel> = WeakReference(vm)
            override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
                vm.get()?.let {
                    state?.apply {
                        when(getState()){
                            PlaybackStateCompat.STATE_BUFFERING->{

                            }
                            PlaybackStateCompat.STATE_PLAYING->{
                                //获取音乐时长
                                var totalTime = state.extras?.getLong(MEDIA_DURATION)?:-1L
                                //获取音乐播放进度
                                var currentProcess = state.position
                                if(totalTime>0){
                                    var percent = currentProcess/totalTime
                                    Log.i(TAG,"position: $currentProcess totalTime: $totalTime percent: $percent")
                                    var songMetaData = SongMetaData(-1,currentProcess.toFloat(),totalTime)
                                    it.songMetaData.set(songMetaData)
                                }

                                it.playSwitchFlag.set(true)
                                Log.i(TAG,"State: playing")
                            }
                            PlaybackStateCompat.STATE_PAUSED->{
                                it.playSwitchFlag.set(false)
                            }
                            PlaybackStateCompat.STATE_STOPPED->{
                                it.playSwitchFlag.set(false)
                            }
                        }
                    }
                }
            }

            override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
                vm.get()?.let {
                    metadata?.apply {
                        var songID = description.mediaId?.toInt()?:-1
                        var songName = getString(MediaMetadataCompat.METADATA_KEY_TITLE)
                        var singerName = getString(MediaMetadataCompat.METADATA_KEY_AUTHOR)
                        var imageUrl = getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI)
                        var songData = SearchSingleSongData(songID,songName,singerName,imageUrl)
                        Log.i(TAG,songData.toString())

                        it.songData.set(songData)
                    }
                }
            }

            override fun onQueueChanged(queue: List<MediaSessionCompat.QueueItem?>?) {

            }
        }

    }
}