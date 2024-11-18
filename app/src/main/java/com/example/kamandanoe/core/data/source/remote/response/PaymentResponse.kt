package com.example.kamandanoe.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class PaymentResponse(

    @field:SerializedName("data")
    val data: Redirect? = null,

    @field:SerializedName("success")
    val success: Boolean? = null
)

data class Redirect(

    @field:SerializedName("redirectUrl")
    val redirectUrl: String? = null
)
