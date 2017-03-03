package com.thales.github.model.pullrequest;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PullRequest implements Parcelable {

    @SerializedName("id")
    public long id;

    @SerializedName("title")
    public String title;

    @SerializedName("body")
    public String body;

    @SerializedName("created_at")
    public String date;

    @SerializedName("html_url")
    public String htmlUrl;

    @SerializedName("user")
    public User user;

    public String getLoginUser() {
        if (user != null) {
            return user.login;
        }

        return null;
    }

    public String getAvatarUrlUser() {
        if (user != null) {
            return user.avatarUrl;
        }

        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.title);
        dest.writeString(this.body);
        dest.writeString(this.date);
        dest.writeString(this.htmlUrl);
        dest.writeParcelable(this.user, flags);
    }

    public PullRequest() {
    }

    protected PullRequest(Parcel in) {
        this.id = in.readLong();
        this.title = in.readString();
        this.body = in.readString();
        this.date = in.readString();
        this.htmlUrl = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Parcelable.Creator<PullRequest> CREATOR = new Parcelable.Creator<PullRequest>() {
        @Override
        public PullRequest createFromParcel(Parcel source) {
            return new PullRequest(source);
        }

        @Override
        public PullRequest[] newArray(int size) {
            return new PullRequest[size];
        }
    };
}
