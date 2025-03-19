package com.example.kamandanoe.ui.detail


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.kamandanoe.core.data.Resource
import com.example.kamandanoe.core.domain.model.LapanganModel
import com.example.kamandanoe.core.domain.usecase.LapanganUseCase

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val lapanganUseCase: LapanganUseCase
) : ViewModel() {

    fun getLapById(id: String, tanggal: String): LiveData<Resource<LapanganModel>> {
        return lapanganUseCase.getLapById(id, tanggal).asLiveData()
    }
}

