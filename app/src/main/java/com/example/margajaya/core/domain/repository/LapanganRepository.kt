package com.example.margajaya.core.domain.repository

import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.domain.model.Lapangan
import kotlinx.coroutines.flow.Flow

interface LapanganRepository {
    fun getLapangan(tanggal: String): Flow<Resource<List<Lapangan>>>
}