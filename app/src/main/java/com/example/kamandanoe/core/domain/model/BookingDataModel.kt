package com.example.kamandanoe.core.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

data class BookingDataModel(
    val bookings: List<BookingItemModel>?,
    val success: Boolean?
)
@Parcelize
data class BookingItemModel(
    @SerializedName("jam_mulai")
    val jamMulai: String? = null,

    @SerializedName("payment_link")
    val paymentLink: String? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("payment_type")
    val paymentType: String? = null,

    @SerializedName("harga")
    val harga: Int? = null,

    @SerializedName("id_lapangan")
    val idLapangan: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("transaction_time")
    val transactionTime: @RawValue Any?,

    @SerializedName("id")
    val id: String? = null,

    @SerializedName("jam_berakhir")
    val jamBerakhir: String? = null,

    @SerializedName("tanggal")
    val tanggal: String? = null,

    @SerializedName("jenis_lapangan")
    val jenisLapangan: String? = null,

    @SerializedName("updatedAt")
    val updatedAt: String? = null,

    @SerializedName("status")
    val status: String? = null
): Parcelable
