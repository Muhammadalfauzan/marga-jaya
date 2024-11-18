package com.example.kamandanoe.core.domain.usecase

import com.example.kamandanoe.core.data.Resource
import com.example.kamandanoe.core.domain.model.LapanganModel
import com.example.kamandanoe.core.domain.repository.LapanganRepository
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
