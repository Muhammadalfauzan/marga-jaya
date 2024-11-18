package com.example.kamandanoe.core.domain.usecase

import com.example.kamandanoe.core.data.Resource
import com.example.kamandanoe.core.domain.model.LapanganModel
import kotlinx.coroutines.flow.Flow

interface LapanganUseCase {
    fun getLapangan(tanggal: String): Flow<Resource<List<LapanganModel>>>

    fun getLapById(id: String, tanggal: String): Flow<Resource<LapanganModel>>
}
