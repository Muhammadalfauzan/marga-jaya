package com.example.margajaya.core.domain.usecase

import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.data.source.remote.response.Lapangan
import com.example.margajaya.core.domain.model.LapanganModel
import com.example.margajaya.core.domain.repository.LapanganRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LapanganInteractor @Inject constructor(
    private val lapanganRepository: LapanganRepository
) : LapanganUseCase {

    override fun getLapangan(tanggal: String): Flow<Resource<List<LapanganModel>>> {
        return lapanganRepository.getLapangan(tanggal)
    }

    override fun getLapById(id: String, tanggal: String): Flow<Resource<LapanganModel>> {
        return lapanganRepository.getLapById(id, tanggal)
    }
}
