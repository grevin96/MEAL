package com.mega.test.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mega.test.api.ApiClient
import com.mega.test.model.BaseResponse
import com.mega.test.model.Data
import retrofit2.Call
import retrofit2.Response

class DetailViewModel: ViewModel() {
    private val suratLiveData                           = MutableLiveData<Data>()
    private val tafsirLiveData                          = MutableLiveData<Data>()
    private val progressLiveData                        = MutableLiveData<Boolean>()
    private val failureLiveData                         = MutableLiveData<Boolean>()
    private var callSurat: Call<BaseResponse<Data>>?    = null
    private var callTafsir: Call<BaseResponse<Data>>?   = null

    fun observerSurat(): LiveData<Data>         = suratLiveData
    fun observerTafsir(): LiveData<Data>        = tafsirLiveData
    fun observerProgress(): LiveData<Boolean>   = progressLiveData
    fun observerFailure(): LiveData<Boolean>    = failureLiveData

    fun surat(number: Int) {
        callSurat               = ApiClient.api.suratByNumber(number)
        progressLiveData.value  = true
        failureLiveData.value   = false

        callSurat?.enqueue(object: retrofit2.Callback<BaseResponse<Data>> {
            override fun onResponse(call: Call<BaseResponse<Data>>, response: Response<BaseResponse<Data>>) {
                progressLiveData.value  = false

                if (response.body() != null) suratLiveData.value = response.body()!!.data
            }

            override fun onFailure(call: Call<BaseResponse<Data>>, t: Throwable) {
                if (!call.isCanceled) failureLiveData.value = true
            }
        })
    }

    fun tafsir(number: Int) {
        callTafsir              = ApiClient.api.tafsirByNumber(number)
        progressLiveData.value  = true
        failureLiveData.value   = false

        callTafsir?.enqueue(object: retrofit2.Callback<BaseResponse<Data>> {
            override fun onResponse(call: Call<BaseResponse<Data>>, response: Response<BaseResponse<Data>>) {
                progressLiveData.value  = false

                if (response.body() != null) tafsirLiveData.value = response.body()!!.data
            }

            override fun onFailure(call: Call<BaseResponse<Data>>, t: Throwable) {
                if (!call.isCanceled) failureLiveData.value = true
            }
        })
    }

    fun cancel() {
        callSurat?.cancel()
        callTafsir?.cancel()
    }
}