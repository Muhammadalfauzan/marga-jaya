package com.example.margajaya.core.data.source.remote.network

import com.example.margajaya.core.data.source.remote.response.BookingResponse
import com.example.margajaya.core.data.source.remote.response.GetLapByIdResponse
import com.example.margajaya.core.data.source.remote.response.GetLapResponse
import com.example.margajaya.core.data.source.remote.response.LoginResponse
import com.example.margajaya.core.data.source.remote.response.PaymentResponse
import com.example.margajaya.core.data.source.remote.response.RegisterResponse
import com.example.margajaya.core.domain.model.LoginModel
import com.example.margajaya.core.domain.model.PaymentModel
import com.example.margajaya.core.domain.model.RegisterModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("lapangan")
    suspend fun getLapangan(
        @Query("tanggal") tanggal : String
    ): GetLapResponse

    @GET("lapangan/{id}")
    suspend fun getLapById(
        @Path("id") id : String,
        @Query("tanggal") tanggal: String
    ): GetLapByIdResponse

   @POST("auth/register")
      suspend fun register(
          @Body postModel: RegisterModel
      ): RegisterResponse


       @Headers("User-Agent: margajaya-app")
       @POST("auth/users/login")
       suspend fun login(
           @Body postModel: LoginModel
       ): LoginResponse

    @POST("bookings")
    suspend fun payment(
        @Body postModel : PaymentModel
    ): PaymentResponse

    @GET("users/bookings")
    suspend fun getAllBooking(): BookingResponse
    /*
          @GET("auth/users/me")
          suspend fun getProfile() : ProfileResponse*/



/*



    */

}