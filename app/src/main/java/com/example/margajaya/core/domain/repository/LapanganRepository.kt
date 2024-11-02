package com.example.margajaya.core.domain.repository

import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.data.source.remote.response.Lapangan
import com.example.margajaya.core.domain.model.LapanganModel
import kotlinx.coroutines.flow.Flow

interface LapanganRepository {
    fun getLapangan(tanggal: String): Flow<Resource<List<LapanganModel>>>

    fun getLapById(id: String, tanggal: String): Flow<Resource<LapanganModel>>

}