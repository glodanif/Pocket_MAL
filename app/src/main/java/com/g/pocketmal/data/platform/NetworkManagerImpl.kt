package com.g.pocketmal.data.platform

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network

class NetworkManagerImpl(private val context: Context) : NetworkManager {

    override fun isNetworkAvailable(): Boolean {

        val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        try {
            val activeNetworks: Array<Network> = connectivityManager.allNetworks
            for (n in activeNetworks) {
                val nInfo = connectivityManager.getNetworkInfo(n)
                if (nInfo?.isConnectedOrConnecting == true) return true
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
        return false
    }
}
