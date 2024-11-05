package com.example.margajaya.core.domain.repository

import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.data.source.remote.response.PaymentResponse
import com.example.margajaya.core.domain.model.PaymentModel

interface PaymentRepository {
    suspend fun processPayment(paymentModel: PaymentModel): Resource<PaymentResponse>
}
