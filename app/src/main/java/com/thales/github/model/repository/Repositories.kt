package com.thales.github.model.repository

import com.google.gson.annotations.SerializedName

class Repositories {
    @SerializedName("items")
    var repositories: List<Repository>? = null
}
