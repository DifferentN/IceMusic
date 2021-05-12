package com.example.icemusic.service.musicService

import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat

interface MusicControllerCallback {
    /**
     * 播放状态修改
     */
    fun onPlaybackStateChanged(state: PlaybackStateCompat?)

    /**
     * 当前播放歌曲信息修改
     */
    fun onMetadataChanged(metadata: MediaMetadataCompat?)

    /**
     * 播放队列修改
     */
    fun onQueueChanged(queue: List<MediaSessionCompat.QueueItem?>?)
}