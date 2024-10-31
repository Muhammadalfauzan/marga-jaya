package com.example.margajaya.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lapangan_table")
data class LapanganEntity(
    @PrimaryKey
    val id: String,
    val harga: Int,
    val jenisLapangan: String,
    val jamMulai: String,
    val jamBerakhir: String,
    val available: Boolean,
    val imageUrls: List<String>,
    val createdAt: String?,
    val updatedAt: String?
)
