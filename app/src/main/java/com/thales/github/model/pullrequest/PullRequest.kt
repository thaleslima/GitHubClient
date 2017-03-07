package com.thales.github.model.pullrequest


import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.SerializedName

class PullRequest : Parcelable {

    @SerializedName("id")
    var id: Long = 0

    @SerializedName("title")
    var title: String? = null

    @SerializedName("body")
    var body: String? = null

    @SerializedName("created_at")
    var date: String? = null

    @SerializedName("html_url")
    var htmlUrl: String? = null

    @SerializedName("user")
    var user: User? = null

    val loginUser: String?
        get() {
            if (user != null) {
                return user!!.login
            }

            return null
        }

    val avatarUrlUser: String?
        get() {
            if (user != null) {
                return user!!.avatarUrl
            }

            return null
        }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(this.id)
        dest.writeString(this.title)
        dest.writeString(this.body)
        dest.writeString(this.date)
        dest.writeString(this.htmlUrl)
        dest.writeParcelable(this.user, flags)
    }

    constructor() {}

    protected constructor(source: Parcel) {
        this.id = source.readLong()
        this.title = source.readString()
        this.body = source.readString()
        this.date = source.readString()
        this.htmlUrl = source.readString()
        this.user = source.readParcelable<User>(User::class.java.classLoader)
    }

    companion object {

        @JvmField val CREATOR: Parcelable.Creator<PullRequest> = object : Parcelable.Creator<PullRequest> {
            override fun createFromParcel(source: Parcel): PullRequest {
                return PullRequest(source)
            }

            override fun newArray(size: Int): Array<PullRequest?> {
                return arrayOfNulls(size)
            }
        }
    }
}
