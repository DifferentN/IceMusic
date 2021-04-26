package com.example.icemusic.db.DAO

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.icemusic.db.entity.SearchHistorySong

@Dao
interface SearchHistorySongDAO {

    @Query("select * from searchhistorysong")
    fun getSearchHistorySong(): LiveData<List<SearchHistorySong>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSearchHistorySong(song: SearchHistorySong)

    @Delete
    fun deleteSearchHistorySong(songList:List<SearchHistorySong>)
}