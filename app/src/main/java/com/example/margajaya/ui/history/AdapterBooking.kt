package com.example.margajaya.ui.history

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.margajaya.core.domain.model.BookingItemModel
import com.example.margajaya.databinding.ItemListBookingBinding
import java.text.SimpleDateFormat
import java.util.Locale

class AdapterBooking(
    private val onItemClick: (BookingItemModel) -> Unit // Fungsi untuk menangani klik item
) : ListAdapter<BookingItemModel, AdapterBooking.BookingViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val binding = ItemListBookingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val bookingItem = getItem(position)
        holder.bind(bookingItem)
    }

    inner class BookingViewHolder(private val binding: ItemListBookingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(bookingItem: BookingItemModel) {
            // Memuat gambar jika ada URL gambar, misalnya lapangan yang di-booking
            // Menampilkan data booking
            val tgl = bookingItem.tanggal
            val sformat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val inputformat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault())
            val date = inputformat.parse(tgl)
            val resultDate = sformat.format(date)

            binding.tvJenlaphistory.text = bookingItem.jenisLapangan
            binding.tvJamhistory.text = "${bookingItem.jamMulai} - ${bookingItem.jamBerakhir}"
            binding.tvStatusBooking.text = bookingItem.status
            binding.tvHarilap.text=resultDate


            // Mengatur klik item
            itemView.setOnClickListener {
                onItemClick(bookingItem)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BookingItemModel>() {
            override fun areItemsTheSame(oldItem: BookingItemModel, newItem: BookingItemModel): Boolean {
                return oldItem.id == newItem.id // Memastikan bahwa setiap item memiliki ID unik
            }

            override fun areContentsTheSame(oldItem: BookingItemModel, newItem: BookingItemModel): Boolean {
                return oldItem == newItem // Membandingkan seluruh properti dari item
            }
        }
    }
}