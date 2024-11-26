package com.example.kamandanoe.ui.history

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kamandanoe.R
import com.example.kamandanoe.core.domain.model.BookingItemModel
import com.example.kamandanoe.databinding.ItemListBookingBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class AdapterBooking(
    private val context: Context,
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
            // Format tanggal
            val bookingDate = formatTanggal(bookingItem.tanggal)

            // Menampilkan data booking
            binding.tvCourtName.text = bookingItem.jenisLapangan
            binding.tvSesi.text = "${bookingItem.jamMulai} - ${bookingItem.jamBerakhir}"

            val statusTextView = binding.tvStatus
            when (bookingItem.status?.lowercase()) {
                "success" -> {
                    statusTextView.setBackgroundResource(R.drawable.bg_status_success) // Hijau
                    statusTextView.text = "Success"
                    statusTextView.setTextColor(ContextCompat.getColor(context,R.color.green))
                    binding.tvLihat.visibility = View.VISIBLE
                }
                "pending" -> {
                    statusTextView.setBackgroundResource(R.drawable.bg_status_pending) // Kuning
                    statusTextView.text = "Pending"
                    statusTextView.setTextColor(ContextCompat.getColor(context, R.color.yellow))
                    binding.tvLihat.visibility = View.GONE
                }
                "expired" -> {
                    statusTextView.setBackgroundResource(R.drawable.bg_status_pending) // Merah
                    statusTextView.text = "Expired"
                    statusTextView.setTextColor(ContextCompat.getColor(context, R.color.red))
                    binding.tvLihat.visibility = View.GONE
                }
                else -> {
                    statusTextView.setBackgroundResource(R.drawable.bg_status_pending) // Default jika ada
                    statusTextView.text = "Unknown"
                    statusTextView.setTextColor(ContextCompat.getColor(context, R.color.red))
                    binding.tvLihat.visibility = View.GONE
                }
            }
            binding.tvBookingDate.text = bookingDate
            Log.d("AdapterBooking", "Binding booking item: ${bookingItem.jenisLapangan}")

            // Menampilkan atau menyembunyikan TextView "Lanjutkan Pembayaran" berdasarkan status
            if (bookingItem.status != "success") {
                binding.tvLanjutByr.visibility = View.VISIBLE
                binding.tvLanjutByr.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(bookingItem.paymentLink))
                    binding.root.context.startActivity(intent)
                }
            } else {
                binding.tvLanjutByr.visibility = View.GONE
            }

            // Mengatur klik item
            itemView.setOnClickListener {
                onItemClick(bookingItem)
            }
        }

        private fun formatTanggal(tanggal: String?): String {
            if (tanggal.isNullOrEmpty()) return "Tanggal tidak tersedia"
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            return try {
                val date = inputFormat.parse(tanggal)
                date?.let { outputFormat.format(it) } ?: "Tanggal tidak tersedia"
            } catch (e: ParseException) {
                "Tanggal tidak tersedia"
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
