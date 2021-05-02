package com.example.icemusic.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["songName"],name = "songNameIndex",unique = true)])
data class SearchHistorySong(val songName:String?, val songId:Int?){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}
