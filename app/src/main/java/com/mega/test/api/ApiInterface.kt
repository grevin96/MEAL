package com.mega.test.api

import com.mega.test.model.BaseResponse
import com.mega.test.model.Data
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {
    @GET("surat")
    fun surat(): Call<BaseResponse<ArrayList<Data>>>

    @GET("surat/{number}")
    fun suratByNumber(@Path("number") number: Int): Call<BaseResponse<Data>>

    @GET("tafsir/{number}")
    fun tafsirByNumber(@Path("number") number: Int): Call<BaseResponse<Data>>
}