package com.example.kamandanoe.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class GetLapResponse(

    @field:SerializedName("data")
    val data: DataLapangan? = null,

    @field:SerializedName("success")
    val success: Boolean? = null
)

data class SesiLapangan(

    @field:SerializedName("jam_mulai")
    val jamMulai: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("jam_berakhir")
    val jamBerakhir: String? = null
)

data class ImageItem(

    @field:SerializedName("imageUrl")
    val imageUrl: String? = null
)

data class JenisLapangan(

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("deskripsi")
    val deskripsi: String? = null,

    @field:SerializedName("Image")
    val image: List<ImageItem?>? = null,

    @field:SerializedName("jenis_lapangan")
    val jenisLapangan: String? = null
)

data class LapanganItem(

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("harga")
    val harga: Int? = null,

    @field:SerializedName("SesiLapangan")
    val sesiLapangan: SesiLapangan? = null,

    @field:SerializedName("available")
    val available: Boolean? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("JenisLapangan")
    val jenisLapangan: JenisLapangan? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null
)

data class DataLapangan(

    @field:SerializedName("lapangan")
    val lapangan: List<LapanganItem?>? = null
)
