package com.example.margajaya.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LapanganModel(
    val id: String,
    val harga: Int,
    val jenisLapangan: String,
    val jamMulai: String,
    val jamBerakhir: String,
    val available: Boolean,
    val imageUrls: List<String>,
    val deskripsi :  String? = null
) :Parcelable