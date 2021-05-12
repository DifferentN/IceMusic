package com.example.icemusic.data

import android.os.Parcel
import android.os.Parcelable

data class LoadingPageData(var hintWord: String? = "正在加载数据") :Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(hintWord)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LoadingPageData> {
        override fun createFromParcel(parcel: Parcel): LoadingPageData {
            return LoadingPageData(parcel)
        }

        override fun newArray(size: Int): Array<LoadingPageData?> {
            return arrayOfNulls(size)
        }
    }
}
