package com.example.icemusic.data

import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable


data class AdvertData(var title: String?, var imageUrl:String?, var color:Int = Color.GREEN.toInt()):Parcelable{
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(imageUrl)
        parcel.writeInt(color)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AdvertData> {
        override fun createFromParcel(parcel: Parcel): AdvertData {
            return AdvertData(parcel)
        }

        override fun newArray(size: Int): Array<AdvertData?> {
            return arrayOfNulls(size)
        }
    }

}
