package com.example.icemusic.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.icemusic.db.DAO.SearchHistorySongDAO
import com.example.icemusic.db.entity.SearchHistorySong

@Database(entities = arrayOf(SearchHistorySong::class),version = 2)
abstract class MusicDatabase:RoomDatabase() {
    abstract fun searchHistorySongDao():SearchHistorySongDAO
}