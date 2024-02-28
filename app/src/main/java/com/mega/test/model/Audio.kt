package com.mega.test.model

import com.google.gson.annotations.SerializedName

data class Audio(
    @SerializedName("01") val one: String?,
    @SerializedName("02") val two: String?,
    @SerializedName("03") val three: String?,
    @SerializedName("04") val four: String?,
    @SerializedName("05") val five: String?
)
