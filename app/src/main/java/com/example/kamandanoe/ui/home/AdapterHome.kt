package com.example.kamandanoe.ui.home


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kamandanoe.R
import com.example.kamandanoe.core.domain.model.LapanganModel
import com.example.kamandanoe.databinding.ItemHomeBinding

class AdapterHome(
    private val onItemClick: (LapanganModel) -> Unit // Fungsi untuk menangani klik item
) : ListAdapter<LapanganModel, AdapterHome.LapanganViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LapanganViewHolder {
        val binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LapanganViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LapanganViewHolder, position: Int) {
        val lapangan = getItem(position)
        holder.bind(lapangan)
    }

    inner class LapanganViewHolder(private val binding: ItemHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(lapangan: LapanganModel) {
            // Misalnya, memuat gambar menggunakan Glide
            Glide.with(itemView.context)
                .load(lapangan.imageUrls.firstOrNull()) // Mengambil URL gambar pertama
                .placeholder(R.color.primary)
                .centerCrop()// Placeholder saat loading
                .into(binding.ivHomefrag)

            binding.tvStatushomefrag.text = if (lapangan.available) "TERSEDIA" else "TIDAK TERSEDIA"
            binding.tvJenishomefrag.text = lapangan.jenisLapangan
            binding.tvMulaihomefrag.text = lapangan.jamMulai
            binding.tvAkhirhomefrag.text = lapangan.jamBerakhir
            binding.tvHargahomefrag.text = "Rp.${lapangan.harga}"

            itemView.setOnClickListener {
                onItemClick(lapangan) // Menangani klik item
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<LapanganModel>() {
            override fun areItemsTheSame(oldItem: LapanganModel, newItem: LapanganModel): Boolean {
                return oldItem.id == newItem.id // Pastikan ID unik
            }

            override fun areContentsTheSame(
                oldItem: LapanganModel,
                newItem: LapanganModel
            ): Boolean {
                return oldItem == newItem // Bandingkan semua properti
            }
        }
    }
}
