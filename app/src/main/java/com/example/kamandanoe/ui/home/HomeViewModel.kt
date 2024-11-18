package com.example.kamandanoe.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kamandanoe.core.data.Resource
import com.example.kamandanoe.core.domain.model.LapanganModel
import com.example.kamandanoe.core.domain.usecase.LapanganUseCase
import com.example.kamandanoe.core.utils.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val lapanganUseCase: LapanganUseCase,
    private val networkUtils: NetworkUtils
) : ViewModel() {

    var pickerDate: String = getCurrentDate() // Inisialisasi dengan tanggal hari ini secara default
    private val _lapangan = MutableLiveData<Resource<List<LapanganModel>>>()
    val lapangan: LiveData<Resource<List<LapanganModel>>> get() = _lapangan

    init {
        fetchLapanganData(pickerDate) // Fetch data saat ViewModel dibuat
    }

    private fun getCurrentDate(): String {
        return SimpleDateFormat("EEE,MMM dd yyyy", Locale.getDefault()).format(Date())
    }

    fun fetchLapanganData(tanggal: String) {
        if (networkUtils.isNetworkAvailable()) {
            viewModelScope.launch {
                lapanganUseCase.getLapangan(tanggal).collect { resource ->
                    _lapangan.value = resource
                }
            }
        } else {
            _lapangan.value = Resource.Error("No internet connection")
        }
    }

    fun updatePickerDate(newDate: String) {
        pickerDate = newDate
        fetchLapanganData(newDate) // Update data berdasarkan tanggal yang baru
    }
}

// Langsung mengonversi Flow menjadi LiveData
//val lapangan = lapanganUseCase.getLapangan("2024-10-31").asLiveData()