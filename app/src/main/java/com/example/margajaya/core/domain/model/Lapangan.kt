package com.example.margajaya.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Lapangan(
    val id: String,
    val harga: Int,
    val jenisLapangan: String,
    val jamMulai: String,
    val jamBerakhir: String,
    val available: Boolean,
    val imageUrls: List<String>
) :Parcelable