package com.example.kamandanoe.core.domain.usecase

import com.example.kamandanoe.core.data.Resource
import com.example.kamandanoe.core.data.source.remote.response.PaymentResponse
import com.example.kamandanoe.core.domain.model.PaymentModel


interface PaymentUseCase {
    suspend fun execute(paymentModel: PaymentModel): Resource<PaymentResponse>
}