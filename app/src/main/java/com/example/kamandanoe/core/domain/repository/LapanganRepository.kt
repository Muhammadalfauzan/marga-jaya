package com.example.kamandanoe.core.domain.repository

import com.example.kamandanoe.core.data.Resource
import com.example.kamandanoe.core.domain.model.LapanganModel
import kotlinx.coroutines.flow.Flow

interface LapanganRepository {
    fun getLapangan(tanggal: String): Flow<Resource<List<LapanganModel>>>
    fun getLapById(id: String, tanggal: String): Flow<Resource<LapanganModel>>

}