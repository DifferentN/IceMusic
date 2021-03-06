package com.example.icemusic.data.searchData.searchResultData

import android.os.Parcel
import android.os.Parcelable

data class SearchSingleSongData(var songID:Int = 27678655,
                                var songName:String? = "李白",
                                var singerName:String? = "李荣浩",
                                var imageUrl:String? = "http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg"):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(songID)
        parcel.writeString(songName)
        parcel.writeString(singerName)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SearchSingleSongData> {
        override fun createFromParcel(parcel: Parcel): SearchSingleSongData {
            return SearchSingleSongData(parcel)
        }

        override fun newArray(size: Int): Array<SearchSingleSongData?> {
            return arrayOfNulls(size)
        }
    }
}
