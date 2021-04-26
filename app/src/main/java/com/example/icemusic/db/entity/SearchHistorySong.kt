package com.example.icemusic.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchHistorySong(val songName:String?, val songId:Int?){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}
