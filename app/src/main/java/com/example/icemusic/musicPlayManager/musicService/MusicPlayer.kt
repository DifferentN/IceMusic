package com.example.icemusic.musicPlayManager.musicService

import android.media.MediaMetadata
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import com.example.icemusic.data.searchData.searchResultData.SearchSingleSongData
import com.example.icemusic.musicPlayManager.musicClient.MusicPlayManager.Companion.SONG_DATA
import com.example.icemusic.netWork.SearchSongWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.wait

class MusicPlayer(val musicChangeCallback: MusicChangeCallback) :MediaSessionCompat.Callback(){
    val TAG = "MusicPlayer"

    var mediaPlayer: MediaPlayer
    var lastMediaId = ""

    //当前的播放速度
    var mCurrentSpeed = 1f
    var mCurrentState = PlaybackStateCompat.STATE_NONE
    //歌曲时长
    var mediaDuration:Long = 0L

    var onPreparedListener = object :MediaPlayer.OnPreparedListener{
        override fun onPrepared(mp: MediaPlayer?) {
            Log.i(TAG,"mediaPlayer start")
            mp?.start()
            //通知Client开始播放
            mediaDuration = mp?.duration?.toLong()?:0L
            mCurrentState = PlaybackStateCompat.STATE_PLAYING
            musicChangeCallback.onStateChanged(createPlaybackState(mCurrentState))
        }
    }

    var onCompletionListener = object :MediaPlayer.OnCompletionListener{
        override fun onCompletion(mp: MediaPlayer?) {
            //通知Client播放完成
            mCurrentState = PlaybackStateCompat.STATE_STOPPED
            musicChangeCallback.onStateChanged(createPlaybackState(mCurrentState))
        }
    }
    companion object{
        val MEDIA_DURATION = "DURATION"
    }
    init {
        mediaPlayer = MediaPlayer()
        mediaPlayer.setOnPreparedListener(onPreparedListener)
        mediaPlayer.setOnCompletionListener(onCompletionListener)
    }

    /**
     * 继续播放
     */
    override fun onPlay() {
        super.onPlay()
        if(!mediaPlayer.isPlaying){
            mediaPlayer.start()
        }

        mCurrentState = PlaybackStateCompat.STATE_PLAYING
        musicChangeCallback.onStateChanged(createPlaybackState(mCurrentState))
    }

    override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {
        mediaId?.let {
            if(mediaId != lastMediaId){
                if(mCurrentState != PlaybackStateCompat.STATE_STOPPED){
                    mediaPlayer.stop()
                    mCurrentState = PlaybackStateCompat.STATE_STOPPED
                    musicChangeCallback.onStateChanged(createPlaybackState(mCurrentState))
                }
                mediaPlayer.reset()

                CoroutineScope(Dispatchers.Main).launch{
                    var searchWork = SearchSongWorker()
                    //获取歌曲播放地址
                    var songUrl = withContext(Dispatchers.IO){
                        searchWork.obtainSongUrl(mediaId.toInt())
                    }
                    //异步准备播放数据
                    mediaPlayer.setDataSource(songUrl)
                    mediaPlayer.prepareAsync()
                    Log.i(TAG,"prepare async")
                }
                //通知client播放数据发生变化
                var metaData = createMetaData(mediaId,extras)
                musicChangeCallback.onMetaDataChanged(metaData)
                //通知进入加载数据状态
                mCurrentState = PlaybackStateCompat.STATE_BUFFERING
                musicChangeCallback.onStateChanged(createPlaybackState(mCurrentState))
            }else if(!mediaPlayer.isPlaying){
                //播放完成，重新播放
                mediaPlayer.start()

                mCurrentState = PlaybackStateCompat.STATE_PLAYING
                musicChangeCallback.onStateChanged(createPlaybackState(mCurrentState))
            }
            lastMediaId = mediaId
        }
    }

    override fun onPlayFromSearch(query: String?, extras: Bundle?) {
        super.onPlayFromSearch(query, extras)
    }

    override fun onPlayFromUri(uri: Uri?, extras: Bundle?) {
        super.onPlayFromUri(uri, extras)
    }

    override fun onSkipToQueueItem(id: Long) {
        super.onSkipToQueueItem(id)
    }

    /**
     * 暂停播放
     */
    override fun onPause() {
        super.onPause()
        if(mediaPlayer.isPlaying){
            mediaPlayer.pause()
        }

        mCurrentState = PlaybackStateCompat.STATE_PAUSED
        musicChangeCallback.onStateChanged(createPlaybackState(mCurrentState))
    }

    override fun onSkipToNext() {
        super.onSkipToNext()
    }

    override fun onSkipToPrevious() {
        super.onSkipToPrevious()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onSeekTo(pos: Long) {
        super.onSeekTo(pos)
    }

    override fun onCustomAction(action: String?, extras: Bundle?) {
        super.onCustomAction(action, extras)
    }


    private fun getAvailableActions(): Long {
        // 可用的播放状态
        var actions = PlaybackStateCompat.ACTION_PLAY_PAUSE or
                PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID or
                PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH or
                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or
                PlaybackStateCompat.ACTION_SKIP_TO_NEXT
        return actions
    }

    /**
     * 根据mediaId和extras中的数据创建一个metaData
     * @param mediaId String?
     * @param extras Bundle?
     * @return MediaMetadataCompat
     */
    fun createMetaData(mediaId: String?, extras: Bundle?): MediaMetadataCompat{
        var songData: SearchSingleSongData? = extras?.getParcelable<SearchSingleSongData>(SONG_DATA)
        var metadataBuilder = MediaMetadataCompat.Builder()
            .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID,mediaId)

        songData?.let {
            metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_AUTHOR,it.singerName)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE,it.songName)
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI,it.imageUrl)
        }
        return metadataBuilder.build()
    }

    /**
     * 创建一个PlaybackState
     * @param state Int
     * @return PlaybackStateCompat
     */
    fun createPlaybackState(state:Int):PlaybackStateCompat{
        var stateBuilder = PlaybackStateCompat.Builder().setActions(getAvailableActions())
        var currentPosition = mediaPlayer.currentPosition.toLong()
        stateBuilder.setState(state,currentPosition,mCurrentSpeed,SystemClock.elapsedRealtime())
        stateBuilder.setExtras(Bundle().apply {
            putLong(MEDIA_DURATION,mediaDuration)
        })
        return stateBuilder.build()
    }
}