package com.example.kamandanoe.ui.map

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.example.kamandanoe.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CustomWindowGoogleMaps(private val context: Context) : GoogleMap.InfoWindowAdapter {
    override fun getInfoContents(p0: Marker): View? {
        return null
    }

    @SuppressLint("InflateParams")
    override fun getInfoWindow(p0: Marker): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_snipet,null)
        return view

    }

}