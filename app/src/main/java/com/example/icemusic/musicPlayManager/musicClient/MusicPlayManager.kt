package com.example.icemusic.musicPlayManager.musicClient

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.os.Binder
import android.os.Bundle
import android.os.RemoteException
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.text.TextUtils
import android.util.Log
import com.example.icemusic.data.searchData.searchResultData.SearchSingleSongData
import com.example.icemusic.musicPlayManager.musicService.IceMusicService
import java.util.concurrent.CopyOnWriteArrayList

class MusicPlayManager {
    val TAG = "MusicPlayManager"

    companion object {
        val SONG_DATA = "SEARCH_SINGLE_SONG_DATA"
        val SONG_ID = "SONG_ID"
        private var mContext: Context? = null

        val instance: MusicPlayManager by lazy {
            if (mContext == null) {
                throw Exception("context is null")
            }
            MusicPlayManager(mContext!!)
        }

        fun init(context: Context) {
            mContext = context
        }
    }

    private lateinit var mMediaBrowser: MediaBrowserCompat
    private var mMediaController: MediaControllerCompat? = null
    private var mTransportControls: MediaControllerCompat.TransportControls? = null

    //保存音频信息改变时的回调接口
    private val musicControllerCallbackList = CopyOnWriteArrayList<MusicControllerCallback>()

    private val mConnectionCallback = object : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            super.onConnected()
            try {
                connectToSession(mMediaBrowser.sessionToken)
            }catch (e: RemoteException){
                Log.e(TAG,"could not connect media controller ${e.message}")
            }catch (e : Exception){
                Log.e(TAG,"could not connect media controller ${e.message}")
            }
        }
    }

    private val mMediaControllerCallback = object : MediaControllerCompat.Callback() {
        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)
            //依次通知回调接口，播放状态发生变化
            dispatchPlaybackStateChanged(state)
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)
            //依次通知回调接口，播放数据发生变化
            dispatchMetadataChanged(metadata)
        }

        override fun onQueueChanged(queue: MutableList<MediaSessionCompat.QueueItem>?) {
            super.onQueueChanged(queue)
            //依次通知回调接口，播放列表发生变化
            dispatchQueueChanged(queue)
        }

        override fun onSessionEvent(event: String?, extras: Bundle?) {
            super.onSessionEvent(event, extras)
        }
    }

    constructor(context: Context) {

        if (!isValidPackage(context.packageName, Binder.getCallingUid(), context)) {
            return
        }
        mMediaBrowser = MediaBrowserCompat(context,
            ComponentName(context, IceMusicService::class.java),
            mConnectionCallback,
            null)

        //连接IceMusicService
        connect()
    }

    /**
     * 连接{@link IceMusicService}
     */
    fun connect(){
        if(!mMediaBrowser.isConnected){
            try {
                mMediaBrowser.connect()
            }catch (e:Exception){
                Log.e(TAG,"connect failed\n ${e.message}")
            }
        }
    }
    fun getCurrentMediaId():String?{
        var metaData = mMediaController?.metadata
        return metaData?.description?.mediaId
    }

    fun playMusic(songData:SearchSingleSongData){
        var bundle = Bundle()
        bundle.putParcelable(SONG_DATA,songData)
        mTransportControls?.playFromMediaId(songData.songID.toString(),bundle)
    }

    fun play(){
        mTransportControls?.play()
    }

    fun pause(){
        mTransportControls?.pause()
    }

    fun stop(){
        mTransportControls?.stop()
    }

    fun skipToPrevious(){
        mTransportControls?.skipToPrevious()
    }

    fun skipToNext(){
        mTransportControls?.skipToNext()
    }

    fun disconnect(){
        mMediaController?.unregisterCallback(mMediaControllerCallback)

        if(mMediaBrowser.isConnected){
            mMediaBrowser.disconnect()
        }
    }

    fun isConnected():Boolean{
        return mMediaBrowser.isConnected
    }

    /**
     * 获取MediaController与TransportControls
     * 建立MediaController与MediaSession之间的联系
     * @param token Token
     */
    private fun connectToSession(token: MediaSessionCompat.Token){

        mMediaController = MediaControllerCompat(mContext!!,token)
        //音频变化监听
        mMediaController?.registerCallback(mMediaControllerCallback)
        mTransportControls = mMediaController?.transportControls

        /**
         * 通知音频变化
         */

        //播放状态变化
        dispatchPlaybackStateChanged(getPlaybackState())
        //播放数据变化
        dispatchMetadataChanged(getMediaMetadata())
        //播放列表变化
        dispatchQueueChanged(getQueue())
    }

    private fun isValidPackage(pkg: String, uid: Int, context: Context): Boolean {
        // 包名为空
        if (TextUtils.isEmpty(pkg)) {
            return false
        }
        // PackageManager
        val pm: PackageManager = context.getPackageManager()
        val packages = pm.getPackagesForUid(uid)
        val N = packages!!.size
        for (i in 0 until N) {
            if (packages[i] == pkg) {
                return true
            }
        }
        Log.i(TAG,"isValid package")
        return false
    }

    private fun dispatchPlaybackStateChanged(state: PlaybackStateCompat?){
        for(musicControllerCallback in musicControllerCallbackList){
            musicControllerCallback.onPlaybackStateChanged(state)
        }
    }

    private fun dispatchMetadataChanged(metadata: MediaMetadataCompat?){
        for(musicControllerCallback in musicControllerCallbackList){
            musicControllerCallback.onMetadataChanged(metadata)
        }
    }

    private fun dispatchQueueChanged(queue: List<MediaSessionCompat.QueueItem?>?){
        for(musicControllerCallback in musicControllerCallbackList){
            musicControllerCallback.onQueueChanged(queue)
        }
    }

    fun registerMusicControllerCallback(callback: MusicControllerCallback){
        musicControllerCallbackList.add(callback)

        /**
         * 注册时，通知一次音频信息
         */
        callback.onPlaybackStateChanged(getPlaybackState())

        callback.onMetadataChanged(getMediaMetadata())

        callback.onQueueChanged(getQueue())

    }

    fun unregisterMusicControllerCallback(callback: MusicControllerCallback){
        musicControllerCallbackList.remove(callback)
    }

    /**
     * 当前播放数据
     * @return MediaMetadataCompat
     */
    private fun getMediaMetadata():MediaMetadataCompat?{
        return mMediaController?.metadata
    }

    /**
     * 当前播放状态
     * @return PlaybackStateCompat
     */
    private fun getPlaybackState():PlaybackStateCompat?{
        return mMediaController?.playbackState
    }

    /**
     * 回调播放队列
     * @return List<MediaSessionCompat.QueueItem>
     */
    private fun getQueue():List<MediaSessionCompat.QueueItem>?{
        return mMediaController?.queue
    }
}