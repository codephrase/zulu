package com.zulu.android

import android.os.Parcel
import android.os.Parcelable

class NavigationState(
    val id: String,
    val name: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NavigationState> {
        override fun createFromParcel(parcel: Parcel): NavigationState {
            return NavigationState(parcel)
        }

        override fun newArray(size: Int): Array<NavigationState?> {
            return arrayOfNulls(size)
        }
    }
}