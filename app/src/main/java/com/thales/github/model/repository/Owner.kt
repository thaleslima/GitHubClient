package com.thales.github.model.repository

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.SerializedName

class Owner : Parcelable {

    @SerializedName("id")
    var id: Long = 0

    @SerializedName("login")
    var login: String? = null

    @SerializedName("avatar_url")
    var avatarUrl: String? = null

    constructor() {}

    protected constructor(source: Parcel) {
        this.id = source.readLong()
        this.login = source.readString()
        this.avatarUrl = source.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(this.id)
        dest.writeString(this.login)
        dest.writeString(this.avatarUrl)
    }

    companion object {

        @JvmField val CREATOR: Parcelable.Creator<Owner> = object : Parcelable.Creator<Owner> {
            override fun createFromParcel(source: Parcel): Owner {
                return Owner(source)
            }

            override fun newArray(size: Int): Array<Owner?> {
                return arrayOfNulls(size)
            }
        }
    }
}
