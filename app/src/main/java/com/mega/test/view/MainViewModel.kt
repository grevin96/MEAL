package com.mega.test.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mega.test.api.ApiClient
import com.mega.test.model.BaseResponse
import com.mega.test.model.Data
import retrofit2.Call
import retrofit2.Response

class MainViewModel: ViewModel() {
    private val dataLiveData                                    = MutableLiveData<ArrayList<Data>>()
    private val progressLiveData                                = MutableLiveData<Boolean>()
    private val failureLiveData                                 = MutableLiveData<Boolean>()
    private var callData: Call<BaseResponse<ArrayList<Data>>>?  = null

    fun observerData(): LiveData<ArrayList<Data>>   = dataLiveData
    fun observerProgress(): LiveData<Boolean>       = progressLiveData
    fun observerFailure(): LiveData<Boolean>        = failureLiveData

    fun data() {
        callData               = ApiClient.api.surat()
        progressLiveData.value  = true
        failureLiveData.value   = false

        callData?.enqueue(object: retrofit2.Callback<BaseResponse<ArrayList<Data>>> {
            override fun onResponse(call: Call<BaseResponse<ArrayList<Data>>>, response: Response<BaseResponse<ArrayList<Data>>>) {
                progressLiveData.value  = false

                if (response.body() != null) dataLiveData.value = response.body()!!.data
            }

            override fun onFailure(call: Call<BaseResponse<ArrayList<Data>>>, t: Throwable) {
                if (!call.isCanceled) failureLiveData.value = true
            }
        })
    }


    fun cancel() { callData?.cancel() }
}