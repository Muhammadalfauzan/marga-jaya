package com.example.margajaya.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.domain.model.Lapangan
import com.example.margajaya.core.domain.usecase.LapanganUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val lapanganUseCase: LapanganUseCase
) : ViewModel() {
    // Langsung mengonversi Flow menjadi LiveData
  //  val lapangan = lapanganUseCase.getLapangan("2024-10-31").asLiveData()

    private val _lapangan = MutableLiveData<Resource<List<Lapangan>>>()
    val lapangan: LiveData<Resource<List<Lapangan>>> get() = _lapangan

    init {
        // Fetch data saat ViewModel dibuat
        fetchLapanganData("2024-10-31") // Atau gunakan tanggal default yang kamu inginkan
    }

    fun fetchLapanganData(tanggal: String) {
        viewModelScope.launch {
            lapanganUseCase.getLapangan(tanggal).collect { resource ->
                _lapangan.value = resource // Update LiveData dengan data terbaru
            }
        }
    }
}
