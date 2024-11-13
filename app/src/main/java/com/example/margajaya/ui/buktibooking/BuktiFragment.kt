package com.example.margajaya.ui.buktibooking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.margajaya.R
import com.example.margajaya.databinding.FragmentBuktiBinding
import com.example.margajaya.databinding.FragmentHistoryBinding

class BuktiFragment : Fragment() {
    private var _binding: FragmentBuktiBinding? = null
    private val binding get() = _binding!!
    private val args: BuktiFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBuktiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val jenisLapangan = args.jenisLapangan
        val hari = args.hari
        val sesi = args.sesi
        val harga = args.harga
        val tglBooking = args.tglBooking

        binding.tvJenbukti.text = jenisLapangan
        binding.tvHaribukti.text = hari
        binding.tvJambukti.text = sesi
        binding.tvHargabukti.text = harga.toString()
        binding.tvWaktupesanbukti.text = tglBooking
    }
    }
