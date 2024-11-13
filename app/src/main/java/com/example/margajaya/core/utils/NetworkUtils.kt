@file:Suppress("DEPRECATION")

package com.example.margajaya.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkUtils @Inject constructor(
    @ApplicationContext private val context: Context
) : NetworkHelper {

    /**
     * Method untuk memeriksa ketersediaan koneksi jaringan.
     * Menggunakan [ConnectivityManager] untuk mengevaluasi status jaringan.
     */
    @SuppressLint("ObsoleteSdkInt")
    override fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            // Periksa jenis koneksi
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } else {
            // Versi Android sebelum Marshmallow (API < 23)
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            networkInfo.isConnected
        }
    }
}

