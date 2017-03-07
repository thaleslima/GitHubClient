package com.thales.github.model.pullrequest


import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.SerializedName

class User : Parcelable {

    @SerializedName("id")
    var id: Long = 0

    @SerializedName("login")
    var login: String? = null

    @SerializedName("avatar_url")
    var avatarUrl: String? = null

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(this.id)
        dest.writeString(this.login)
        dest.writeString(this.avatarUrl)
    }

    constructor() {}

    protected constructor(source: Parcel) {
        this.id = source.readLong()
        this.login = source.readString()
        this.avatarUrl = source.readString()
    }

    companion object {

        @JvmField val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(source: Parcel): User {
                return User(source)
            }

            override fun newArray(size: Int): Array<User?> {
                return arrayOfNulls(size)
            }
        }
    }
}
