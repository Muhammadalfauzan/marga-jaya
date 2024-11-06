package com.example.margajaya.core.utils

import com.example.margajaya.core.data.source.local.entity.LapanganEntity
import com.example.margajaya.core.data.source.local.entity.UserProfileEntity
import com.example.margajaya.core.data.source.remote.response.Booking
import com.example.margajaya.core.data.source.remote.response.BookingItem
import com.example.margajaya.core.data.source.remote.response.BookingResponse
import com.example.margajaya.core.data.source.remote.response.Lapangan
import com.example.margajaya.core.data.source.remote.response.LapanganItem
import com.example.margajaya.core.data.source.remote.response.ProfileResponse
import com.example.margajaya.core.domain.model.BookingDataModel
import com.example.margajaya.core.domain.model.BookingItemModel
import com.example.margajaya.core.domain.model.LapanganModel
import com.example.margajaya.core.domain.model.ProfileModel

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
                    imageUrls = it.jenisLapangan?.image?.mapNotNull { img -> img?.imageUrl }
                        ?: emptyList(),
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

/*    // Mapper dari BookingResponse ke List<BookingDataModel>
    fun mapBookingResponseToDataModel(response: BookingResponse): List<BookingDataModel> {
        // Memetakan response ke dalam daftar BookingDataModel
        val bookingDataModels = response.data?.booking?.mapNotNull { bookingItem ->
            mapBookingItemToBookingDataModel(bookingItem)
        } ?: emptyList()

        return listOf(
            BookingDataModel(
                bookings = bookingDataModels,
                success = response.success
            )
        )
    }*/
/*

    // Mapper dari BookingItem ke BookingItemModel
    private fun mapBookingItemToBookingDataModel(bookingItem: BookingItem?): BookingItemModel? {
        return bookingItem?.let {
            BookingItemModel(
                jamMulai = it.jamMulai,
                paymentLink = it.paymentLink,
                createdAt = it.createdAt,
                paymentType = it.paymentType,
                harga = it.harga,
                idLapangan = it.idLapangan,
                name = it.name,
                transactionTime = it.transactionTime,
                id = it.id,
                jamBerakhir = it.jamBerakhir,
                tanggal = it.tanggal,
                jenisLapangan = it.jenisLapangan,
                updatedAt = it.updatedAt,
                status = it.status
            )
        }
    }
    fun mapBookingItemToDataModel(bookingItem: BookingItem): BookingItemModel {
        return BookingItemModel(
            jamMulai = bookingItem.jamMulai,
            paymentLink = bookingItem.paymentLink,
            createdAt = bookingItem.createdAt,
            paymentType = bookingItem.paymentType,
            harga = bookingItem.harga,
            idLapangan = bookingItem.idLapangan,
            name = bookingItem.name,
            transactionTime = bookingItem.transactionTime,
            id = bookingItem.id,
            jamBerakhir = bookingItem.jamBerakhir,
            tanggal = bookingItem.tanggal,
            jenisLapangan = bookingItem.jenisLapangan,
            updatedAt = bookingItem.updatedAt,
            status = bookingItem.status
        )
    }

*/




    fun mapBookingItemToModel(bookingItem: BookingItem?): BookingItemModel? {
        return bookingItem?.let {
            BookingItemModel(
                jamMulai = it.jamMulai,
                paymentLink = it.paymentLink,
                createdAt = it.createdAt,
                paymentType = it.paymentType,
                harga = it.harga,
                idLapangan = it.idLapangan,
                name = it.name,
                transactionTime = it.transactionTime,
                id = it.id,
                jamBerakhir = it.jamBerakhir,
                tanggal = it.tanggal,
                jenisLapangan = it.jenisLapangan,
                updatedAt = it.updatedAt,
                status = it.status
            )
        }
    }


        fun entityToModel(entity: UserProfileEntity?): ProfileModel {
            return ProfileModel(
                id = entity?.id ?: "",
                name = entity?.name ?: "",
                email = entity?.email ?: "",
                role = entity?.role ?: "",
                noTelp = entity?.noTelp ?: ""
            )
        }

        fun responseToEntity(response: ProfileResponse): UserProfileEntity? {
            return response.data?.user?.let {
                UserProfileEntity(
                    id = it.id ?: "",
                    name = it.name ?: "",
                    email = it.email ?: "",
                    role = it.role ?: "",
                    noTelp = it.noTelp ?: ""
                )
            }
        }
}
