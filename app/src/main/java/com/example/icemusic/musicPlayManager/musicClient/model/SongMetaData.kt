package com.example.icemusic.musicPlayManager.musicClient.model

/**
 *
 * @property songId Int
 * @property playProcess Float 歌曲播放进度，表示已播放了多少时间
 * @property songDuration Long 歌曲时长
 * @constructor
 */
data class SongMetaData(val songId:Int,val playProcess:Float,val songDuration:Long)
