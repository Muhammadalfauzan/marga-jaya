package com.example.margajaya.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

data class BookingDataModel(
    val bookings: List<BookingItemModel>?,
    val success: Boolean?
)
@Parcelize
data class BookingItemModel(
    val jamMulai: String?,
    val paymentLink: String?,
    val createdAt: String?,
    val paymentType: String?,
    val harga: Int?,
    val idLapangan: String?,
    val name: String?,
    val transactionTime: @RawValue Any?,
    val id: String?,
    val jamBerakhir: String?,
    val tanggal: String?,
    val jenisLapangan: String?,
    val updatedAt: String?,
    val status: String?
): Parcelable
