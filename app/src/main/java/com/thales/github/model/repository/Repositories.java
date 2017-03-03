package com.thales.github.model.repository;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Repositories {
    @SerializedName("items")
    public List<Repository> repositories;
}
