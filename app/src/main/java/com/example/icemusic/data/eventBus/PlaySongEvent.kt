package com.example.icemusic.data.eventBus

import com.example.icemusic.data.searchData.searchResultData.SearchSingleSongData

data class PlaySongEvent(var songData: SearchSingleSongData)
