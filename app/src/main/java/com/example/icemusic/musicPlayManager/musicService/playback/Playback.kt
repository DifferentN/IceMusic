package com.example.icemusic.musicPlayManager.musicService.playback

/**
 * 代表本地或远程的播放接口，IceMusicService使用此接口进行音乐的播放，暂停等待
 */
interface Playback {

    /**
     * Start/setup the playback.
     * Resources/listeners would be allocated by implementations.
     */
    fun start()

    /**
     * Stop the playback. All resources can be de-allocated by implementations here.
     */
    fun stop()

    fun setState(state:Int)

    /**
     * Get the current {@link android.media.session.PlaybackState#getState()}
     */
    fun getState():Int

    /**
     * @return boolean indicating whether the player is playing or is supposed to be
     * playing when we gain audio focus.
     */
    fun isPlaying(): Boolean

    fun play(url: String)

    fun pause()

    fun seekTo(position: Long)
}