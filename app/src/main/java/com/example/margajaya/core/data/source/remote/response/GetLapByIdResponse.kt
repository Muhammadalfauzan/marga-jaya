package com.example.margajaya.core.data.source.remote.response

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

data class GetLapByIdResponse(

    @field:SerializedName("data")
    val data: Hasil? = null,

    @field:SerializedName("success")
    val success: Boolean? = null
)

data class Hasil(

    @field:SerializedName("lapangan")
    val lapangan: Lapangan? = null
)
data class Lapangan(

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("harga")
    val harga: Int? = null,

    @field:SerializedName("SesiLapangan")
    val sesiLapangan: SesiLap? = null,

    @field:SerializedName("available")
    val available: Boolean? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("JenisLapangan")
    val jenisLapangan: JenLapangan? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null
)

data class Gambar(

    @field:SerializedName("imageUrl")
    val imageUrl: String? = null
)



data class SesiLap(

    @field:SerializedName("jam_mulai")
    val jamMulai: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("jam_berakhir")
    val jamBerakhir: String? = null
)

data class JenLapangan(

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("deskripsi")
    val deskripsi: String? = null,

    @field:SerializedName("Image")
    val image: ArrayList<Gambar?>? = null,

    @field:SerializedName("jenis_lapangan")
    val jenisLapangan: String? = null
)