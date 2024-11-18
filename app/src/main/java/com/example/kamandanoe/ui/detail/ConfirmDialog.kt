package com.example.kamandanoe.ui.detail

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.kamandanoe.R
import com.example.kamandanoe.core.domain.model.LapanganModel
import com.example.kamandanoe.core.domain.model.PaymentModel
class CustomBookingDialogFragment(
    private val lapangan: LapanganModel,
    private val idLapang: String,
    private val dateByStatus: String,
    private val onBookingConfirmed: (PaymentModel) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = layoutInflater.inflate(R.layout.custom_alert_dialog_pesan, null)
        val alertDialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
            .setView(dialogView)
            .create()

        // Set animasi sebelum menampilkan dialog
        alertDialog.window?.attributes?.windowAnimations = R.style.FadeDialogAnimation

        // Inisialisasi elemen dialog
        val tvJenisLapangan = dialogView.findViewById<TextView>(R.id.tv_jenis_lapangan)
        val tvSesiLapangan = dialogView.findViewById<TextView>(R.id.tv_sesi_lapangan)
        val tvHarga = dialogView.findViewById<TextView>(R.id.tv_harga)
        val btnPesan = dialogView.findViewById<Button>(R.id.btn_pesan)
        val ivClose = dialogView.findViewById<ImageView>(R.id.iv_close)

        // Set data ke elemen dialog
        tvJenisLapangan.text = lapangan.jenisLapangan
        tvSesiLapangan.text = "${lapangan.jamMulai} - ${lapangan.jamBerakhir}"
        tvHarga.text = "Rp.${lapangan.harga}"

        // Tombol untuk menutup dialog
        ivClose.setOnClickListener {
            dismiss()
        }

        // Tombol untuk konfirmasi booking
        btnPesan.setOnClickListener {
            val paymentModel = PaymentModel(id_lapangan = idLapang, tanggal = dateByStatus)
            onBookingConfirmed(paymentModel) // Callback ke parent fragment
            dismiss()
        }

        return alertDialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),  // 90% of screen width
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}
