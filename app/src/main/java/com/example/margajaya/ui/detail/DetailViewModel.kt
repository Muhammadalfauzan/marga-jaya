package com.example.margajaya.ui.detail


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.domain.model.LapanganModel
import com.example.margajaya.core.domain.usecase.LapanganUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val lapanganUseCase: LapanganUseCase
) : ViewModel() {

    // Fungsi untuk mendapatkan detail lapangan berdasarkan ID dan tanggal
    fun getLapById(id: String, tanggal: String): LiveData<Resource<LapanganModel>> {
        return lapanganUseCase.getLapById(id, tanggal).asLiveData()
    }
}

