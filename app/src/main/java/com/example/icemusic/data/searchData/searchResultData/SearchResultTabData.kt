package com.example.icemusic.data.searchData.searchResultData

import android.os.Parcel
import android.os.Parcelable

data class SearchResultTabData(var tabTitle:String?,var tabType:Int) : Parcelable{

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.let {
            it.writeString(tabTitle)
            it.writeInt(tabType)
        }
    }

    companion object CREATOR : Parcelable.Creator<SearchResultTabData> {
        override fun createFromParcel(parcel: Parcel): SearchResultTabData {
            return SearchResultTabData(parcel)
        }

        override fun newArray(size: Int): Array<SearchResultTabData?> {
            return arrayOfNulls(size)
        }
    }

}
