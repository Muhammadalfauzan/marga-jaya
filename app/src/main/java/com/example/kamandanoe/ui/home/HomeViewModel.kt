package com.example.kamandanoe.ui.home

import android.util.Log
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
   // private val networkUtils: NetworkUtils
) : ViewModel() {

    var pickerDate: String = getCurrentDate()
    private val _lapangan = MutableLiveData<Resource<List<LapanganModel>>>()
    val lapangan: LiveData<Resource<List<LapanganModel>>> get() = _lapangan

    private val _selectedSession = MutableLiveData("Semua Sesi")
    val selectedSession: LiveData<String> get() = _selectedSession

    private val _originalDataList = MutableLiveData<List<LapanganModel>>()
    val originalDataList: LiveData<List<LapanganModel>> get() = _originalDataList

    init {
        fetchLapanganData(pickerDate)
    }

    private fun getCurrentDate(): String {
        return SimpleDateFormat("EEE, MMM dd yyyy", Locale.getDefault()).format(Date())
    }

    fun fetchLapanganData(tanggal: String) {
        _lapangan.postValue(Resource.Loading())
        resetSessionFilter()
        viewModelScope.launch {
            lapanganUseCase.getLapangan(tanggal).collect { resource ->
                _lapangan.postValue(resource)
                if (resource is Resource.Success) {
                    val data = resource.data ?: emptyList()
                    _originalDataList.postValue(data)
                } else if (resource is Resource.Error) {
                    _originalDataList.postValue(emptyList())
                }
            }
        }
    }

    fun updatePickerDate(newDate: String) {
        pickerDate = newDate
        fetchLapanganData(newDate)
    }

    fun updateSelectedSession(session: String) {
        _selectedSession.value = session
        applySessionFilter(session)
    }


    private fun applySessionFilter(session: String) {
        val dataList = _originalDataList.value.orEmpty()
        val filteredData = if (session == "Semua Sesi") {
            dataList
        } else {
            val sessionStartTime = session.split(" - ").first().trim()
            dataList.filter { it.jamMulai.trim() == sessionStartTime }
        }
        _lapangan.postValue(Resource.Success(filteredData))
    }

    fun resetSessionFilter() {
        _selectedSession.value = "Semua Sesi"
    }
}


// Langsung mengonversi Flow menjadi LiveData
//val lapangan = lapanganUseCase.getLapangan("2024-10-31").asLiveData()