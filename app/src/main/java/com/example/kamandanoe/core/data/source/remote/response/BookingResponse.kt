package com.example.kamandanoe.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class BookingResponse(

    @field:SerializedName("data")
    val data: Booking? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message :String? = null
)

data class Booking(

    @field:SerializedName("booking")
    val booking: List<BookingItem?>? = null
)

data class BookingItem(
    @field:SerializedName("jam_mulai")
    val jamMulai: String? = null,

    @field:SerializedName("payment_link")
    val paymentLink: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("payment_type")
    val paymentType: String? = null,

    @field:SerializedName("harga")
    val harga: Int? = null,

    @field:SerializedName("id_lapangan")
    val idLapangan: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("transaction_time")
    val transactionTime: Any? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("jam_berakhir")
    val jamBerakhir: String? = null,

    @field:SerializedName("tanggal")
    val tanggal: String? = null,

    @field:SerializedName("jenis_lapangan")
    val jenisLapangan: String? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)
