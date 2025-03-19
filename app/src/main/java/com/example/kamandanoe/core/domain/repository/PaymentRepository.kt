package com.example.kamandanoe.core.domain.repository

import com.example.kamandanoe.core.data.Resource
import com.example.kamandanoe.core.data.source.remote.response.PaymentResponse
import com.example.kamandanoe.core.domain.model.PaymentModel


interface PaymentRepository {

    suspend fun processPayment(paymentModel: PaymentModel): Resource<PaymentResponse>
}
