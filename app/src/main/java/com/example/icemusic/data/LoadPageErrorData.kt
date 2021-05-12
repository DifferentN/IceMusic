package com.example.icemusic.data

import android.os.Parcel
import android.os.Parcelable

data class LoadPageErrorData(var errorTip:String? = "加载错误，请稍后重试"):Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(errorTip)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LoadPageErrorData> {
        override fun createFromParcel(parcel: Parcel): LoadPageErrorData {
            return LoadPageErrorData(parcel)
        }

        override fun newArray(size: Int): Array<LoadPageErrorData?> {
            return arrayOfNulls(size)
        }
    }
}
