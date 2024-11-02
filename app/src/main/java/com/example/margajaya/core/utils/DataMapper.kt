package com.example.margajaya.core.utils

import com.example.margajaya.core.data.source.local.entity.LapanganEntity
import com.example.margajaya.core.data.source.remote.response.Lapangan
import com.example.margajaya.core.data.source.remote.response.LapanganItem
import com.example.margajaya.core.domain.model.LapanganModel

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
    fun mapResponseToModel(lapangan: Lapangan?): LapanganModel {
        return LapanganModel(
            id = lapangan?.id ?: "",
            harga = lapangan?.harga ?: 0,
            jenisLapangan = lapangan?.jenisLapangan?.jenisLapangan ?: "",
            jamMulai = lapangan?.sesiLapangan?.jamMulai ?: "",
            jamBerakhir = lapangan?.sesiLapangan?.jamBerakhir ?: "",
            available = lapangan?.available ?: false,
            imageUrls = lapangan?.jenisLapangan?.image?.mapNotNull { it?.imageUrl } ?: emptyList(),
            deskripsi = lapangan?.jenisLapangan?.deskripsi
        )
    }
    // Mengonversi dari List<LapanganEntity> (database) ke List<Lapangan> (domain model)
    fun mapEntitiesToDomain(input: List<LapanganEntity>): List<LapanganModel> =
        input.map { entity ->
            LapanganModel(
                id = entity.id,
                harga = entity.harga,
                jenisLapangan = entity.jenisLapangan,
                jamMulai = entity.jamMulai,
                jamBerakhir = entity.jamBerakhir,
                available = entity.available,
                imageUrls = entity.imageUrls,

            )
        }


}
