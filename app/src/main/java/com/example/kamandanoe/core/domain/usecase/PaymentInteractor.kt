package com.example.kamandanoe.core.domain.usecase

import com.example.kamandanoe.core.data.Resource
import com.example.kamandanoe.core.data.source.remote.response.PaymentResponse
import com.example.kamandanoe.core.domain.model.PaymentModel
import com.example.kamandanoe.core.domain.repository.PaymentRepository
import javax.inject.Inject

class PaymentInteractor @Inject constructor(
    private val paymentRepository: PaymentRepository
) : PaymentUseCase {
    override suspend fun execute(paymentModel: PaymentModel): Resource<PaymentResponse> {
        return paymentRepository.processPayment(paymentModel)
    }
}