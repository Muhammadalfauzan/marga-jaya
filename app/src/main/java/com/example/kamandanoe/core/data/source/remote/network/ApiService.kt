package com.example.kamandanoe.core.data.source.remote.network

import com.example.kamandanoe.core.data.source.remote.response.BookingResponse
import com.example.kamandanoe.core.data.source.remote.response.GetLapByIdResponse
import com.example.kamandanoe.core.data.source.remote.response.GetLapResponse
import com.example.kamandanoe.core.data.source.remote.response.LoginResponse
import com.example.kamandanoe.core.data.source.remote.response.PaymentResponse
import com.example.kamandanoe.core.data.source.remote.response.ProfileResponse
import com.example.kamandanoe.core.data.source.remote.response.RegisterResponse
import com.example.kamandanoe.core.data.source.remote.response.UpdateUserResponse
import com.example.kamandanoe.core.domain.model.LoginModel
import com.example.kamandanoe.core.domain.model.PaymentModel
import com.example.kamandanoe.core.domain.model.RegisterModel
import com.example.kamandanoe.core.domain.model.UpdateUserModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users/bookings")
    suspend fun getAllBooking(): BookingResponse

    @GET("auth/users/me")
    suspend fun getProfile() : ProfileResponse

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


    @PUT("users")
    suspend fun updateUser(
        @Body userRequest: UpdateUserModel
    ): UpdateUserResponse
    /*
         */



/*



    */

}