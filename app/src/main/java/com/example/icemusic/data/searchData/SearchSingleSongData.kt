package com.example.icemusic.data.searchData

data class SearchSingleSongData(var songID:Int = 27678655,
                                var songName:String = "李白",
                                var singerName:String = "李荣浩",
                                var imageUrl:String = "http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg"){
    override fun equals(other: Any?): Boolean {
        if(other==null){
            return false
        }
        if(other !is SearchSingleSongData){
            return false
        }
        var otherSongData = other as SearchSingleSongData
        if(this.songID==otherSongData.songID){
            return true
        }
        return false
    }
}
