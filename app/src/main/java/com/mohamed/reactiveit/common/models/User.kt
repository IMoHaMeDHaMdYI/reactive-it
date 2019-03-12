package com.mohamed.reactiveit.common.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    @Expose
    @SerializedName("user_name")
    val name: String,
    @Expose
    val password: String
)