package com.example.margajaya.core.domain.usecase

import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.data.source.remote.response.PaymentResponse
import com.example.margajaya.core.domain.model.PaymentModel
import com.example.margajaya.core.domain.repository.PaymentRepository
import javax.inject.Inject

class PaymentInteractor @Inject constructor(
    private val paymentRepository: PaymentRepository
) : PaymentUseCase {
    override suspend fun execute(paymentModel: PaymentModel): Resource<PaymentResponse> {
        return paymentRepository.processPayment(paymentModel)
    }
}