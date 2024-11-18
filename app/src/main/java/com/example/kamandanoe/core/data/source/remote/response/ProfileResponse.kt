package com.example.kamandanoe.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

    @field:SerializedName("data")
    val data: Result? = null,

    @field:SerializedName("success")
    val success: Boolean? = null
)

data class User(

    @field:SerializedName("role")
    val role: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("no_telp")
    val noTelp: String? = null,

    @field:SerializedName("email")
    val email: String? = null
)


data class Result(

    @field:SerializedName("user")
    val user: User? = null
)

