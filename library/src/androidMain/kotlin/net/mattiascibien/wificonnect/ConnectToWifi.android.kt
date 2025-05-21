package net.mattiascibien.wificonnect

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import android.net.wifi.WifiNetworkSuggestion
import android.os.Build
import androidx.annotation.RequiresApi
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Suppress("unused")
@RequiresApi(Build.VERSION_CODES.Q)
actual suspend fun connectToWifi(ssid: String, type: WiFiType, password: String?) : Boolean {
    return suspendCoroutine { continuation ->
        val wifiManager = AndroidServicesImpl.getInstance().wifiManager
        val connectivityManager = AndroidServicesImpl.getInstance().connectivityManager

        val suggestionBuilder = WifiNetworkSuggestion.Builder()
            .setSsid(ssid)

        if (password != null) {
            when (type) {
                WiFiType.Wpa2 -> suggestionBuilder.setWpa2Passphrase(password)
                WiFiType.Wpa3 -> suggestionBuilder.setWpa3Passphrase(password)
                WiFiType.Wep -> continuation.resumeWithException(Exception("WEP is not supported on Android"))
                WiFiType.Unsecured -> continuation.resumeWithException(Exception("Password should not be specified when WiFiType.Unsecured"))
            }
        }

        val status = wifiManager.addNetworkSuggestions(listOf(suggestionBuilder.build()))
        if (status != WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS) {
            continuation.resume(false)
        }

        val specifier = WifiNetworkSpecifier.Builder().setSsid(ssid).build()

        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .setNetworkSpecifier(specifier)
            .build()

        connectivityManager.requestNetwork(request, object : ConnectivityManager.NetworkCallback() {
            override fun onUnavailable() {
                super.onUnavailable()
                continuation.resume(false)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                continuation.resume(false)
            }

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                continuation.resume(true)
            }
        })
    }
}