package com.thales.github.model.repository


import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.SerializedName

class Repository : Parcelable {
    @SerializedName("id")
    var id: Long = 0

    @SerializedName("name")
    var name: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("forks_count")
    var forksCount: Long = 0

    @SerializedName("stargazers_count")
    var starsCount: Long = 0

    @SerializedName("owner")
    var owner: Owner? = null

    val loginUser: String?
        get() {
            if (owner != null) {
                return owner!!.login
            }

            return null
        }

    val avatarUrlUser: String?
        get() {
            if (owner != null) {
                return owner!!.avatarUrl
            }

            return null
        }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(this.id)
        dest.writeString(this.name)
        dest.writeString(this.description)
        dest.writeLong(this.forksCount)
        dest.writeLong(this.starsCount)
        dest.writeParcelable(this.owner, flags)
    }

    constructor() {}

    protected constructor(source: Parcel) {
        this.id = source.readLong()
        this.name = source.readString()
        this.description = source.readString()
        this.forksCount = source.readLong()
        this.starsCount = source.readLong()
        this.owner = source.readParcelable<Owner>(Owner::class.java.classLoader)
    }

    companion object {

        @JvmField val CREATOR: Parcelable.Creator<Repository> = object : Parcelable.Creator<Repository> {
            override fun createFromParcel(source: Parcel): Repository {
                return Repository(source)
            }

            override fun newArray(size: Int): Array<Repository?> {
                return arrayOfNulls(size)
            }
        }
    }
}
