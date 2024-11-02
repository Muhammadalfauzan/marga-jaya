package com.example.margajaya.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope
import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.domain.model.LapanganModel
import com.example.margajaya.core.domain.usecase.LapanganUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val lapanganUseCase: LapanganUseCase
) : ViewModel() {


    private val _lapangan = MutableLiveData<Resource<List<LapanganModel>>>()
    val lapangan: LiveData<Resource<List<LapanganModel>>> get() = _lapangan

    init {
        fetchLapanganData(getCurrentDate()) // Fetch data dengan tanggal saat ini saat ViewModel dibuat
    }

    private fun getCurrentDate(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }
    fun fetchLapanganData(tanggal: String) {
        viewModelScope.launch {
            lapanganUseCase.getLapangan(tanggal).collect { resource ->
                _lapangan.value = resource // Update LiveData dengan data terbaru
            }
        }
    }
}
// Langsung mengonversi Flow menjadi LiveData
//val lapangan = lapanganUseCase.getLapangan("2024-10-31").asLiveData()