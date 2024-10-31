package com.example.margajaya.core.utils

import com.example.margajaya.core.data.source.local.entity.LapanganEntity
import com.example.margajaya.core.data.source.remote.response.LapanganItem
import com.example.margajaya.core.domain.model.Lapangan

object DataMapper {

    // Mengonversi dari List<LapanganItem> (dari API) ke List<LapanganEntity> (untuk database)
    fun mapResponsesToEntities(input: List<LapanganItem?>?): List<LapanganEntity> {
        val lapanganList = ArrayList<LapanganEntity>()
        input?.mapNotNull { lapanganItem ->
            lapanganItem?.let {
                val lapangan = LapanganEntity(
                    id = it.id ?: "",
                    harga = it.harga ?: 0,
                    jenisLapangan = it.jenisLapangan?.jenisLapangan ?: "",
                    jamMulai = it.sesiLapangan?.jamMulai ?: "",
                    jamBerakhir = it.sesiLapangan?.jamBerakhir ?: "",
                    available = it.available ?: false,
                    imageUrls = it.jenisLapangan?.image?.mapNotNull { img -> img?.imageUrl } ?: emptyList(),
                    createdAt = it.createdAt,
                    updatedAt = it.updatedAt
                )
                lapanganList.add(lapangan)
            }
        }
        return lapanganList
    }

    // Mengonversi dari List<LapanganEntity> (database) ke List<Lapangan> (domain model)
    fun mapEntitiesToDomain(input: List<LapanganEntity>): List<Lapangan> =
        input.map { entity ->
            Lapangan(
                id = entity.id,
                harga = entity.harga,
                jenisLapangan = entity.jenisLapangan,
                jamMulai = entity.jamMulai,
                jamBerakhir = entity.jamBerakhir,
                available = entity.available,
                imageUrls = entity.imageUrls
            )
        }

    // Mengonversi dari Lapangan (domain model) ke LapanganEntity (untuk database)
    fun mapDomainToEntity(input: Lapangan) = LapanganEntity(
        id = input.id,
        harga = input.harga,
        jenisLapangan = input.jenisLapangan,
        jamMulai = input.jamMulai,
        jamBerakhir = input.jamBerakhir,
        available = input.available,
        imageUrls = input.imageUrls,
        createdAt = null, // Optional: bisa diisi dengan nilai default jika diperlukan
        updatedAt = null  // Optional: bisa diisi dengan nilai default jika diperlukan
    )
}
