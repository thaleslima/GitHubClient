package com.thales.github.model.repository;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Repository implements Parcelable {
    @SerializedName("id")
    public long id;

    @SerializedName("name")
    public String name;

    @SerializedName("description")
    public String description;

    @SerializedName("forks_count")
    public long forksCount;

    @SerializedName("stargazers_count")
    public long starsCount;

    @SerializedName("owner")
    public Owner owner;

    public String getLoginUser() {
        if (owner != null) {
            return owner.login;
        }

        return null;
    }

    public String getAvatarUrlUser() {
        if (owner != null) {
            return owner.avatarUrl;
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
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeLong(this.forksCount);
        dest.writeLong(this.starsCount);
        dest.writeParcelable(this.owner, flags);
    }

    public Repository() {
    }

    protected Repository(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.description = in.readString();
        this.forksCount = in.readLong();
        this.starsCount = in.readLong();
        this.owner = in.readParcelable(Owner.class.getClassLoader());
    }

    public static final Parcelable.Creator<Repository> CREATOR = new Parcelable.Creator<Repository>() {
        @Override
        public Repository createFromParcel(Parcel source) {
            return new Repository(source);
        }

        @Override
        public Repository[] newArray(int size) {
            return new Repository[size];
        }
    };
}
