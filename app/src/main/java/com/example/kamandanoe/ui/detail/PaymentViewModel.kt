package com.example.kamandanoe.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kamandanoe.core.data.Resource
import com.example.kamandanoe.core.data.source.remote.response.PaymentResponse
import com.example.kamandanoe.core.domain.model.PaymentModel
import com.example.kamandanoe.core.domain.usecase.PaymentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val paymentUseCase: PaymentUseCase
) : ViewModel() {

    private val _paymentResult = MutableLiveData<Resource<PaymentResponse>>()
    val paymentResult: LiveData<Resource<PaymentResponse>> get() = _paymentResult

    fun processPayment(paymentModel: PaymentModel) {
        viewModelScope.launch {
            _paymentResult.postValue(Resource.Loading())
            val result = paymentUseCase.execute(paymentModel)
            _paymentResult.postValue(result)
        }
    }
}
