package com.example.icemusic.netWork

import com.example.icemusic.musicService.PlayMusicService
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

class SearchSongWorkerTest : TestCase() {
    var searchSongWorker:SearchSongWorker? = null

    @Before
    fun before(){
        searchSongWorker = SearchSongWorker()
    }
    fun testSearchSongData() {
        searchSongWorker = SearchSongWorker()
        searchSongWorker?.searchSongData("好几年")
    }

    fun testExtractSingleSongDataList() {}
}