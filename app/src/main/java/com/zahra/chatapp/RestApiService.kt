package com.zahra.chatapp

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class RestApiService {
    fun newMessage(userData: MessageInfo, onResult: (MessageInfo?) -> Unit){
        val retrofit = ServiceBuilder.buildService(RestApi::class.java)
        retrofit.sendMessage(userData).enqueue(
            object : Callback<MessageInfo> {
                override fun onFailure(call: Call<MessageInfo>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse( call: Call<MessageInfo>, response: Response<MessageInfo>) {
                    val newerMessage = response.body()
                    onResult(newerMessage)
                }
            }
        )
    }
}