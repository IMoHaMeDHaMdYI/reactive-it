package com.mohamed.reactiveit.common.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Result<T>(
    @Expose
    val message: String?,
    @Expose
    @SerializedName("result")
    val data: T
) {
    fun isSuccess(): Boolean {
        return message == null
    }
}