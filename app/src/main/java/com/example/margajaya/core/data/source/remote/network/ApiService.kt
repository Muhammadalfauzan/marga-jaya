package com.example.margajaya.core.data.source.remote.network

import com.example.margajaya.core.data.source.remote.response.GetLapResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("lapangan")
    suspend fun getLapangan(
        @Query("tanggal") tanggal : String
    ): GetLapResponse
/*
    @GET("lapangan/{id}")
    suspend fun getLapById(
        @Path("id") id : String,
        @Query("tanggal") tanggal: String
    ):GetLapByIdResponse*/
    /*  @POST("auth/register")
      suspend fun register(
          @Body postModel: RegisterModel
      ): RegisterResponse


      @Headers("User-Agent: margajaya-app")
      @POST("auth/users/login")
      suspend fun login(
          @Body postModel: LoginModel
      ): LoginResponse


      @GET("auth/users/me")
      suspend fun getProfile() : ProfileResponse*/



/*


    @POST("bookings")
    suspend fun payment(
        @Body postModel : PaymetModel
    ):PaymentResponse

    @GET("users/bookings")
    suspend fun getAllHistory():HistoryResponse*/

}