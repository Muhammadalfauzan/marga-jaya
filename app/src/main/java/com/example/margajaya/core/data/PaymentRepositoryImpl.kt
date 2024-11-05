package com.example.margajaya.core.data

import com.example.margajaya.core.data.source.remote.network.ApiService
import com.example.margajaya.core.data.source.remote.response.PaymentResponse
import com.example.margajaya.core.domain.model.PaymentModel
import com.example.margajaya.core.domain.repository.PaymentRepository
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : PaymentRepository {
    override suspend fun processPayment(paymentModel: PaymentModel): Resource<PaymentResponse> {
        return try {
            val response = apiService.payment(paymentModel)
            if (response.success == true && response.data != null) {
                Resource.Success(response)
            } else {
                Resource.Error("Payment failed")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An error occurred")
        }
    }
}