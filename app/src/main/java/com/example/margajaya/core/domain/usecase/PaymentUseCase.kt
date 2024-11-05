package com.example.margajaya.core.domain.usecase

import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.data.source.remote.response.PaymentResponse
import com.example.margajaya.core.domain.model.PaymentModel

interface PaymentUseCase {
    suspend fun execute(paymentModel: PaymentModel): Resource<PaymentResponse>
}