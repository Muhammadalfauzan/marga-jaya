package com.example.margajaya.core.domain.usecase

import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.domain.model.Lapangan
import kotlinx.coroutines.flow.Flow

interface LapanganUseCase {
    fun getLapangan(tanggal: String): Flow<Resource<List<Lapangan>>>

}
