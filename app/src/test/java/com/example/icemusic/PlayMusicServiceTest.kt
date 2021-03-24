package com.example.icemusic

import com.example.icemusic.musicService.PlayMusicService
import org.junit.Before
import org.junit.Test

class PlayMusicServiceTest {
    lateinit var musicService: PlayMusicService

    @Before
    fun before(){
        musicService = PlayMusicService()
    }

    @Test
    fun testObtainMusic() {
        musicService.obtainMusic()
        Thread.sleep(1000)
    }
}