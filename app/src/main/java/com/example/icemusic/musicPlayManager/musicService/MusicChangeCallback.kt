package com.example.icemusic.musicPlayManager.musicService

import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat

interface MusicChangeCallback {
    fun onStateChanged(state: PlaybackStateCompat?)

    fun onMetaDataChanged(metaData:MediaMetadataCompat)
}