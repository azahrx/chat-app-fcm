package com.zahra.chatapp

import com.google.gson.annotations.SerializedName

data class MessageInfo (
    @SerializedName("user") val user: String?,
    @SerializedName("message") val message: String?
)